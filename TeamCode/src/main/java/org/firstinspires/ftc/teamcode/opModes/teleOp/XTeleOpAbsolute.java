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
        PIDData drivePID = new PIDData(0.015f, 0.0f, -0.2f);

        ArmData arm = new ArmData(this.hardwareMap);




        arm.gripServo.setPosition(0.55f);
        while(!arm.limitSwitch.isPressed()){
            arm.liftMotor.setPower(-0.5f);
        }

        arm.basePos = arm.liftMotor.getCurrentPosition();
        arm.liftMotor.setPower(0.0f);




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
            if(r.length() >= Constants.TURN_CUTOFF){
                float mod = Constants.defaultXTurnSpeed
                        + ((Constants.maxXTurnSpeed-Constants.defaultXTurnSpeed)*this.gamepad1.left_trigger);
                drivePID.target = PIDFunctions.angleWrap(drivePID.target - (r.x * mod));
            }
            opModeTelemetry.addChild(new TelemetryData("Target Angle", drivePID.target));




            float PIDOut = PIDFunctions.updatePIDAngular(
                    drivePID,
                    nav.heading,
                    (float)nav.dt);
            opModeTelemetry.addChild(new TelemetryData("Angle", nav.heading));
            opModeTelemetry.addChild(new TelemetryData("PID power", PIDOut));





            //multiplier for drive speed.
            float mod = Constants.defaultXDriveSpeed
                    + ((1-Constants.defaultXDriveSpeed)*this.gamepad1.right_trigger);
            //float mod = 1.0f;
            telemetry.addChild("Percent", mod);
            V2f in = new V2f(
                    l.x * mod,
                    l.y * mod);
            in = in.rotated(-nav.heading);
            opModeTelemetry.addChild(new TelemetryData("Drive", in));


            DriveFunctions.setPower(
                    drive,
                    in,
                    PIDOut);




            {
                float lift = this.gamepad2.left_stick_y;
                if(arm.limitSwitch.isPressed()){
                    lift = (lift > 0)? 0:lift; //clamps from going lower than b
                    arm.basePos = arm.liftMotor.getCurrentPosition();
                }
                float liftSpeed = 2 * -lift;
                telemetry.addChild("Lift speed", liftSpeed);

                int pos = arm.liftMotor.getCurrentPosition();
                int armDifference = arm.liftMotor.getCurrentPosition() - arm.basePos;




                arm.liftMotor.setPower(liftSpeed);
                telemetry.addChild("Lift pos", pos);
                telemetry.addChild("Lift speed", liftSpeed);
                telemetry.addChild("Arm Base", arm.basePos);
                telemetry.addChild("Arm Difference", armDifference);
            }

            { // Servo
                arm.gripServo.setPosition(this.gamepad2.a ? 1 : 0.55f);
                telemetry.addChild("Servo pos", arm.gripServo.getPosition());
            }

            TelemetryFunctions.sendTelemetry(this.telemetry, telemetry);
        }

    }

}
