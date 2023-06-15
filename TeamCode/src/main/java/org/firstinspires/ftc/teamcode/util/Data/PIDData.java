package org.firstinspires.ftc.teamcode.util.Data;

public class PIDData {

    public double Kp;
    public double Ki;
    public double Kd;

    public double target = 0;
    public double iSum = 0;
    public double prevErr = 0;




    public PIDData(double kp, double ki, double kd){
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
