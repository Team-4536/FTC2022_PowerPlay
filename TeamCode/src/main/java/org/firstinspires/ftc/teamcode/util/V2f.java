package org.firstinspires.ftc.teamcode.util;


public class V2f {
    public float x = 0;
    public float y = 0;

    public V2f() { }
    public V2f(float _x, float _y) {
        this.x = _x;
        this.y = _y;
    }


    public String toString(){
        return this.x + ", " + this.y;
    }


    public V2f rotated(float deg){
        float degInRad = (float)Math.toRadians(deg);
        float sin = (float)Math.sin(degInRad);
        float cos = (float)Math.cos(degInRad);

        return new V2f(
                this.x * cos - this.y * sin,
                this.y * cos + this.x * sin);
    }

    public float getAngleDeg(){
        return (float)Math.toDegrees(Math.atan2(this.y, this.x));
    }


    public float length(){
        return (float)Math.sqrt(this.x*this.x + this.y*this.y);
    }
}
