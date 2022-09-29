package org.firstinspires.ftc.teamcode.functions;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.util.TelemetryData;
import org.firstinspires.ftc.teamcode.util.BotState;

//Telemetry functions for sending back bot info.
//all of these only return a string (FORMATTING), that can be sent telemetry after
//ex. TelemetryFS.dcMotor() will return a string with all the info abt a motor
public abstract class TelemetryFS {



    public static TelemetryData botState(String name, BotState s) {

        TelemetryData r = new TelemetryData();
        r.title = name;


        r.addChild(new TelemetryData("Time", s.time.seconds()));
        r.addChild(new TelemetryData("target Angle", s.targetAngle));
        r.addChild(dcMotor("FL Drive:", s.FLDrive));
        r.addChild(dcMotor("FR Drive:", s.FRDrive));
        r.addChild(dcMotor("BL Drive:", s.BLDrive));
        r.addChild(dcMotor("BR Drive:", s.BRDrive));
        //NTS: add IMU telemetry plz




        return r;
    }

    public static TelemetryData dcMotor(String name, DcMotor m) {

        TelemetryData r = new TelemetryData(name);
        r.addChild(new TelemetryData("Power", m.getPower()));
        r.addChild(new TelemetryData("Position", m.getCurrentPosition()));
        r.addChild(new TelemetryData("Direction", (String.valueOf(m.getDirection()))));
        return r;
    }



}

