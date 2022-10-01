package org.firstinspires.ftc.teamcode.functions;

import org.firstinspires.ftc.teamcode.util.PIDData;

public abstract class PIDFs {

    public static float updatePID(PIDData d, float pos, float dt){

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
    }



    public static float updatePIDAngular(PIDData d, float pos, float dt){

        //all code yoinked from ctrl alt ftc thanks lol

        // calculate the error in degrees
        float error = DriveFS.angleWrap(d.target - pos);
        while(error > 180){ error -= 360; }

        // rate of change of the error
        float derivative = (error - d.prevErr) * dt;

        // sum of all error over time
        d.iSum += error * dt;

        float out = (d.Kp * error) + (d.Ki * d.iSum) + (d.Kd * derivative);

        d.prevErr = error;

        return out;
    }

}
