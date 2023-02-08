package org.firstinspires.ftc.teamcode.opModes.autos;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.util.Data.AprilTagDetectionPipeline;
import org.firstinspires.ftc.teamcode.util.XRobot;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.functions.DriveFunctions;
import org.firstinspires.ftc.teamcode.functions.NavFunctions;
import org.firstinspires.ftc.teamcode.functions.PIDFunctions;
import org.firstinspires.ftc.teamcode.functions.SequencerFunctions;
import org.firstinspires.ftc.teamcode.functions.TelemetryFunctions;
import org.firstinspires.ftc.teamcode.util.Constants;
import org.firstinspires.ftc.teamcode.util.Data.DriveData;
import org.firstinspires.ftc.teamcode.util.Data.NavData;
import org.firstinspires.ftc.teamcode.util.Data.ObjectDetectionData;
import org.firstinspires.ftc.teamcode.util.Data.PIDData;
import org.firstinspires.ftc.teamcode.util.Data.TelemetryData;
import org.firstinspires.ftc.teamcode.util.Step;
import org.firstinspires.ftc.teamcode.util.V2f;


import java.util.ArrayList;

//https://github.com/OpenFTC/EOCV-AprilTag-Plugin

@Disabled
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="OpenCV", group="Autos")
public class OpenCVCameraDetection extends LinearOpMode
{
        OpenCvCamera camera;
        AprilTagDetectionPipeline aprilTagDetectionPipeline;

        static final double FEET_PER_METER = 3.28084;

        // Lens intrinsics
        // UNITS ARE PIXELS
        // NOTE: this calibration is for the C920 webcam at 800x448.
        // You will need to do your own calibration for other configurations!
        double fx = 578.272;
        double fy = 578.272;
        double cx = 402.145;
        double cy = 221.506;

        // UNITS ARE METERS
        double tagsize = 0.166;

        int ID_TAG_OF_INTEREST = 1; // Tag ID 18 from the 36h11 family

        int foundTag = -1;
        int zone = -1;
        AprilTagDetection tagOfInterest = null;

        @Override
        public void runOpMode() {

                XRobot.init(this.hardwareMap, this.telemetry, true);

                int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
                camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
                aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

                camera.setPipeline(aprilTagDetectionPipeline);
                camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
                        @Override
                        public void onOpened() {
                                camera.startStreaming(800, 448, OpenCvCameraRotation.UPRIGHT);
                        }

                        @Override
                        public void onError(int errorCode) {

                        }
                });

                XRobot.telemetry.addChild("Initialized!", "");
                XRobot.updateSystems(this.telemetry);
                waitForStart();




                XRobot.updateSystems(this.telemetry);
                while (opModeIsActive()) {

                        if(foundTag != -1){
                                XRobot.nav.timer.reset();
                        }

                        float PIDOut = PIDFunctions.updatePIDAngular(
                                XRobot.drivePID,
                                XRobot.nav.heading,
                                (float)XRobot.nav.dt);

                        ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();
                        if (foundTag == -1) {
                                XRobot.telemetry.addChild("length", currentDetections.size());
                                if (currentDetections.size() != 0) {

                                        for (AprilTagDetection tag : currentDetections) {
                                                foundTag = tag.id;
                                                break;

                                        }
                                }
                        } else {
                                zone = foundTag + 1;
                                XRobot.telemetry.addChild("Zone Found:", zone);
                                XRobot.autoData.stages = Constants.parkingRoutines.get(foundTag);

                                //telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
                                //telemetry.addChild("\nDetected tag ID=%d", foundTag);
                        }


                }
                if(zone != 0){

                        XRobot.telemetry.addChild("Zone Found:", zone);
                        XRobot.autoData.run();
                }

                /*
                 * The START command just came in: now work off the latest snapshot acquired
                 * during the init loop.
                 */

                /* Update the telemetry */
                XRobot.updateSystems(this.telemetry);




                /* You wouldn't have this in your autonomous, this is just to prevent the sample from ending */


        }
}
