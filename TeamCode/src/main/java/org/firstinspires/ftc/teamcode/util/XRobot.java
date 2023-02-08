package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.functions.NavFunctions;
import org.firstinspires.ftc.teamcode.functions.TelemetryFunctions;
import org.firstinspires.ftc.teamcode.util.Data.ArmData;
import org.firstinspires.ftc.teamcode.util.Data.AutoData;
import org.firstinspires.ftc.teamcode.util.Data.DriveData;
import org.firstinspires.ftc.teamcode.util.Data.NavData;
import org.firstinspires.ftc.teamcode.util.Data.PIDData;
import org.firstinspires.ftc.teamcode.util.Data.TelemetryData;

import java.util.List;

public abstract class XRobot {



    public static TelemetryData telemetry = new TelemetryData("State");
    public static NavData nav;
    public static DriveData drive;
    public static ArmData arm;
    public static PIDData drivePID;
    public static AutoData autoData;


    public static void init(HardwareMap hardwareMap, Telemetry t, boolean resetArm) {


        XRobot.nav = new NavData(hardwareMap);

        XRobot.drive = new DriveData(
                Constants.XFlipMap,
                hardwareMap);


        XRobot.arm = new ArmData(hardwareMap);
        if(resetArm) { Constants.initArm(arm); }

        // TUNE YOU DUMBASS
        XRobot.drivePID = new PIDData(0.015f, 0.0f, -0.2f);

        autoData = new AutoData();




        telemetry.addChild("Robot initialized", "");
        updateSystems(t);
    }

    public static void updateSystems(Telemetry t) {

        NavFunctions.updateDt(XRobot.nav);
        NavFunctions.updateHeading(XRobot.nav);



        XRobot.telemetry.addChild("Angle", XRobot.nav.heading);
        XRobot.telemetry.addChild("Target Angle", XRobot.drivePID.target);

        XRobot.telemetry.addChild("Lift pos", arm.liftMotor.getCurrentPosition());
        XRobot.telemetry.addChild("Arm Base", XRobot.arm.basePos);

        XRobot.telemetry.addChild("Auto stage: ", (autoData.index+1) + "/" + autoData.stages.size());

        TelemetryFunctions.sendTelemetry(t, telemetry);
    }



}
