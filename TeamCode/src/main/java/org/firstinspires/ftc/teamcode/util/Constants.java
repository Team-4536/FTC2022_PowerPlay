package org.firstinspires.ftc.teamcode.util;

import org.firstinspires.ftc.teamcode.util.Data.ArmData;
import org.firstinspires.ftc.teamcode.util.Data.TelemetryData;

public abstract class Constants {


    public static final String TELEMETRY_INDENT = "        ";

    public static final float MM_PER_INCH      = 25.4f;
    public static final float IMG_HEIGHT_MM    = 6 * MM_PER_INCH;          // the height of the center of the target image above the floor
    public static final float HALF_FIELD_MM    = 72 * MM_PER_INCH;
    public static final float HALF_TILE_MM     = 12 * MM_PER_INCH;
    public static final float TILE_MM          = 36 * MM_PER_INCH;


    // These are for nav,
    // offset of the camera lens from the center of the bot
    // in MM
    final float CAMERA_FORWARD      = 0.0f * MM_PER_INCH;
    final float CAMERA              = 6.0f * MM_PER_INCH;
    final float CAMERA_LEFT         = 0.0f * MM_PER_INCH;



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


    public static final float TURN_CUTOFF = 0.5f;

    public static final float defaultXDriveSpeed = 0.25f;
    public static final float defaultXTurnSpeed = 1.0f;
    public static final float maxXTurnSpeed = 3.0f;


    public static final float mechStrafeMod = 0.9f;
    public static final float mechDriveMod = 0.6f;




    static final float zonesPwr = 0.23f;
    static final float hTime = 3.1f;
    static final float vTime = 4.0f;

    public static Step[][] PARKING_SEQUENCES = new Step[][]{

            new Step[] { },

            new Step[]{
                    new Step(new float[]{-zonesPwr, 0}, hTime),
                    new Step(new float[]{0, zonesPwr}, vTime)
            },

            new Step[]{
                    new Step(new float[]{0, zonesPwr}, vTime),
            },

            new Step[]{
                    new Step(new float[]{zonesPwr, 0}, hTime),
                    new Step(new float[]{0, zonesPwr}, vTime)
            }
    };


    public static final float SERVO_CLOSED = 0.0f;
    public static final float SERVO_OPEN =1.0f;

    public static void initArm(ArmData a, TelemetryData d){

        d.info = String.valueOf(a.gripServo.getPosition());
        a.gripServo.setPosition(SERVO_CLOSED);
        while(!a.limitSwitch.isPressed()){
            a.liftMotor.setPower(-0.5f);
        }

        a.basePos = a.liftMotor.getCurrentPosition();
        a.liftMotor.setPower(0.0f);
    }
}

