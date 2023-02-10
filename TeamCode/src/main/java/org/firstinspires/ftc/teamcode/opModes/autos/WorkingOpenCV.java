package org.firstinspires.ftc.teamcode.opModes.autos;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.sun.tools.javac.util.List;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.util.Data.AprilTagDetectionPipeline;
import org.firstinspires.ftc.teamcode.util.Data.ArmData;
import org.firstinspires.ftc.teamcode.util.Stage;
import org.firstinspires.ftc.teamcode.util.XRobot;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.firstinspires.ftc.teamcode.functions.DriveFunctions;
import org.firstinspires.ftc.teamcode.functions.NavFunctions;
import org.firstinspires.ftc.teamcode.functions.PIDFunctions;
import org.firstinspires.ftc.teamcode.functions.SequencerFunctions;
import org.firstinspires.ftc.teamcode.functions.TelemetryFunctions;
import org.firstinspires.ftc.teamcode.util.Constants;
import org.firstinspires.ftc.teamcode.util.Data.DriveData;
import org.firstinspires.ftc.teamcode.util.Data.NavData;
import org.firstinspires.ftc.teamcode.util.Data.PIDData;
import org.firstinspires.ftc.teamcode.util.Data.TelemetryData;
import org.firstinspires.ftc.teamcode.util.Step;
import org.firstinspires.ftc.teamcode.util.V2f;


import java.util.ArrayList;

//https://github.com/OpenFTC/EOCV-AprilTag-Plugin


@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="WorkingOpenCV", group="Autos")

public class WorkingOpenCV extends LinearOpMode
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

        XRobot.init(this.hardwareMap, this.telemetry, true);
        XRobot.telemetry.addChild("amoongus"," open");
        XRobot.updateSystems(this.telemetry);
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                camera.startStreaming(800, 448, OpenCvCameraRotation.UPRIGHT);
                XRobot.telemetry.addChild("amoongus"," open");
            }

            @Override
            public void onError(int errorCode) {
                XRobot.telemetry.addChild("amoongus","error");
                //XRobot.updateSystems(this.telemetry);
            }
        });

        XRobot.telemetry.addChild("April tag pip initialized!", "");
        XRobot.nav.timer.reset();
        XRobot.updateSystems(this.telemetry);




        while(!isStopRequested() && foundTag == -1){
            XRobot.telemetry.addChild("is searching 1", "");
            XRobot.updateSystems(this.telemetry);
            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();
            if (foundTag == -1) {
                XRobot.telemetry.addChild("is searching 2", "");
                XRobot.updateSystems(this.telemetry);
                if (currentDetections.size() != 0) {

                    for (AprilTagDetection tag : currentDetections) {
                        foundTag = tag.id;
                        break;
                    }
                }
            }

        }



        if(foundTag != -1) {
            XRobot.telemetry.addChild("Tag Found: ", foundTag); }
        else { XRobot.telemetry.addChild("No tag identified", ""); }

        XRobot.updateSystems(this.telemetry);






        waitForStart();

        XRobot.autoData.stages = Constants.parkingRoutines.get(foundTag);

        XRobot.updateSystems(this.telemetry);
        while (opModeIsActive()) {

            XRobot.telemetry.addChild("current time", XRobot.nav.timer.seconds());

            XRobot.autoData.run();

            XRobot.updateSystems(this.telemetry);
        }
    }


}

