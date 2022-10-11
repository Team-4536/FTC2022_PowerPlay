package org.firstinspires.ftc.teamcode.util;

public class PIDData {
    public float Kp;
    public float Ki;
    public float Kd;


    public float target;
    public float iSum;
    public float prevErr;

    public void reset() {
        this.target = 0;
        this.iSum = 0;
    }
}
