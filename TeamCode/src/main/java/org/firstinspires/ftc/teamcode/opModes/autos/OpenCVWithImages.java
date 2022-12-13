package org.firstinspires.ftc.teamcode.opModes.autos;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.functions.DriveFunctions;
import org.firstinspires.ftc.teamcode.functions.NavFunctions;
import org.firstinspires.ftc.teamcode.functions.PIDFunctions;
import org.firstinspires.ftc.teamcode.functions.SequencerFunctions;
import org.firstinspires.ftc.teamcode.functions.TelemetryFunctions;
import org.firstinspires.ftc.teamcode.util.Constants;
import org.firstinspires.ftc.teamcode.util.Data.AprilTagDetectionPipeline;
import org.firstinspires.ftc.teamcode.util.Data.DriveData;
import org.firstinspires.ftc.teamcode.util.Data.NavData;
import org.firstinspires.ftc.teamcode.util.Data.PIDData;
import org.firstinspires.ftc.teamcode.util.Data.TelemetryData;
import org.firstinspires.ftc.teamcode.util.Step;
import org.firstinspires.ftc.teamcode.util.V2f;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;

//https://github.com/OpenFTC/EOCV-AprilTag-Plugin


@Autonomous(name="OpenCV and Feild", group="Autos")

public class OpenCVWithImages extends LinearOpMode
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

    AprilTagDetection tagOfInterest = null;

    @Override
    public void runOpMode() {
        TelemetryData telemetry = new TelemetryData("State");

        NavData nav = new NavData(this.hardwareMap);

        DriveData drive = new DriveData(
                new boolean[]{true, false, true, false},
                this.hardwareMap);
        PIDData drivePID = new PIDData(0.018f, 0.0f, -0.2f);


        Step[][] autos = new Step[][]{

                new Step[]{},

                new Step[]{
                        new Step(new float[]{-0.4f, 0f}, 2),
                        new Step(new float[]{0, 0.4f}, 2)
                },

                new Step[]{
                        new Step(new float[]{0, 0.4f}, 2),
                },

                new Step[]{
                        new Step(new float[]{0.4f, 0f}, 2),
                        new Step(new float[]{0, 0.4f}, 2)
                }
        };

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

        telemetry.addChild("Initialized!", "");
        TelemetryFunctions.sendTelemetry(this.telemetry, telemetry);
        waitForStart();
        nav.timer.reset();


        int zone = 0;





        while (opModeIsActive()) {


            NavFunctions.updateDt(nav);
            NavFunctions.updateHeading(nav);

            float PIDOut = PIDFunctions.updatePIDAngular(
                    drivePID,
                    nav.heading,
                    (float) nav.dt);


            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();
            if (foundTag == -1) {
                telemetry.addChild("length", currentDetections.size());
                if (currentDetections.size() != 0) {

                    for (AprilTagDetection tag : currentDetections) {
                        foundTag = tag.id;
                        telemetry.addChild("Tag Found:", foundTag);
                        TelemetryFunctions.sendTelemetry(this.telemetry, telemetry);
                        break;

                    }
                }


                if (foundTag != -1) {
                    nav.timer.reset();
                }

            }
            else {
                telemetry.addChild("Tag Found:", foundTag);
                //telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
                //telemetry.addChild("\nDetected tag ID=%d", foundTag);
                TelemetryFunctions.sendTelemetry(this.telemetry, telemetry);
                if (foundTag == 0) {
                    telemetry.addChild("Tag Found:", foundTag);
                    zone = 1;
                }
                if (foundTag == 1) {
                    telemetry.addChild("Tag Found:", foundTag);
                    zone = 2;
                }
                if (foundTag == 2) {
                    telemetry.addChild("Tag Found:", foundTag);
                    zone = 3;
                }

                TelemetryFunctions.sendTelemetry(this.telemetry, telemetry);

                //if the zone is found this iteration, reset timer to work w/ sequencer

            }

            /*if (zone != 0) {
                //if a zone is detected, set motor pwr with current step of that zones
                //sequence

                Step c = SequencerFunctions.getStep(Constants.PARKING_SEQUENCES[zone], (float) nav.timer.seconds());


                if (c.data.length == 0) {
                    telemetry.addChild("Step invalid", "");
                    DriveFunctions.setPower(
                            drive,
                            new V2f(0, 0),
                            PIDOut);
                } else {
                    DriveFunctions.setPower(
                            drive,
                            new V2f(c.data[0], c.data[1]),
                            PIDOut);
                }
            }*/



            telemetry.addChild("Detected zone", zone);
            telemetry.addChild("current time", nav.timer.seconds());
            TelemetryFunctions.sendTelemetry(this.telemetry, telemetry);

            /*if (foundTag == 1) {
                telemetry.addChild("Tag Found:", foundTag);
            }

            if (foundTag == 2) {
                telemetry.addChild("Tag Found:", foundTag);
            }*/
        }
    }


}

