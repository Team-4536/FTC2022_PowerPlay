package org.firstinspires.ftc.teamcode.util.PID;

public class PIDData {

    public float target = 0;
    public float iSum = 0;
    public float prevErr = 0;

    public void reset() {
        this.target = 0;
        this.iSum = 0;
        this.prevErr = 0;
    }

}
