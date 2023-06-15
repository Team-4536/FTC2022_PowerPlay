package org.firstinspires.ftc.teamcode.util;

public class V2f {
    public double x = 0;
    public double y = 0;

    public V2f() { }
    public V2f(double _x, double _y) {
        this.x = _x;
        this.y = _y;
    }


    public String toString(){
        return this.x + ", " + this.y;
    }


    public V2f rotated(double deg){
        double degInRad = Math.toRadians(deg);
        double sin = Math.sin(degInRad);
        double cos = Math.cos(degInRad);

        return new V2f(
                this.x * cos - this.y * sin,
                this.y * cos + this.x * sin);
    }

    public double getAngleDeg(){
        return Math.toDegrees(Math.atan2(this.y, this.x));
    }


    public V2f add(V2f s) {
        return new V2f(this.x + s.x, this.y + s.y); }

    public V2f divide(double s) {
        return new V2f(this.x / s, this.y / s); }

    public V2f multiply(double s) {
        return new V2f(this.x * s, this.y * s); }
    public double length(){
        return Math.sqrt(this.x*this.x + this.y*this.y);
    }

    public V2f normalized() {
        double l = this.length();
        return l == 0? new V2f() : this.divide(this.length()); }
}
