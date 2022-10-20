package org.firstinspires.ftc.teamcode.functions;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.util.Data.DynamicData;
import org.firstinspires.ftc.teamcode.util.Data.StaticData;

//class for figuring out the the bot is on the game board.
//using TF, & acc
public abstract class NavFunctions {

    public static void updateDt(StaticData s, DynamicData d) {

        d.dt = s.timer.seconds() - d.prevTime;
        d.prevTime = s.timer.seconds();
    }
    public static void updateHeading(StaticData s, DynamicData d) {

        //get the dist to angle target
        //note: z angle is flipped from normal!
        Orientation heading = s.imu.getAngularOrientation(
        AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        d.heading = heading.firstAngle;
    }
}

