package org.firstinspires.ftc.teamcode.functions;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.Data.TelemetryData;

//Telemetry functions for sending back bot info.
//all of these only return a string (FORMATTING), that can be sent telemetry after
//ex. TelemetryFS.dcMotor() will return a string with all the info abt a motor
public abstract class TelemetryFunctions {

    public static void sendTelemetry(Telemetry t, TelemetryData m){
        t.addLine(m.getString());
        t.update();
        m.clear();
    }

}

