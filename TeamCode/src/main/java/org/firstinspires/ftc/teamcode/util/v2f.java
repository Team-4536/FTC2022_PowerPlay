package org.firstinspires.ftc.teamcode.util;

import androidx.annotation.NonNull;


public class v2f {
    @NonNull
    public float x = 0;
    @NonNull
    public float y = 0;

    public v2f() { }
    public v2f(@NonNull float _x, @NonNull float _y) {
        this.x = _x;
        this.y = _y;
    }


    public String toString(){
        return this.x + ", " + this.y;
    }


    public v2f rotated(float deg){
        float degInRad = (float)Math.toRadians(deg);
        float sin = (float)Math.sin(degInRad);
        float cos = (float)Math.cos(degInRad);

        v2f f = new v2f(
                this.x * cos - this.y * sin,
                this.y * cos + this.x * sin);

        return f;
    }
}
