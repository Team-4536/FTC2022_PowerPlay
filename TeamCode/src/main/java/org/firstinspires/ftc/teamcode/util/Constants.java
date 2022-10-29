package org.firstinspires.ftc.teamcode.util;

public abstract class Constants {

    //constants for driving, plz tune
    public static final float mechStrafeMod = 0.9f;
    public static final float mechDriveMod = 0.6f;


    public static final String telemetryIndent = "        \t";




    public static final int ticksPerLiftRevolution = 1440;
    public static final float defaultXDriveSpeed = 0.25f;

    public static final String VUFORIA_KEY =
            "ARtx29j/////AAABmc8Mmozqr0GsgPsH/NOLUkBPcj0u8eyHkeiAyftu6QAtlUKWRkZ5UPmI6mCAXfVgmJHq7dPpxL1kd5rCK4YO8ABWIbqITG6W6Kp8LqKWJnRWOVIBoOxtZsSkfvOu39s2NKVKbmNshfLUTkNS8Xs6NLOJS2mAHITqOPgR+68DhqEFQIvT1HRBplwsEeuWFVeiizCgmp3sBHZMWdNiCN0KS1AZLbF/+352OGpIkhFGPuMYSb7VdjLVUPRc3Pl7qoOISjVpIweyRMIrc/jkj3bEBULFbKjPyBa+sAwXnxiBxoFDVB7Fo2S5NsU6pW1w67wqYyHM8R1/JLEQJqWTFf+hrnLnPN+aew4/+C3q1XP14fBg";


    public static final Model STANDARD_SLEEVE_MODEL = new Model(
            "PowerPlay.tflite",
            new String[] {
                    "1 Bulb",
                    "2 Bulb",
                    "3 Panel"
            }
    );
    public static final Model CUSTOM_SLEEVE_MODEL = new Model(
            "custumModel.tflite",
            new String[] {
                    "BlueLines",
                    "GreenLines",
                    "redDots"
            }
    );
}

