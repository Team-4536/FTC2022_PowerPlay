package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.dashboard.config.Config;
import com.sun.tools.javac.util.List;

import org.firstinspires.ftc.teamcode.util.Data.ArmData;
import org.firstinspires.ftc.teamcode.util.Data.TelemetryData;

import java.util.ArrayList;



@Config
public class Constants {

    public static final String TELEMETRY_INDENT = "        ";
    //public static float MM_PER_INCH = 25.4f;
    public static double MM_PER_INCH = 25.4;
    public static double IMG_HEIGHT_MM = 6 * MM_PER_INCH;          // the height of the center of the target image above the floor
    public static  double HALF_FIELD_MM    = 72 * MM_PER_INCH;
    public static  double HALF_TILE_MM     = 12 * MM_PER_INCH;
    public static  double TILE_MM          = 36 * MM_PER_INCH;

    // These are for nav,
    // offset of the camera lens from the center of the bot
    // in MM
    // all bullshit btw
    final double CAMERA_FORWARD      = 0.0 * MM_PER_INCH;
    final double CAMERA              = 6.0 * MM_PER_INCH;
    final double CAMERA_LEFT         = 0.0 * MM_PER_INCH;



    public static final String VUFORIA_KEY =
            "ARtx29j/////AAABmc8Mmozqr0GsgPsH/NOLUkBPcj0u8eyHkeiAyftu6QAtlUKWRkZ5UPmI6mCAXfVgmJHq7dPpxL1kd5rCK4YO8ABWIbqITG6W6Kp8LqKWJnRWOVIBoOxtZsSkfvOu39s2NKVKbmNshfLUTkNS8Xs6NLOJS2mAHITqOPgR+68DhqEFQIvT1HRBplwsEeuWFVeiizCgmp3sBHZMWdNiCN0KS1AZLbF/+352OGpIkhFGPuMYSb7VdjLVUPRc3Pl7qoOISjVpIweyRMIrc/jkj3bEBULFbKjPyBa+sAwXnxiBxoFDVB7Fo2S5NsU6pW1w67wqYyHM8R1/JLEQJqWTFf+hrnLnPN+aew4/+C3q1XP14fBg";


    public static final Model STANDARD_SLEEVE_MODEL = new Model(
            "PowerPlay.tflite",
            new String[]{
                    "1",
                    "2",
                    "3"
            }
    );
    public static final Model CUSTOM_SLEEVE_MODEL = new Model(
            "custumModel.tflite",
            new String[]{

                    "GreenLines",
                    "redDots"
            }
    );










    public static double TURN_CUTOFF = 0.5;

    public static double defaultXDriveSpeed = 0.25;
    public static double defaultXTurnSpeed = 1.0;
    public static double maxXTurnSpeed = 3.0;

    public static final boolean[] XFlipMap = {
            true, false, true, false
    };


    public static double SERVO_CLOSED = 0.55;
    public static double SERVO_OPEN =1.0;

    public static void initArm(ArmData a){

        a.gripServo.setPosition(SERVO_CLOSED);
        while(!a.limitSwitch.isPressed()){
            a.liftMotor.setPower(0.5);
        }

        a.basePos = a.liftMotor.getCurrentPosition();
        a.liftMotor.setPower(0.0);
    }










    public static  double mechStrafeMod = 0.9;
    public static  double mechDriveMod = 0.0;






    static final double zonesPwr = 0.23f;
    static final double hTime = 3.5f;
    static final double vTime = 5.0f;
    static final double outTime = 2.0f;

    public static List<List<Stage>> parkingRoutines = List.of(


            List.of(
                new Stage.MoveTimed(new V2f(0, zonesPwr), outTime),
                new Stage.MoveTimed(new V2f(-zonesPwr, 0), hTime),
                new Stage.MoveTimed(new V2f(0, zonesPwr), vTime),
                    new Stage.MoveTimed(new V2f(0, 0), 99999)
            ),

            List.of(
                    new Stage.MoveTimed(new V2f(0, zonesPwr), outTime),
                    new Stage.MoveTimed(new V2f(0, zonesPwr), vTime),
                    new Stage.MoveTimed(new V2f(0, 0), 99999)
            ),

            List.of(
                    new Stage.MoveTimed(new V2f(0, zonesPwr), outTime),
                    new Stage.MoveTimed(new V2f(zonesPwr, 0), hTime),
                    new Stage.MoveTimed(new V2f(0, zonesPwr), vTime),
                    new Stage.MoveTimed(new V2f(0, 0), 99999)
            )
    );




}

