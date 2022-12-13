/*package org.firstinspires.ftc.teamcode.util.Data;

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
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

public class VUforiaInit {
    private static final String VUFORIA_KEY =
            "ARtx29j/////AAABmc8Mmozqr0GsgPsH/NOLUkBPcj0u8eyHkeiAyftu6QAtlUKWRkZ5UPmI6mCAXfVgmJHq7dPpxL1kd5rCK4YO8ABWIbqITG6W6Kp8LqKWJnRWOVIBoOxtZsSkfvOu39s2NKVKbmNshfLUTkNS8Xs6NLOJS2mAHITqOPgR+68DhqEFQIvT1HRBplwsEeuWFVeiizCgmp3sBHZMWdNiCN0KS1AZLbF/+352OGpIkhFGPuMYSb7VdjLVUPRc3Pl7qoOISjVpIweyRMIrc/jkj3bEBULFbKjPyBa+sAwXnxiBxoFDVB7Fo2S5NsU6pW1w67wqYyHM8R1/JLEQJqWTFf+hrnLnPN+aew4/+C3q1XP14fBg";

    // Since ImageTarget trackables use mm to specifiy their dimensions, we must use mm for all the physical dimension.
    // We will define some constants and conversions here
    private static final float mmPerInch        = 25.4f;
    private static final float mmTargetHeight   = 6 * mmPerInch;          // the height of the center of the target image above the floor
    private static final float halfField        = 72 * mmPerInch;
    private static final float halfTile         = 12 * mmPerInch;
    private static final float oneAndHalfTile   = 36 * mmPerInch;

    // Class Members
    private OpenGLMatrix lastLocation   = null;

    private boolean targetVisible       = false;

    final float CAMERA_FORWARD_DISPLACEMENT  = 0.0f * mmPerInch;   // eg: Enter the forward distance from the center of the robot to the camera lens
    final float CAMERA_VERTICAL_DISPLACEMENT = 6.0f * mmPerInch;   // eg: Camera is 6 Inches above ground
    final float CAMERA_LEFT_DISPLACEMENT     = 0.0f * mmPerInch;   // eg: Enter the left distance from the center of the robot to the camera lens


    VUforiaInit(HardwareMap hardwareMap){

                // VUFORIA INIT
                // Same code as object detector, besides camMonitor, plz look into more

                int cameraMonitorViewId = hardwareMap.appContext.getResources()
                        .getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
                VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
                parameters.vuforiaLicenseKey = VUFORIA_KEY;
                parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");;
                // parameters.useExtendedTracking = false;
                VuforiaLocalizer vuforia = ClassFactory.getInstance().createVuforia(parameters);

                //============================================================================








                //SETUP TARGETS
                // Load the data sets for the trackable objects. These particular data
                // sets are stored in the 'assets' part of our application.
                VuforiaTrackables targets = vuforia.loadTrackablesFromAsset("PowerPlay");
                List<VuforiaTrackable> allTrackables = new ArrayList<>(targets);

                /*
                 * In order for localization to work, we need to tell the system where each target is on the field, and
                 * where the phone resides on the robot.  These specifications are in the form of <em>transformation matrices.</em>
                 * Transformation matrices are a central, important concept in the math here involved in localization.
                 * See <a href="https://en.wikipedia.org/wiki/Transformation_matrix">Transformation Matrix</a>
                 * for detailed information. Commonly, you'll encounter transformation matrices as instances
                 * of the {@link OpenGLMatrix} class.
                 *
                 * If you are standing in the Red Alliance Station looking towards the center of the field,
                 *     - The X axis runs from your left to the right. (positive from the center to the right)
                 *     - The Y axis runs from the Red Alliance Station towards the other side of the field
                 *       where the Blue Alliance Station is. (Positive is from the center, towards the BlueAlliance station)
                 *     - The Z axis runs from the floor, upwards towards the ceiling.  (Positive is above the floor)
                 *
                 * Before being transformed, each target image is conceptually located at the origin of the field's
                 *  coordinate system (the center of the field), facing up.


                // Name and locate each trackable object
                identifyTarget(targets, 0, "Red Left",
                        -halfField,  -oneAndHalfTile, mmTargetHeight, 90, 0,  90);

                identifyTarget(targets, 1, "Red Right",
                        halfField,  -oneAndHalfTile, mmTargetHeight, 90, 0, -90);

                identifyTarget(targets, 2, "Blue Right",
                        -halfField,   oneAndHalfTile, mmTargetHeight, 90, 0,  90);

                identifyTarget(targets, 3, "Blue Left",
                        halfField,   oneAndHalfTile, mmTargetHeight, 90, 0, -90);
                //======================================================================================









                /*
                 * Create a transformation matrix describing where the camera is on the robot.
                 *
                 * Info:  The coordinate frame for the robot looks the same as the field.
                 * The robot's "forward" direction is facing out along X axis, with the LEFT side facing out along the Y axis.
                 * Z is UP on the robot.  This equates to a bearing angle of Zero degrees.
                 *
                 * For a WebCam, the default starting orientation of the camera is looking UP (pointing in the Z direction),
                 * with the wide (horizontal) axis of the camera aligned with the X axis, and
                 * the Narrow (vertical) axis of the camera aligned with the Y axis
                 *
                 * But, this example assumes that the camera is actually facing forward out the front of the robot.
                 * So, the "default" camera position requires two rotations to get it oriented correctly.
                 * 1) First it must be rotated +90 degrees around the X axis to get it horizontal (its now facing out the right side of the robot)
                 * 2) Next it must be be rotated +90 degrees (counter-clockwise) around the Z axis to face forward.
                 *
                 * Finally the camera can be translated to its actual mounting position on the robot.
                 *      In this example, it is centered on the robot (left-to-right and front-to-back), and 6 inches above ground level.



                OpenGLMatrix cameraLocationOnRobot = OpenGLMatrix
                        .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
                        .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XZY, DEGREES, 90, 90, 0));

                /**  Let all the trackable listeners know where the camera is.
                for (VuforiaTrackable trackable : allTrackables) {
                    ((VuforiaTrackableDefaultListener) trackable.getListener()).
                            setCameraLocationOnRobot(parameters.cameraName, cameraLocationOnRobot);
                }

                targets.activate();
        /***
         * Identify a target by naming it, and setting its position and orientation on the field
         * @param targetIndex .
         * @param targetName .
         * @param dx, dy, dz  Target offsets in x,y,z axes
         * @param rx, ry, rz  Target rotations in x,y,z axes

        void identifyTarget(VuforiaTrackables targets, int targetIndex, String targetName,
                float dx, float dy, float dz,
                float rx, float ry, float rz) {

                    VuforiaTrackable aTarget = targets.get(targetIndex);
                    aTarget.setName(targetName);
                    aTarget.setLocation(OpenGLMatrix.translation(dx, dy, dz)
                            .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, rx, ry, rz)));
        }
    }

}
*/