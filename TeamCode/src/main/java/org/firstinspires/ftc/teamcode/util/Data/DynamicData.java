package org.firstinspires.ftc.teamcode.util.Data;

import org.firstinspires.ftc.teamcode.util.PID.PIDData;
import org.firstinspires.ftc.teamcode.util.TelemetryData;

public class DynamicData {

    public TelemetryData telemetryData = new TelemetryData();
    public float heading = 0;
    public double prevTime = 0;
    public double dt = 0;



    public PIDData drivePID = new PIDData();
}
