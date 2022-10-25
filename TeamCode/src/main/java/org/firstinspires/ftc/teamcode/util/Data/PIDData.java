package org.firstinspires.ftc.teamcode.util.Data;

public class PIDData {

    public float Kp;
    public float Ki;
    public float Kd;

    public float target = 0;
    public float iSum = 0;
    public float prevErr = 0;




    public PIDData(float kp, float ki, float kd){
        this.Kp = kp;
        this.Ki = ki;
        this.Kd = kd;

    }


    public void reset() {
        this.target = 0;
        this.iSum = 0;
        this.prevErr = 0;
    }

}
