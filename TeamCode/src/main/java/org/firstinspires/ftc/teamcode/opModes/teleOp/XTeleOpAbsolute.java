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
                Constants.XFlipMap,
                this.hardwareMap);
        PIDData drivePID = new PIDData(0.015f, 0.0f, -0.2f);

        ArmData arm = new ArmData(this.hardwareMap);



        TelemetryData c = new TelemetryData();
        telemetry.addChild(c);
        c.title = "yg;uirguhlsrtguh;sgbrhj;sreguh;srge;ioj";
        Constants.initArm(arm, c);
        TelemetryFunctions.sendTelemetry(this.telemetry, telemetry);



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
                int pos = arm.liftMotor.getCurrentPosition();
                int armDifference = arm.liftMotor.getCurrentPosition() - arm.basePos;


                float lift = this.gamepad2.left_stick_y;


                if(arm.limitSwitch.isPressed()){
                    lift = (lift > 0)? 0:lift; //clamps from going lower than b
                    arm.basePos = arm.liftMotor.getCurrentPosition();
                }

                if(armDifference > 12000){
                    lift = (lift < 0)?0:lift;
                }

                float liftSpeed = 2 * -lift;
                telemetry.addChild("Lift speed", liftSpeed);





                arm.liftMotor.setPower(liftSpeed);
                telemetry.addChild("Lift pos", pos);
                telemetry.addChild("Lift speed", liftSpeed);
                telemetry.addChild("Arm Base", arm.basePos);
                telemetry.addChild("Arm Difference", armDifference);
            }

            { // Servo
                arm.gripServo.setPosition(this.gamepad2.a ? Constants.SERVO_OPEN : Constants.SERVO_CLOSED);
                telemetry.addChild("Servo pos", arm.gripServo.getPosition());
            }

            TelemetryFunctions.sendTelemetry(this.telemetry, telemetry);
        }

    }

}
