package org.firstinspires.ftc.teamcode.util.Data;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.PID.PIDSettings;
import org.firstinspires.ftc.teamcode.util.Step;

public class StaticData {

    //DRIVE _________________
    public @NonNull DcMotor FLDrive;
    public @NonNull DcMotor FRDrive;
    public @NonNull DcMotor BLDrive;
    public @NonNull DcMotor BRDrive;

    public @NonNull
    PIDSettings drivePIDSettings;
    //_______________________

    //NAV_______________
    public @NonNull BNO055IMU imu;
    public @NonNull ElapsedTime timer;

    public Step[] sequencerSteps;
    //_______________________





    public StaticData(@NonNull HardwareMap map,
                      @NonNull PIDSettings _drivePID,
                      boolean[] driveFlipMap){



        //NAV______________________________________________
        this.timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);


        this.imu = map.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imu.initialize(parameters);
        //_________________________________________________





        //DRIVE________________________________________________________________
        this.FLDrive = map.get(DcMotor.class, "front_left_drive");
        FLDrive.setDirection(pickMotorDir(driveFlipMap[0]));

        this.FRDrive = map.get(DcMotor.class, "front_right_drive");
        FRDrive.setDirection(pickMotorDir(driveFlipMap[1]));

        this.BLDrive  = map.get(DcMotor.class, "back_left_drive");
        BLDrive.setDirection(pickMotorDir(driveFlipMap[2]));

        this.BRDrive = map.get(DcMotor.class, "back_right_drive");
        BRDrive.setDirection(pickMotorDir(driveFlipMap[3]));

        this.drivePIDSettings = _drivePID;
        //_____________________________________________________________________


    }


    private DcMotorSimple.Direction pickMotorDir(boolean a){
        return a?DcMotorSimple.Direction.FORWARD:DcMotorSimple.Direction.REVERSE;
    }


}
