package org.firstinspires.ftc.teamcode.util.Data;


import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XZY;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.sun.tools.javac.util.List;
import com.vuforia.Trackable;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.util.Constants;
import org.firstinspires.ftc.teamcode.util.V2f;
import org.firstinspires.ftc.teamcode.util.XRobot;

import java.util.ArrayList;


public class NavImageDetectionPipeline {

    public V2f translation = new V2f();
    public List<V2f> imagePositions;
    public int imageVisible;



    VuforiaLocalizer vuforia;
    ArrayList<VuforiaTrackable> trackables;
    VuforiaTrackables targets;



    public NavImageDetectionPipeline(HardwareMap hardwareMap) {

        // VUFORIA INIT
        // Same code as object detector, besides camMonitor, plz look into more
        int cameraMonitorViewId = hardwareMap.appContext.getResources()
                .getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = Constants.VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");;
        // parameters.useExtendedTracking = false;
        vuforia = ClassFactory.getInstance().createVuforia(parameters);




        // note the 0 translation
        OpenGLMatrix cameraLocationOnRobot = OpenGLMatrix
                .translation(0, 0, 0)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XZY, DEGREES, 90, 90, 0));





        //=====================================================================================
        this.targets = vuforia.loadTrackablesFromAsset("PowerPlay");
        this.trackables = new ArrayList<>();
        this.trackables.addAll(this.targets);

        this.imagePositions = List.of(

            new V2f(-Constants.HALF_FIELD_MM,  -(Constants.HALF_FIELD_MM + Constants.HALF_TILE_MM)),
            new V2f( Constants.HALF_FIELD_MM,  -(Constants.HALF_FIELD_MM + Constants.HALF_TILE_MM)),
            new V2f(-Constants.HALF_FIELD_MM,   (Constants.HALF_FIELD_MM + Constants.HALF_TILE_MM)),
            new V2f( Constants.HALF_FIELD_MM,   (Constants.HALF_FIELD_MM + Constants.HALF_TILE_MM))
        );
        registerTarget(targets, 0, "Red Left",
                imagePositions.get(0).x, imagePositions.get(0).y, Constants.IMG_HEIGHT_MM, 90, 0,  90);
        registerTarget(targets, 1, "Red Right",
                imagePositions.get(1).x, imagePositions.get(1).y, Constants.IMG_HEIGHT_MM, 90, 0,  -90);
        registerTarget(targets, 2, "Blue Right",
                imagePositions.get(2).x, imagePositions.get(2).y, Constants.IMG_HEIGHT_MM, 90, 0,  90);
        registerTarget(targets, 3, "Blue Left",
                imagePositions.get(3).x, imagePositions.get(3).y, Constants.IMG_HEIGHT_MM, 90, 0,  -90);


        for (VuforiaTrackable trackable : this.trackables) {
            ((VuforiaTrackableDefaultListener) trackable.getListener()).
                    setCameraLocationOnRobot(parameters.cameraName, cameraLocationOnRobot);
        }
        this.targets.activate();
        //======================================================================================





    }

    public void deactivate() {
        this.targets.deactivate();
    }


















    public void update() {

        boolean targetFound = false;
        int i = 0;
        for (VuforiaTrackable trackable : this.trackables) {
            if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {

                targetFound = true;
                XRobot.telemetry.addChild("Visible Target", trackable.getName());
                this.imageVisible = i;

                OpenGLMatrix robotLocationTransform =
                        ((VuforiaTrackableDefaultListener)trackable.getListener()).getUpdatedRobotLocation();

                if (robotLocationTransform != null) {
                    VectorF v = robotLocationTransform.getTranslation();
                    this.translation = new V2f(v.get(0), v.get(1));
                }

                break;
            }

            i++;
        }

        if(!targetFound) {
            XRobot.telemetry.addChild("Visible Target", "None");
            this.imageVisible = -1;
        };

        XRobot.telemetry.addChild(new TelemetryData("Position Estimate", this.translation));
    }






    private void registerTarget(VuforiaTrackables targets, int targetIndex, String targetName,
                        float dx, float dy, float dz,
                        float rx, float ry, float rz) {

        VuforiaTrackable aTarget = targets.get(targetIndex);
        aTarget.setName(targetName);
        aTarget.setLocation(OpenGLMatrix.translation(dx, dy, dz)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, rx, ry, rz)));

        this.imagePositions.add(new V2f(dx, dy));
    }

}
