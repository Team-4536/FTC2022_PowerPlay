package org.firstinspires.ftc.teamcode.util.Data;

import android.widget.Switch;

import androidx.annotation.NonNull;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class ArmData {

    public final @NonNull DcMotor liftMotor;
    public final @NonNull Servo gripServo;
    public final @NonNull TouchSensor limitSwitch;


    public ArmData(HardwareMap map) {
        this.liftMotor = map.get(DcMotor.class, "lift_motor");
        this.liftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        this.liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.gripServo = map.get(Servo.class, "grip_servo");
        this.limitSwitch = map.touchSensor.get("limit_switch");
    }

}
