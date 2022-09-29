/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.Unused;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When a selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */





class v2f{
    public float x;
    public float y;



    public v2f() { }

    public v2f(float _x, float _y){
        this.x = _x;
        this.y = _y;
    }

    public float length(){
        return (float)java.lang.Math.sqrt(this.x*this.x + this.y*this.y); }

    public v2f normalized(){
        float l = this.length();
        return new v2f(this.x * l, this.y * l);

    }
}

class DriveData {
    public float FL = 0;
    public float FR = 0;
    public float BL = 0;
    public float BR = 0;

    public DriveData() { };
    public DriveData(float a, float b, float c, float d) {
        this.FL = a;
        this.FR = b;
        this.BL = c;
        this.BR = d;
    }
}









//NTS: @disabled is a criminal statement
@TeleOp(name="MechDiveBasic", group="Experimental")
public class LOPMD_DriveTesting extends LinearOpMode {


    private BNO055IMU imu = null;
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor BLDrive = null;
    private DcMotor BRDrive = null;
    private DcMotor FLDrive = null;
    private DcMotor FRDrive = null;

    @Override
    public void runOpMode() {


        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit            = BNO055IMU.AngleUnit.DEGREES;
        imu.initialize(parameters);


        FLDrive = hardwareMap.get(DcMotor.class, "front_left_drive");
        FRDrive = hardwareMap.get(DcMotor.class, "front_right_drive");
        BLDrive  = hardwareMap.get(DcMotor.class, "back_left_drive");
        BRDrive = hardwareMap.get(DcMotor.class, "back_right_drive");

        FLDrive.setDirection(DcMotor.Direction.FORWARD);
        FRDrive.setDirection(DcMotor.Direction.REVERSE);
        BLDrive.setDirection(DcMotor.Direction.FORWARD);
        BRDrive.setDirection(DcMotor.Direction.REVERSE);

        float targetHeading = 0;



        telemetry.addData("Status", "Initialized");
        telemetry.update();


        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            float LXIn = Range.clip(gamepad1.left_stick_x, -1.0f, 1.0f);
            float LYIn = Range.clip(gamepad1.left_stick_y, -1.0f, 1.0f);
            float RXIn = Range.clip(gamepad1.right_stick_x, -1.0f, 1.0f);
            float RYIn = Range.clip(gamepad1.right_stick_x, -1.0f, 1.0f);



            float driveMod = 0.6f;
            float strafeMod = 0.6f;
            float turnMod = 0.4f;
            float drive = LYIn * driveMod;
            float strafe = LXIn * strafeMod;
            float turn = RXIn * turnMod;


            double[] speeds = {
                    (drive + strafe - turn),
                    (drive - strafe + turn),
                    (drive - strafe - turn),
                    (drive + strafe + turn),
            };

            // Because we are adding vectors and motors only take values between
            // [-1,1] we may need to normalize them.

            // Loop through all values in the speeds[] array and find the greatest
            // *magnitude*.  Not the greatest velocity.
            double max = Math.abs(speeds[0]);
            for(int i = 0; i < speeds.length; i++) {
                if ( max < Math.abs(speeds[i]) ) max = Math.abs(speeds[i]);
            }

            // If and only if the maximum is outside of the range we want it to be,
            // normalize all the other speeds based on the given speed value.
            if (max > 1) {
                for (int i = 0; i < speeds.length; i++) speeds[i] /= max;
            }


            // apply the calculated values to the motors.
            FLDrive.setPower(speeds[0]);
            FRDrive.setPower(speeds[1]);
            BLDrive.setPower(speeds[2]);
            BRDrive.setPower(speeds[3]);

/*
            float fl = LYIn + LXIn + RXIn;
            float fr = LYIn - LXIn - RXIn;
            float bl = LYIn - LXIn + RXIn;
            float br = LYIn + LXIn - RXIn;




            float max = Math.abs(fl);
            if(Math.abs(fr) > max){
                max = Math.abs(fr); }
            if(Math.abs(bl) > max){
                max = Math.abs(bl); }
            if(Math.abs(br) > max){
                max = Math.abs(br); }

           if(max > 1.0){
               fl /= max;
               fr /= max;
               bl /= max;
               br /= max;
           }

            FLDrive.setPower(fl);
            FRDrive.setPower(fr);
            BLDrive.setPower(bl);
            BRDrive.setPower(br);
*/



            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Input Left", LXIn + ", " + LYIn);
            telemetry.addData("Input Right", RXIn + ", " + RYIn);

            Orientation heading   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            telemetry.addData("IMU", heading.firstAngle);


            sendMotorTelemetry("BL", BLDrive);
            sendMotorTelemetry("BR", BRDrive);
            sendMotorTelemetry("FL", FLDrive);
            sendMotorTelemetry("FR", FRDrive);



            telemetry.update();
        }
    }

    private void sendMotorTelemetry(String name, DcMotor m){
        telemetry.addData(name,
                  "Pwr:" + m.getPower() + ", Pos"
                        + m.getCurrentPosition() + ", Dir" + m.getDirection());


    }
}





