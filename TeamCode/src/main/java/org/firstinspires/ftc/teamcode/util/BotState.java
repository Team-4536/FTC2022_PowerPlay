package org.firstinspires.ftc.teamcode.util;


import androidx.annotation.NonNull;

import com.qualcomm.hardware.bosch.BNO055IMU;
//import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
//import com.qualcomm.hardware.bosch.NaiveAccelerationIntegrator;

public class BotState {


    //DRIVE _________________
    public DcMotor FLDrive;
    public DcMotor FRDrive;
    public DcMotor BLDrive;
    public DcMotor BRDrive;

    //maybe want to add target pos, if nav gets good enough
    public float targetAngle = 0;
    //_______________________





    //SENSING _______________
    public BNO055IMU imu;
    public ElapsedTime time;
    //_______________________

    //All bot motors and sensors should be kept in here


    public BotState(@NonNull HardwareMap map){



        this.time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

        this.imu = map.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.mode = BNO055IMU.SensorMode.ACCGYRO;
        //parameters.accelerationIntegrationAlgorithm = null;
        imu.initialize(parameters);






        this.FLDrive = map.get(DcMotor.class, "front_left_drive");
        this.FRDrive = map.get(DcMotor.class, "front_right_drive");
        this.BLDrive  = map.get(DcMotor.class, "back_left_drive");
        this.BRDrive = map.get(DcMotor.class, "back_right_drive");

        FLDrive.setDirection(DcMotor.Direction.FORWARD);
        FRDrive.setDirection(DcMotor.Direction.REVERSE);
        BLDrive.setDirection(DcMotor.Direction.FORWARD);
        BRDrive.setDirection(DcMotor.Direction.REVERSE);
    }
}
