package org.firstinspires.ftc.teamcode.functions;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.util.Data.NavData;

//class for figuring out the the bot is on the game board.
//using TF, & acc
public abstract class NavFunctions {

    public static void updateDt(NavData d) {

        d.dt = d.timer.seconds() - d.prevTime;
        d.prevTime = d.timer.seconds();
    }
    public static void updateHeading(NavData d) {

        //get the dist to angle target
        //note: z angle is flipped from normal!
        Orientation heading = d.imu.getAngularOrientation(
        AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        d.heading = heading.firstAngle;
    }
}

