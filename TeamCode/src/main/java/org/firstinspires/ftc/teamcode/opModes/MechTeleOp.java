package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.functions.DriveFNS;
import org.firstinspires.ftc.teamcode.functions.TelemetryFNS;
import org.firstinspires.ftc.teamcode.functions.NavFunctions;
import org.firstinspires.ftc.teamcode.util.DynamicData;
import org.firstinspires.ftc.teamcode.util.PID.PIDFNS;
import org.firstinspires.ftc.teamcode.util.PID.PIDSettings;
import org.firstinspires.ftc.teamcode.util.StaticData;
import org.firstinspires.ftc.teamcode.util.TelemetryData;
import org.firstinspires.ftc.teamcode.util.V2f;
import org.firstinspires.ftc.teamcode.util.Constants;


@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="FTC 2022 Mechanum", group="TeleOps")
public class MechTeleOp extends LinearOpMode {

    @Override
    public void runOpMode() {
        StaticData s = new StaticData(
                this.hardwareMap,
                new PIDSettings(0.018f, 0.0f, -0.2f),
                new boolean[] { true, false, true, false } );

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
                d.drivePID.target = PIDFNS.angleWrap(-r.getAngleDeg() + 90); }
            opModeTelemetry.addChild(new TelemetryData("Target Angle", d.drivePID.target));




            float PIDOut = PIDFNS.updatePIDAngular(
                    s.drivePIDSettings,
                    d.drivePID,
                    d.heading,
                    (float)d.dt);
            opModeTelemetry.addChild(new TelemetryData("Angle", d.heading));
            opModeTelemetry.addChild(new TelemetryData("PID power", PIDOut));





            //rotate drive input to be relative to start angle, not current angle
            V2f in = new V2f(l.x, l.y);
            in = in.rotated(d.heading);
            //multiply x and y axis to compensate for speed differences when strafing vs driving
            in.x *= Constants.mechStrafeMod;
            in.y *= Constants.mechDriveMod;
            opModeTelemetry.addChild(new TelemetryData("Drive", in));

            DriveFNS.setPower(
                s,
                in,
                PIDOut);




            TelemetryFNS.sendTelemetry(this.telemetry, d.telemetryData);
        }

    }

}
