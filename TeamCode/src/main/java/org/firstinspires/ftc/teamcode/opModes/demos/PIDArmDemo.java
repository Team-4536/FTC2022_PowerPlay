package org.firstinspires.ftc.teamcode.opModes.demos;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.functions.NavFunctions;
import org.firstinspires.ftc.teamcode.functions.PIDFunctions;
import org.firstinspires.ftc.teamcode.functions.TelemetryFunctions;
import org.firstinspires.ftc.teamcode.util.Constants;
import org.firstinspires.ftc.teamcode.util.Data.ArmData;
import org.firstinspires.ftc.teamcode.util.Data.DriveData;
import org.firstinspires.ftc.teamcode.util.Data.NavData;
import org.firstinspires.ftc.teamcode.util.Data.PIDData;
import org.firstinspires.ftc.teamcode.util.Data.TelemetryData;


@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Arm PID Demo", group="TeleOps")
public class PIDArmDemo extends LinearOpMode {

    @Override
    public void runOpMode() {

        TelemetryData telemetry = new TelemetryData("State");
        NavData nav = new NavData(this.hardwareMap);
        PIDData armPID = new PIDData(0.01f, 0.0f, 0.0f);
        @NonNull DcMotor liftMotor = this.hardwareMap.get(DcMotor.class, "lift_motor");


        // Constants.initArm(arm, c);
        TelemetryFunctions.sendTelemetry(this.telemetry, telemetry);


        waitForStart();
        while (opModeIsActive()) {

            NavFunctions.updateDt(nav);
            armPID.target += this.gamepad1.left_stick_y * 10 * nav.dt;

            float pidOut = PIDFunctions.updatePID(armPID, liftMotor.getCurrentPosition(), (float)nav.dt);
            liftMotor.setPower(pidOut);
            telemetry.addChild("Arm encoder", liftMotor.getCurrentPosition());
            telemetry.addChild("Target pos", armPID.target);
            telemetry.addChild("pidOut", pidOut);

        }
    }

}
