package org.firstinspires.ftc.teamcode.opModes.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.functions.DriveFunctions;
import org.firstinspires.ftc.teamcode.functions.NavFunctions;
import org.firstinspires.ftc.teamcode.functions.TelemetryFunctions;
import org.firstinspires.ftc.teamcode.util.Data.DriveData;
import org.firstinspires.ftc.teamcode.functions.PIDFunctions;
import org.firstinspires.ftc.teamcode.util.Data.NavData;
import org.firstinspires.ftc.teamcode.util.Data.PIDData;
import org.firstinspires.ftc.teamcode.util.Data.TelemetryData;
import org.firstinspires.ftc.teamcode.util.V2f;


@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="X teleOp abs", group="TeleOps")
public class XTeleOpAbsolute extends LinearOpMode{

        @Override
        public void runOpMode() {


            TelemetryData telemetry = new TelemetryData("State");

            NavData nav = new NavData(this.hardwareMap);

            DriveData drive = new DriveData(
                    new boolean[] {true, false, true, false},
                    this.hardwareMap);
            PIDData drivePID = new PIDData(0.018f, 0.0f, -0.2f);




            waitForStart();
            while(opModeIsActive()){



                V2f l = new V2f(
                        this.gamepad1.left_stick_x,
                        -this.gamepad1.left_stick_y);
                V2f r = new V2f(
                        this.gamepad1.right_stick_x,
                        -this.gamepad1.right_stick_y);




                NavFunctions.updateDt(nav);
                NavFunctions.updateHeading(nav);



                TelemetryData opModeTelemetry = new TelemetryData("OpMode");
                telemetry.addChild(opModeTelemetry);

                //change target angle with input
                if(r.length() != 0){
                    drivePID.target = PIDFunctions.angleWrap(+r.getAngleDeg() - 90); }
                opModeTelemetry.addChild(new TelemetryData("Target Angle", drivePID.target));




                float PIDOut = PIDFunctions.updatePIDAngular(
                        drivePID,
                        nav.heading,
                        (float)nav.dt);
                opModeTelemetry.addChild(new TelemetryData("Angle", nav.heading));
                opModeTelemetry.addChild(new TelemetryData("PID power", PIDOut));





                //rotate drive input to be relative to start angle, not current angle
                V2f in = new V2f(l.x, l.y);
                in = in.rotated(-nav.heading);
                opModeTelemetry.addChild(new TelemetryData("Drive", in));

                DriveFunctions.setPower(
                        drive,
                        in,
                        PIDOut);




                TelemetryFunctions.sendTelemetry(this.telemetry, telemetry);
            }

        }

}
