package org.firstinspires.ftc.teamcode.util;

public abstract class Constants {

    //constants for driving, plz ture
    public static final float strafeMod = 0.9f;
    public static final float driveMod = 0.8f;

    //in degs/sec
    public static final float turnRate = 3.0f;

    //when homing on angle target, this cont defines how far away the turn
    //target has to be to = 1.0 power to motor. A PID controller would be a better solution,
    //but I cant be bothered.
    public static final float MaxTurnTargetDist = 180;
    public static final float MaxTurnSpeed = 0.8f;


    public static final String telemetryIndent = "| ";
}
