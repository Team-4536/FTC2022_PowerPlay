package org.firstinspires.ftc.teamcode.functions;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.util.Data.DriveData;
import org.firstinspires.ftc.teamcode.util.V2f;

//DriveFS has all of the functions to control the bots drive motors.
//drive motors should not be messed with outside of these functions.

public abstract class DriveFunctions {


   // m&r need to be between 0 and 1
   //sets the power of drive motors so that the bot will move by m and turn by r
   //NTS: axis from IMU is flipped, so its flipped here too. + turn = turn left
   public static void setPower(@NonNull DriveData d, @NonNull V2f m, float turn) {
      float drive = m.y;
      float strafe = m.x;


      float[] speeds = {
              (drive + strafe - turn),
              (drive - strafe + turn),
              (drive - strafe - turn),
              (drive + strafe + turn),
      };

      //thank you internet, https://github.com/brandon-gong/ftc-mecanum/blob/master/MecanumDrive.java
      // this normalizes speeds[] to between 0-1
      float max = Math.abs(speeds[0]);
      for (int i = 1; i < speeds.length; i++) {
         if (max < Math.abs(speeds[i])) {
            max = Math.abs(speeds[i]);
         }
      }

      //if the controller is only asking for a small amount, only move a small amount
      if (max > 1) {
         for (int i = 0; i < speeds.length; i++) {
            speeds[i] /= max; } }

      //set motor power
      d.FLDrive.setPower(speeds[0]);
      d.FRDrive.setPower(speeds[1]);
      d.BLDrive.setPower(speeds[2]);
      d.BRDrive.setPower(speeds[3]);
   }
}