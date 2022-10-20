package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.functions.DriveFunctions;
import org.firstinspires.ftc.teamcode.functions.NavFunctions;
import org.firstinspires.ftc.teamcode.functions.TelemetryFunctions;
import org.firstinspires.ftc.teamcode.util.Data.DynamicData;
import org.firstinspires.ftc.teamcode.functions.PIDFunctions;
import org.firstinspires.ftc.teamcode.util.PID.PIDSettings;
import org.firstinspires.ftc.teamcode.util.Data.StaticData;
import org.firstinspires.ftc.teamcode.util.TelemetryData;
import org.firstinspires.ftc.teamcode.util.V2f;


@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="FTC 2022 XDrive", group="TeleOps")
public class XTeleOp extends LinearOpMode{

        @Override
        public void runOpMode() {
            StaticData s = new StaticData(
                    this.hardwareMap,
                    new PIDSettings(0.018f, 0.0f, -0.75f),
                    new boolean[] { false, true, false, true } );

            DynamicData d = new DynamicData();



            waitForStart();
            while(opModeIsActive()){



                V2f l = new V2f(
                        this.gamepad1.left_stick_x,
                        -this.gamepad1.left_stick_y);
                V2f r = new V2f(
                        this.gamepad1.right_stick_x,
                        -this.gamepad1.right_stick_y);




                NavFunctions.updateDt(s, d);
                NavFunctions.updateHeading(s, d);



                TelemetryData opModeTelemetry = new TelemetryData("OpMode");
                d.telemetryData.addChild(opModeTelemetry);

                //change target angle with input
                if(r.length() != 0){
                    d.drivePID.target = PIDFunctions.angleWrap(+r.getAngleDeg() - 90); }
                opModeTelemetry.addChild(new TelemetryData("Target Angle", d.drivePID.target));




                float PIDOut = PIDFunctions.updatePIDAngular(
                        s.drivePIDSettings,
                        d.drivePID,
                        d.heading,
                        (float)d.dt);
                opModeTelemetry.addChild(new TelemetryData("Angle", d.heading));
                opModeTelemetry.addChild(new TelemetryData("PID power", PIDOut));





                //rotate drive input to be relative to start angle, not current angle
                V2f in = new V2f(l.x, l.y);
                in = in.rotated(-d.heading);
                opModeTelemetry.addChild(new TelemetryData("Drive", in));

                DriveFunctions.setPower(
                        s,
                        in,
                        PIDOut);




                TelemetryFunctions.sendTelemetry(this.telemetry, d.telemetryData);
            }

        }

}
