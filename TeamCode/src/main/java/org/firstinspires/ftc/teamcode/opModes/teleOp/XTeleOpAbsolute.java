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

            int baseArmPos = arm.liftMotor.getCurrentPosition();;


            TelemetryData opModeTelemetry = new TelemetryData("OpMode");
            telemetry.addChild(opModeTelemetry);


            //change target angle with input
            if(r.length() >= Constants.TURN_CUTOFF){
                drivePID.target = PIDFunctions.angleWrap(drivePID.target - r.x); }
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
            in = new V2f(
                    in.x * mod,
                    in.y * mod);
            in = in.rotated(-nav.heading);
            opModeTelemetry.addChild(new TelemetryData("Drive", in));


            DriveFunctions.setPower(
                    drive,
                    in,
                    PIDOut);

            //multiplier for drive speed.
            float mod = Constants.defaultXDriveSpeed
                    + ((1-Constants.defaultXDriveSpeed)*this.gamepad1.right_trigger);
            //float mod = 1.0f;
            telemetry.addChild("Percent", mod);
            in = new V2f(
                    in.x * mod,
                    in.y * mod);
            in = in.rotated(-nav.heading);
            opModeTelemetry.addChild(new TelemetryData("Drive", in));



            float lift = this.gamepad2.left_stick_y;
            if(arm.limitSwitch.isPressed()){
                lift = (lift > 0)? 0:lift; //clamps from going lower than b
                baseArmPos = arm.liftMotor.getCurrentPosition();
            }
            float liftSpeed = 2 * -lift;
            telemetry.addChild("Lift speed", liftSpeed);

            int pos = arm.liftMotor.getCurrentPosition();
            int armDiference = arm.liftMotor.getCurrentPosition() - baseArmPos;


            arm.liftMotor.setPower(liftSpeed);


            telemetry.addChild("Lift pos", pos);
            telemetry.addChild("Lift speed", liftSpeed);
            telemetry.addChild("Arm Base", baseArmPos);
            telemetry.addChild("Arm Diference", armDiference);
            float servoPosition = this.gamepad2.a?1:0.55f;


            float servoPosition = this.gamepad1.right_bumper?1:0 - (this.gamepad1.left_bumper?1:0);
            arm.gripServo.setPosition(servoPosition);
            telemetry.addChild("Servo pos", arm.gripServo.getPosition());

            //amongus


            TelemetryFunctions.sendTelemetry(this.telemetry, telemetry);
        }

    }

}
