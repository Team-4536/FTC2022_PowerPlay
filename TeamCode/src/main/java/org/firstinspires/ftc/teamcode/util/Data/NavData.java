package org.firstinspires.ftc.teamcode.util.Data;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class NavData {


    public final @NonNull
    BNO055IMU imu;
    public final @NonNull
    ElapsedTime timer;



    public float heading = 0;
    public double prevTime = 0;
    public double dt = 0;


    public NavData(HardwareMap map){

        this.timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);


        this.imu = map.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imu.initialize(parameters);

    }
}
