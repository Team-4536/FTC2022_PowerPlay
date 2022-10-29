package org.firstinspires.ftc.teamcode.opModes.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.functions.DriveFunctions;
import org.firstinspires.ftc.teamcode.functions.NavFunctions;
import org.firstinspires.ftc.teamcode.functions.TelemetryFunctions;
import org.firstinspires.ftc.teamcode.util.Constants;
import org.firstinspires.ftc.teamcode.util.Data.ArmData;
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

            ArmData arm = new ArmData(this.hardwareMap);


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
                float mod = Constants.defaultXDriveSpeed + ((1-Constants.defaultXDriveSpeed)*this.gamepad1.right_trigger);
                telemetry.addChild("Percent", mod);
                in = new V2f(
                        in.x * mod,
                        in.y * mod);
                in = in.rotated(-nav.heading);
                opModeTelemetry.addChild(new TelemetryData("Drive", in));

                DriveFunctions.setPower(
                        drive,
                        in,
                        PIDOut);







                //float liftSpeed = this.gamepad1.right_trigger - this.gamepad1.left_trigger;
                //float servoPosition = this.gamepad1.right_bumper?1:0 - (this.gamepad1.left_bumper?1:0);

                //arm.gripServo.setPosition(servoPosition);

                //int pos = arm.liftMotor.getCurrentPosition();
                //telemetry.addChild("Lift pos", pos);

                //arm.liftMotor.setPower(liftSpeed);


                //TelemetryData servoTarget = new TelemetryData("Servo");
                //telemetry.addChild(servoTarget);
                //servoTarget.addChild("Servo Target", liftSpeed);
                //servoTarget.addChild("Servo Pos", arm.gripServo.getPosition());



                TelemetryFunctions.sendTelemetry(this.telemetry, telemetry);
            }

        }

}
