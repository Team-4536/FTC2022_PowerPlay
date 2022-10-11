package org.firstinspires.ftc.teamcode.functions;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.teamcode.util.Hardware;
import org.firstinspires.ftc.teamcode.util.TelemetryData;
import org.firstinspires.ftc.teamcode.util.V3d;

//class for figuring out the the bot is on the game board.
//using TF, & acc
public abstract class NavFNS {

    public static void updateNav(Hardware h, TelemetryData telemetry, double dt,
                                 V3d va, V3d pa)
    {
        Acceleration acc = h.imu.getAcceleration();
        V3d a = new V3d(acc.xAccel, acc.yAccel, acc.zAccel);


        TelemetryData t = new TelemetryData("Nav");
        telemetry.addChild(t);

        t.addChild("Acc X", acc.xAccel);
        t.addChild("Acc Y", acc.yAccel);
        t.addChild("Acc Z", acc.zAccel);

        va = va.add(a.multiply(dt));
        t.addChild("Vel X", va.x);
        t.addChild("Vel Y", va.y);
        t.addChild("Vel Z", va.z);

        pa = pa.add(va.multiply(dt));
        t.addChild("Pos X", pa.x);
        t.addChild("Pos Y", pa.y);
        t.addChild("Pos Z", pa.z);
    }
}

