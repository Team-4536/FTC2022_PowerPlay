package org.firstinspires.ftc.teamcode.opModes.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.robot.Robot;

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
import org.firstinspires.ftc.teamcode.util.XRobot;


@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="X teleOp abs", group="TeleOps")
public class XTeleOpAbsolute extends LinearOpMode{

    @Override
    public void runOpMode() {

        XRobot.init(this.hardwareMap, this.telemetry, false);

        waitForStart();

        XRobot.drivePID.target = XRobot.nav.heading;
        XRobot.updateSystems(this.telemetry);

        PIDData armPID = new PIDData(0.001f,0,0);

        while(opModeIsActive()){


            V2f l = new V2f(
                    this.gamepad1.left_stick_x,
                    -this.gamepad1.left_stick_y);
            V2f r = new V2f(
                    this.gamepad1.right_stick_x,
                    -this.gamepad1.right_stick_y);




            // angles
            float PIDOut = 0;
            {
                //change target angle with input
                if (r.length() >= Constants.TURN_CUTOFF) {
                    if (!gamepad1.left_bumper) {

                        float mod = Constants.defaultXTurnSpeed
                                + ((Constants.maxXTurnSpeed - Constants.defaultXTurnSpeed) * this.gamepad1.left_trigger);
                        XRobot.drivePID.target = PIDFunctions.angleWrap(XRobot.drivePID.target - (r.x * mod));
                    } else {

                        XRobot.drivePID.target = PIDFunctions.angleWrap(r.getAngleDeg() - 90);
                    }


                }
                PIDOut = PIDFunctions.updatePIDAngular(
                        XRobot.drivePID,
                        XRobot.nav.heading,
                        (float) XRobot.nav.dt);

                XRobot.telemetry.addChild("PID power", PIDOut);
            }

            // drive
            {

                V2f driveCmd = l;
                V2f dpad = new V2f(
                        (gamepad1.dpad_right?1:0) - (gamepad1.dpad_left?1:0),
                        (gamepad1.dpad_up?1:0) - (gamepad1.dpad_down?1:0)
                        );
                if(dpad.length() > 0) {
                    driveCmd = dpad;
                }

                //multiplier for drive speed.
                float mod = Constants.defaultXDriveSpeed
                        + ((1-Constants.defaultXDriveSpeed)*this.gamepad1.right_trigger);


                V2f in = new V2f( driveCmd.x * mod, driveCmd.y * mod);
                in = in.rotated(-XRobot.nav.heading);

                DriveFunctions.setPower(
                        XRobot.drive,
                        in,
                        PIDOut);
            }

            //lift
            {
                int armDifference = XRobot.arm.liftMotor.getCurrentPosition() - XRobot.arm.basePos;
                float lift = this.gamepad2.left_stick_y;


                // you need to tune lift speed and limits
                if(XRobot.arm.limitSwitch.isPressed()){
                    lift = (lift > 0)? 0:lift; //clamps from going lower than b
                    XRobot.arm.basePos = XRobot.arm.liftMotor.getCurrentPosition();
                }

                if(armDifference > 4600){
                    lift = (lift < 0)?0:lift;
                }

                // what the fuck
                float liftSpeed = 1600 * -lift;
                armPID.target += liftSpeed * XRobot.nav.dt;



                float out = PIDFunctions.updatePID(armPID, armDifference, (float)XRobot.nav.dt);
                XRobot.arm.liftMotor.setPower(out);////--------


                XRobot.telemetry.addChild("Arm target delta", liftSpeed);
                XRobot.telemetry.addChild("virtual arm height", armDifference);
                XRobot.telemetry.addChild("Arm PID Target", armPID.target);
                XRobot.telemetry.addChild("Arm base", XRobot.arm.basePos);

                XRobot.telemetry.addChild("PID output", out);
            }

            //servo
            {
                XRobot.arm.gripServo.setPosition(this.gamepad2.a ? Constants.SERVO_OPEN : Constants.SERVO_CLOSED);
                XRobot.telemetry.addChild("Servo pos", XRobot.arm.gripServo.getPosition());
            }

            XRobot.updateSystems(this.telemetry);
        }

    }

}
