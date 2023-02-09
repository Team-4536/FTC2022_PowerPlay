package org.firstinspires.ftc.teamcode.opModes.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Disabled
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="NA TeleOp Lift test", group="TeleOps")
public class NATeleOpLiftTest extends LinearOpMode {


    @Override
    public void runOpMode() {

        DcMotor m = this.hardwareMap.get(DcMotor.class, "lift_motor");

        waitForStart();
        while(opModeIsActive()) {
            m.setPower(this.gamepad1.left_stick_y);

            if(m == null){
                this.telemetry.addLine("Motor null!");
            }

            this.telemetry.update();
        }
    }
}


