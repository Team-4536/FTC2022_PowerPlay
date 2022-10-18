package org.firstinspires.ftc.teamcode.util;

public class Step {

    public float[] data = new float[0];
    public float duration;

    public Step() {};


    public Step(float[] _data, float _duration){
        this.data = _data;
        this.duration = _duration;
    }
}
