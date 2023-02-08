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


    public V2f add(V2f s) {
        return new V2f(this.x + s.x, this.y + s.y); }

    public V2f divide(float s) {
        return new V2f(this.x / s, this.y / s); }

    public V2f multiply(float s) {
        return new V2f(this.x * s, this.y * s); }
    public float length(){
        return (float)Math.sqrt(this.x*this.x + this.y*this.y);
    }

    public V2f normalized() {
        double l = this.length();
        return l == 0? new V2f() : this.divide(this.length()); }
}
