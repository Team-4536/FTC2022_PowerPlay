package org.firstinspires.ftc.teamcode.opModes.autos;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

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

import java.util.List;


@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="MechAuto CV Parking Standard", group="Autos")
public class MechAutoStandardParking extends LinearOpMode {



    @Override
    public void runOpMode() {


        TelemetryData telemetry = new TelemetryData("State");

        NavData nav = new NavData(this.hardwareMap);

        DriveData drive = new DriveData(
                new boolean[] {true, false, true, false},
                this.hardwareMap);
        PIDData drivePID = new PIDData(0.018f, 0.0f, -0.2f);


        Step[][] autos = new Step[][]{

                new Step[] { },

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

        ObjectDetectionData detector = new ObjectDetectionData(this.hardwareMap, Constants.STANDARD_SLEEVE_MODEL);
        int zone = 0;




        telemetry.addChild("Initialized!", "");
        TelemetryFunctions.sendTelemetry(this.telemetry, telemetry);
        waitForStart();
        nav.timer.reset();
        while (opModeIsActive()) {


            NavFunctions.updateDt(nav);
            NavFunctions.updateHeading(nav);

            float PIDOut = PIDFunctions.updatePIDAngular(
                    drivePID,
                    nav.heading,
                    (float)nav.dt);



            if(zone == 0){

                List<Recognition> updatedRecognitions = detector.tfod.getUpdatedRecognitions();
                if(updatedRecognitions != null) {

                    for (Recognition recognition : updatedRecognitions) {
                        if (recognition.getLabel().equals(Constants.STANDARD_SLEEVE_MODEL.tags[0])) {
                            telemetry.addChild("Object Detected", "Bolt");
                            zone = 1;
                        }
                        if (recognition.getLabel().equals(Constants.STANDARD_SLEEVE_MODEL.tags[1])) {
                            telemetry.addChild("Object Detected", "Bulb");
                            zone = 2;
                        }
                        if (recognition.getLabel().equals(Constants.STANDARD_SLEEVE_MODEL.tags[2])) {
                            telemetry.addChild("Object Detected", "Panel");
                            zone = 3;
                        }

                    }
                }

                if(zone != 0){
                    nav.timer.reset();
                }
            }
            else
            {
                //if a zone is detected, set motor pwr with current step of that zones
                //sequence

                Step c = SequencerFunctions.getStep(autos[zone], (float)nav.timer.seconds());


                if(c.data.length == 0) {
                    telemetry.addChild("Step invalid", "");
                    DriveFunctions.setPower(
                            drive,
                            new V2f(0, 0),
                            PIDOut);
                }
                else
                {
                    DriveFunctions.setPower(
                            drive,
                            new V2f(c.data[0], c.data[1]),
                            PIDOut);
                }
            }

            telemetry.addChild("Detected zone", zone);
            TelemetryFunctions.sendTelemetry(this.telemetry, telemetry);
        }
    }
}
