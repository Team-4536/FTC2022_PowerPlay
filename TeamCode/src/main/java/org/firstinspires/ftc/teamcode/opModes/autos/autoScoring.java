package org.firstinspires.ftc.teamcode.opModes.autos;


import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XZY;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.functions.DriveFunctions;
import org.firstinspires.ftc.teamcode.functions.NavFunctions;
import org.firstinspires.ftc.teamcode.functions.PIDFunctions;
import org.firstinspires.ftc.teamcode.functions.TelemetryFunctions;
import org.firstinspires.ftc.teamcode.util.Constants;
import org.firstinspires.ftc.teamcode.util.Data.DriveData;
import org.firstinspires.ftc.teamcode.util.Data.NavData;
import org.firstinspires.ftc.teamcode.util.Data.PIDData;
import org.firstinspires.ftc.teamcode.util.Data.TelemetryData;
import org.firstinspires.ftc.teamcode.util.V2f;

import java.util.ArrayList;
import java.util.List;

@TeleOp(name="XAutoScoring", group ="Autos")
public class autoScoring extends LinearOpMode {


    // Class Members
    private OpenGLMatrix lastLocation   = null;

    @Override public void runOpMode() {

        // VUFORIA INIT
        // Same code as object detector, besides camMonitor, plz look into more
        int cameraMonitorViewId = hardwareMap.appContext.getResources()
                .getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = Constants.VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");;
        // parameters.useExtendedTracking = false;
        VuforiaLocalizer vuforia = ClassFactory.getInstance().createVuforia(parameters);

        DriveData drive = new DriveData(Constants.XFlipMap, this.hardwareMap);
        NavData nav = new NavData(this.hardwareMap);
        PIDData drivePID = new PIDData(0.015f, 0.0f, -0.2f);
        drivePID.target=90;










        // forward from center
        //final float CAMERA_FORWARD_DISPLACEMENT  = 0.0f * Constants.MM_PER_INCH;
        // distance up
        //final float CAMERA_VERTICAL_DISPLACEMENT = 6.0f * Constants.MM_PER_INCH;
        //final float CAMERA_LEFT_DISPLACEMENT     = 0.0f * Constants.MM_PER_INCH;


        OpenGLMatrix cameraLocationOnRobot = OpenGLMatrix
                .translation(0, 0, 0)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XZY, DEGREES, 90, 90, 0));










        //=====================================================================================
        VuforiaTrackables targets = vuforia.loadTrackablesFromAsset("PowerPlay");
        List<VuforiaTrackable> allTrackables = new ArrayList<>(targets);

        registerTarget(targets, 0, "Red Left",
                -Constants.HALF_FIELD_MM,  -(Constants.HALF_FIELD_MM + Constants.HALF_TILE_MM), Constants.IMG_HEIGHT_MM, 90, 0,  90);

        registerTarget(targets, 1, "Red Right",
                Constants.HALF_FIELD_MM,  -(Constants.HALF_FIELD_MM + Constants.HALF_TILE_MM), Constants.IMG_HEIGHT_MM, 90, 0, -90);

        registerTarget(targets, 2, "Blue Right",
                -Constants.HALF_FIELD_MM, (Constants.HALF_FIELD_MM + Constants.HALF_TILE_MM), Constants.IMG_HEIGHT_MM, 90, 0,  90);

        registerTarget(targets, 3, "Blue Left",
                Constants.HALF_FIELD_MM, (Constants.HALF_FIELD_MM + Constants.HALF_TILE_MM), Constants.IMG_HEIGHT_MM, 90, 0, -90);




        for (VuforiaTrackable trackable : allTrackables) {
            ((VuforiaTrackableDefaultListener) trackable.getListener()).
                    setCameraLocationOnRobot(parameters.cameraName, cameraLocationOnRobot);
        }
        targets.activate();
        //======================================================================================









        waitForStart();
        while (!isStopRequested()) {

            TelemetryData t = new TelemetryData();
            NavFunctions.updateDt(nav);
            NavFunctions.updateHeading(nav);

            // check all the trackable targets to see which one (if any) is visible.
            boolean targetVisible = false;
            for (VuforiaTrackable trackable : allTrackables) {
                if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {
                    telemetry.addData("Visible Target", trackable.getName());
                    targetVisible = true;

                    // getUpdatedRobotLocation() will return null if no new information is available since
                    // the last time that call was made, or if the trackable is not currently visible.
                    OpenGLMatrix robotLocationTransform =
                            ((VuforiaTrackableDefaultListener)trackable.getListener()).getUpdatedRobotLocation();
                    if (robotLocationTransform != null) {
                        lastLocation = robotLocationTransform;
                    }
                    break;
                }
            }

            boolean updated = false;
            VectorF translation = new VectorF(0, 0, 0);
            if (targetVisible) {

                updated = true;

                // express position (translation) of robot in inches.
                if(!translation.equals(lastLocation.getTranslation())){
                    nav.timer.reset();
                    translation = lastLocation.getTranslation();
                    t.addChild("SAME", "");
                }

                TelemetryData pos = new TelemetryData("Pos");
                t.addChild(pos);
                pos.addChild("x", translation.get(0));
                pos.addChild("y", translation.get(1));
                pos.addChild("z", translation.get(2));
            }
            else {
                t.addChild("Visible Target", "none");
            }



            float PIDOut = PIDFunctions.updatePIDAngular(
                    drivePID,
                    nav.heading,
                    (float)nav.dt);

            if(updated){


                VectorF pos = lastLocation.getTranslation();
                float x = pos.get(0);
                float y = pos.get(1);

                //NTS: run starting w/ the bot facing the wall!
                V2f rel = new V2f(x - (-1657), y - (-2116));
                TelemetryData r = new TelemetryData("Relative");
                r.addChild("x", rel.x);
                r.addChild("y", rel.y);



                V2f d = new V2f(rel.x / 3000.0f, rel.y / 3000.0f);
                d = rel.rotated(-nav.heading);
                TelemetryData dt = new TelemetryData("drkizghzdrgrdgzgzri;gzrjngrzgrzji");
                t.addChild(dt);
                dt.addChild("x", d.x);
                dt.addChild("y", d.y);

                /*
                *           +Y       BL
                *
                *       O <- target
                * -X                 +X
                *
                *
                *  RL       -Y
                * */

                if(nav.timer.seconds() < 0.1f){
                    // DriveFunctions.setPower(drive, rel, PIDOut);
                }

            }

            DriveFunctions.setPower(drive, new V2f(0.0f, 0.0f), PIDOut);








            TelemetryFunctions.sendTelemetry(this.telemetry, t);
        }

        targets.deactivate();
    }













    void registerTarget(VuforiaTrackables targets, int targetIndex, String targetName,
                        float dx, float dy, float dz,
                        float rx, float ry, float rz) {

        VuforiaTrackable aTarget = targets.get(targetIndex);
        aTarget.setName(targetName);
        aTarget.setLocation(OpenGLMatrix.translation(dx, dy, dz)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, rx, ry, rz)));
    }
}
