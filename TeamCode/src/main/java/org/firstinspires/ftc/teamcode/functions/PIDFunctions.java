package org.firstinspires.ftc.teamcode.functions;

import org.firstinspires.ftc.teamcode.util.Data.PIDData;

public abstract class PIDFunctions {


    public static double updatePIDAngular(PIDData d, double pos, double dt){

        //all code yoinked from ctrl alt ftc thanks lol

        // calculate the error in degrees
        double error = PIDFunctions.angleWrap(d.target - pos);
        while(error > 180){ error -= 360; }

        // rate of change of the error
        double derivative = (error - d.prevErr) * dt;

        // sum of all error over time
        d.iSum += error * dt;

        double out = (d.Kp * error) + (d.Ki * d.iSum) + (d.Kd * derivative);

        d.prevErr = error;

        return out;
    }

    public static double updatePID(PIDData d, double pos, double dt){

        // calculate the error in degrees
        double error = d.target - pos;

        // rate of change of the error
        double derivative = (error - d.prevErr) * dt;

        // sum of all error over time
        d.iSum += error * dt;

        double out = (d.Kp * error) + (d.Ki * d.iSum) + (d.Kd * derivative);

        d.prevErr = error;

        return out;
    }


    //                                                                      V these degree symbols are cool
    // This function normalizes the angle so it returns a value between -180° and 180° instead of 0° to 360°.
    //YOINK https://www.ctrlaltftc.com/practical-examples/controlling-heading
    public static double angleWrap(double deg) {

        double modifiedAngle = deg % 360;
        return ((modifiedAngle + 360) % 360);
    }

}
