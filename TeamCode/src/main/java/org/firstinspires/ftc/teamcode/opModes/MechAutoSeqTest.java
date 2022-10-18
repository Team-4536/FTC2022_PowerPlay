package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.functions.DriveFunctions;
import org.firstinspires.ftc.teamcode.functions.NavFunctions;
import org.firstinspires.ftc.teamcode.functions.PIDFunctions;
import org.firstinspires.ftc.teamcode.functions.SequencerFunctions;
import org.firstinspires.ftc.teamcode.functions.TelemetryFunctions;
import org.firstinspires.ftc.teamcode.util.DynamicData;
import org.firstinspires.ftc.teamcode.util.PID.PIDSettings;
import org.firstinspires.ftc.teamcode.util.StaticData;
import org.firstinspires.ftc.teamcode.util.Step;
import org.firstinspires.ftc.teamcode.util.V2f;


@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="MechAuto Sequencer Test", group="Autos")
public class MechAutoSeqTest extends LinearOpMode {



    @Override
    public void runOpMode() {

        StaticData s = new StaticData(
                this.hardwareMap,
                new PIDSettings(0.018f, 0.0f, -0.2f),
                new boolean[]{true, false, true, false});

        s.sequencerSteps = new Step[] {
                new Step(new float[]{0, 0.5f}, 2),
                new Step(new float[]{0, -0.5f}, 2),
        };


        DynamicData d = new DynamicData();



        waitForStart();
        s.timer.reset();
        while (opModeIsActive()) {


            NavFunctions.updateDt(s, d);
            NavFunctions.updateHeading(s, d);



            float PIDOut = PIDFunctions.updatePIDAngular(
                    s.drivePIDSettings,
                    d.drivePID,
                    d.heading,
                    (float)d.dt);

            Step c = SequencerFunctions.getStep(s.sequencerSteps, (float)s.timer.seconds());


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
