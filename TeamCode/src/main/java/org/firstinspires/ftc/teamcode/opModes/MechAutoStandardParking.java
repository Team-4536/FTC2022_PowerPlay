package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.functions.DriveFunctions;
import org.firstinspires.ftc.teamcode.functions.NavFunctions;
import org.firstinspires.ftc.teamcode.functions.PIDFunctions;
import org.firstinspires.ftc.teamcode.functions.SequencerFunctions;
import org.firstinspires.ftc.teamcode.functions.TelemetryFunctions;
import org.firstinspires.ftc.teamcode.util.Data.DynamicData;
import org.firstinspires.ftc.teamcode.util.Data.ObjectDetectionData;
import org.firstinspires.ftc.teamcode.util.PID.PIDSettings;
import org.firstinspires.ftc.teamcode.util.Data.StaticData;
import org.firstinspires.ftc.teamcode.util.Step;
import org.firstinspires.ftc.teamcode.util.V2f;

import java.util.List;


@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="MechAuto CV Parking", group="Autos")
public class MechAutoStandardParking extends LinearOpMode {



    @Override
    public void runOpMode() {

        StaticData s = new StaticData(
                this.hardwareMap,
                new PIDSettings(0.018f, 0.0f, -0.2f),
                new boolean[]{true, false, true, false});

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

        DynamicData d = new DynamicData();



        waitForStart();
        s.timer.reset();





        ObjectDetectionData detector = new ObjectDetectionData(this.hardwareMap);


        int zone = 0;
        while (opModeIsActive()) {

            if(zone == 0){

                List<Recognition> updatedRecognitions = detector.tfod.getUpdatedRecognitions();
                if(updatedRecognitions != null) {

                    for (Recognition recognition : updatedRecognitions) {
                        if (recognition.getLabel().equals("1 Bolt")) {
                            telemetry.addData("Object Detected", "Bolt");
                            zone = 1;
                        }
                        if (recognition.getLabel().equals("2 Bulb")) {
                            telemetry.addData("Object Detected", "Bulb");
                            zone = 2;
                        }
                        if (recognition.getLabel().equals("3 Panel")) {
                            telemetry.addData("Object Detected", "Panel");
                            zone = 3;
                        }

                    }
                }

                if(zone != 0){
                    s.timer.reset();
                }
            }
            else
            {
                NavFunctions.updateDt(s, d);
                NavFunctions.updateHeading(s, d);



                float PIDOut = PIDFunctions.updatePIDAngular(
                        s.drivePIDSettings,
                        d.drivePID,
                        d.heading,
                        (float)d.dt);

                Step c = SequencerFunctions.getStep(autos[zone], (float)s.timer.seconds());


                if(c.data.length == 0) {
                    d.telemetryData.addChild("Step is invalid!", "");
                    DriveFunctions.setPower(s, new V2f(0, 0), PIDOut);
                }
                else
                {
                    DriveFunctions.setPower(s, new V2f(c.data[0], c.data[1]), PIDOut);
                }



                TelemetryFunctions.sendTelemetry(this.telemetry, d.telemetryData);
            }
        }
    }
}
