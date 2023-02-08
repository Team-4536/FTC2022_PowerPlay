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
import org.firstinspires.ftc.teamcode.util.XRobot;

import java.util.List;
//test

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="X Parking Stock", group="Autos")
public class XAutoStandardParking extends LinearOpMode {



    @Override
    public void runOpMode() {


        XRobot.init(this.hardwareMap, this.telemetry, true);

        ObjectDetectionData detector = new ObjectDetectionData(this.hardwareMap, Constants.STANDARD_SLEEVE_MODEL);
        int zone = 0;




        XRobot.updateSystems(this.telemetry);
        waitForStart();
        while (opModeIsActive()) {

            XRobot.updateSystems(this.telemetry);

            if(zone == 0){

                List<Recognition> updatedRecognitions = detector.tfod.getUpdatedRecognitions();
                if(updatedRecognitions != null) {

                    for (Recognition recognition : updatedRecognitions) {
                        if (recognition.getLabel().equals(Constants.STANDARD_SLEEVE_MODEL.tags[0])) {
                            zone = 1;
                        }
                        else if (recognition.getLabel().equals(Constants.STANDARD_SLEEVE_MODEL.tags[1])) {
                            zone = 2;
                        }
                        else if (recognition.getLabel().equals(Constants.STANDARD_SLEEVE_MODEL.tags[2])) {
                            zone = 3;
                        }


                        if(zone != 0) {
                            XRobot.autoData.stages = Constants.parkingRoutines.get(zone -1); }
                    }
                }

            }
            else
            {
                XRobot.autoData.run();
            }

            XRobot.telemetry.addChild("Detected zone", zone);
        }
    }
}
