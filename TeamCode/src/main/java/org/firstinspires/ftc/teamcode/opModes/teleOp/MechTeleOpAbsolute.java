package org.firstinspires.ftc.teamcode.opModes.teleOp;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.functions.DriveFunctions;
import org.firstinspires.ftc.teamcode.functions.TelemetryFunctions;
import org.firstinspires.ftc.teamcode.functions.NavFunctions;
import org.firstinspires.ftc.teamcode.functions.PIDFunctions;
import org.firstinspires.ftc.teamcode.util.Data.ArmData;
import org.firstinspires.ftc.teamcode.util.Data.DriveData;
import org.firstinspires.ftc.teamcode.util.Data.NavData;
import org.firstinspires.ftc.teamcode.util.Data.PIDData;
import org.firstinspires.ftc.teamcode.util.Data.TelemetryData;
import org.firstinspires.ftc.teamcode.util.V2f;
import org.firstinspires.ftc.teamcode.util.Constants;


@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="MechTeleOp Abs", group="TeleOps")
public class MechTeleOpAbsolute extends LinearOpMode {

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




            //change target angle with input
            if(r.length() >= 0.1f){
                drivePID.target = PIDFunctions.angleWrap(+r.getAngleDeg() - 90); }
            telemetry.addChild(new TelemetryData("Target Angle", drivePID.target));









            //get angle correction
            float PIDOut = PIDFunctions.updatePIDAngular(
                    drivePID,
                    nav.heading,
                    (float)nav.dt);

            //rotate drive input to be relative to start angle, not current angle
            V2f in = new V2f(l.x, l.y);
            in = in.rotated(-nav.heading);
            //multiply x and y axis to compensate for speed differences when strafing vs driving
            in.x *= Constants.mechStrafeMod;
            in.y *= Constants.mechDriveMod;
            telemetry.addChild(new TelemetryData("Drive", in));
            telemetry.addChild(new TelemetryData("Angle", nav.heading));
            telemetry.addChild(new TelemetryData("PID power", PIDOut));

            DriveFunctions.setPower(
                drive,
                in,
                PIDOut);










            TelemetryFunctions.sendTelemetry(this.telemetry, telemetry);
        }

    }

}
