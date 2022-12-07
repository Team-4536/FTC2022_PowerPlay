package org.firstinspires.ftc.teamcode.functions;

import org.firstinspires.ftc.teamcode.util.Data.ArmData;


public class InitArm {


    public static void arm_init(ArmData arm){
        arm.gripServo.setPosition(0.55f);
        while(!arm.limitSwitch.isPressed()){
            arm.liftMotor.setPower(-0.5f);
        }

        arm.basePos = arm.liftMotor.getCurrentPosition();
        arm.liftMotor.setPower(0.0f);

    }
}
