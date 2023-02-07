package org.firstinspires.ftc.teamcode.util.Data;
import java.lang.Math;

public class EncoderOdometry {
    int startingRotationBL;
    int startingRotationBR;
    int startingRotationFL;
    int startingRotationFR;

    double mmPerTick = 0; //-------

    public EncoderOdometry(DriveData drive) {
        EncoderReset(drive);
    }

    public void EncoderReset(DriveData drive){
        this.startingRotationBL = drive.BLDrive.getCurrentPosition();
        this.startingRotationBR = drive.BRDrive.getCurrentPosition();
        this.startingRotationFL = drive.FLDrive.getCurrentPosition();
        this.startingRotationFR = drive.FRDrive.getCurrentPosition();
    }

    public int[] findDifference(DriveData drive){
        int[] differences = {
                Math.abs(startingRotationBL - drive.BLDrive.getCurrentPosition()),
                Math.abs(startingRotationBR - drive.BRDrive.getCurrentPosition()),
                        Math.abs(startingRotationFL - drive.FLDrive.getCurrentPosition()),
                        Math.abs(startingRotationFR - drive.FRDrive.getCurrentPosition())};
        return differences;
    }

    public double findDistance(DriveData drive){
        int[] differences = findDifference(drive);
        double differencesAve = 0;
        double distance; //millimeters

        for(int i = 0; i < differences.length; i++){
            differencesAve += differences[i];
        }
        differencesAve /= differences.length;

        distance = differencesAve * mmPerTick;
        return distance;

    }
}
