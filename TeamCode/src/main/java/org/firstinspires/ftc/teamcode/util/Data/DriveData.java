package org.firstinspires.ftc.teamcode.util.Data;


import androidx.annotation.NonNull;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;




public class DriveData {

    public final @NonNull DcMotor FLDrive;
    public final @NonNull DcMotor FRDrive;
    public final @NonNull DcMotor BLDrive;
    public final @NonNull DcMotor BRDrive;



    public DriveData(boolean[] flipMap, HardwareMap map) {

        this.FLDrive = map.get(DcMotor.class, "front_left_drive");
        FLDrive.setDirection(pickMotorDir(flipMap[0]));

        this.FRDrive = map.get(DcMotor.class, "front_right_drive");
        FRDrive.setDirection(pickMotorDir(flipMap[1]));

        this.BLDrive  = map.get(DcMotor.class, "back_left_drive");
        BLDrive.setDirection(pickMotorDir(flipMap[2]));

        this.BRDrive = map.get(DcMotor.class, "back_right_drive");
        BRDrive.setDirection(pickMotorDir(flipMap[3]));
    }

    private DcMotorSimple.Direction pickMotorDir(boolean a){
        return a?DcMotorSimple.Direction.FORWARD:DcMotorSimple.Direction.REVERSE;
    }



}
