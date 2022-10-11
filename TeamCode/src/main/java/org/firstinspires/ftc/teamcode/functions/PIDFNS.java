package org.firstinspires.ftc.teamcode.functions;

import org.firstinspires.ftc.teamcode.util.PIDData;

public abstract class PIDFNS {

    /*public static float updatePID(PIDData d, float pos, float dt){

        //all code yoinked from ctrl alt ftc thanks lol

        // calculate the error
        float error = d.target - pos;

        // rate of change of the error
        float derivative = (error - d.prevErr) / dt;

        // sum of all error over time
        d.iSum += error * dt;

        float out = (d.Kp * error) + (d.Ki * d.iSum) + (d.Kd * derivative);

        d.prevErr = error;

        return out;
    }*/



    public static float updatePIDAngular(PIDData d, float pos, float dt){

        //all code yoinked from ctrl alt ftc thanks lol

        // calculate the error in degrees
        float error = PIDFNS.angleWrap(d.target - pos);
        while(error > 180){ error -= 360; }

        // rate of change of the error
        float derivative = (error - d.prevErr) * dt;

        // sum of all error over time
        d.iSum += error * dt;

        float out = (d.Kp * error) + (d.Ki * d.iSum) + (d.Kd * derivative);

        d.prevErr = error;

        return out;
    }






    // This function normalizes the angle so it returns a value between -180° and 180° instead of 0° to 360°.
    //YOINK https://www.ctrlaltftc.com/practical-examples/controlling-heading
    public static float angleWrap(float deg) {

        float modifiedAngle = deg % 360;
        return ((modifiedAngle + 360) % 360);
    }

}
