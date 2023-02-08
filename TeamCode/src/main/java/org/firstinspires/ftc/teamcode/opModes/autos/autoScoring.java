package org.firstinspires.ftc.teamcode.opModes.autos;


import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XZY;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
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

@Autonomous(name="XAutoScoring")
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

        V2f target = new V2f(-1232, -1984);



        // note the 0 translation
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







        double timeStash = 0;

        waitForStart();
        while (!isStopRequested()) {

            TelemetryData t = new TelemetryData();
            NavFunctions.updateDt(nav);
            NavFunctions.updateHeading(nav);




            // check all the trackable targets to see which one (if any) is visible.
            boolean targetVisible = false;
            boolean newData = false;
            for (VuforiaTrackable trackable : allTrackables) {
                if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {
                    telemetry.addData("Visible Target", trackable.getName());
                    targetVisible = true;

                    OpenGLMatrix robotLocationTransform =
                            ((VuforiaTrackableDefaultListener)trackable.getListener()).getUpdatedRobotLocation();

                    if (robotLocationTransform != null) {
                        lastLocation = robotLocationTransform;
                        newData = true;
                        timeStash = nav.timer.seconds();
                    }

                    break;
                }
            }












            float PIDOut = PIDFunctions.updatePIDAngular(
                    drivePID,
                    nav.heading,
                    (float)nav.dt);




            if (targetVisible) {


                VectorF pos = lastLocation.getTranslation();
                V2f rel = new V2f(pos.get(0) - target.x, pos.get(1) - target.y);


                t.addChild(new TelemetryData("Relative", rel));
                t.addChild(new TelemetryData("Actual Pos", new V2f(pos.get(0), pos.get(1))));
                t.addChild(new TelemetryData("Target", target));



                rel = rel.normalized();

                if(timeStash - nav.timer.seconds() < 0.5f) {

                    DriveFunctions.setPower(drive, rel.multiply(0.2f), PIDOut);
                }


            }
            else {
                t.addChild("Visible Target", "none");
                DriveFunctions.setPower(drive, new V2f(0.0f, 0.0f), PIDOut);
            }


            // ??????????????????????????????????????
            /*
            *           +Y       BL
            *
            *       O <- target
            * -X                 +X
            *
            *
            *  RL       -Y
            * */

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
