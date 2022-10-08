package org.firstinspires.ftc.teamcode.util;

public class V3d {
    public double x;
    public double y;
    public double z;

    public V3d() { }
    public V3d(double _x, double _y, double _z)
    {
        this.x = _x;
        this.y = _y;
        this.z = _z;
    }


    public V3d add(V3d o){
        return new V3d(this.x + o.x, this.y + o.y, this.z + o.z);
    }

    public V3d multiply(double o){
        return new V3d(this.x * o, this.y * o, this.z * o);
    }
}
