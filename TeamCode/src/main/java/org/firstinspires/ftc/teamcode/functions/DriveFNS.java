package org.firstinspires.ftc.teamcode.functions;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.util.Constants;
import org.firstinspires.ftc.teamcode.util.Hardware;
import org.firstinspires.ftc.teamcode.util.PIDData;
import org.firstinspires.ftc.teamcode.util.TelemetryData;
import org.firstinspires.ftc.teamcode.util.V2f;


//DriveFS has all of the functions to control the bots drive motors.
//drive motors should not be messed with outside of these functions.

public abstract class DriveFNS {

   //note: this is not normalized with time currently, see TODO
   public static void updateDrive(Hardware s, TelemetryData t, float dt,
                                  PIDData PID, @NonNull V2f l, @NonNull V2f dPad){

      TelemetryData driveTelemetry = new TelemetryData("Drive");
      t.addChild(driveTelemetry);

      //change target angle with input
      if(dPad.length() != 0){
         PID.target = PIDFNS.angleWrap(-dPad.getAngleDeg() - 90); }
      driveTelemetry.addChild(new TelemetryData("Target Angle", PID.target));


      //get the dist to angle target
      //note: z angle is flipped from normal!
      Orientation heading = s.imu.getAngularOrientation(
              AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
      float angle = heading.firstAngle;



      float PIDOut = PIDFNS.updatePIDAngular(PID, angle, dt);
      driveTelemetry.addChild(new TelemetryData("Angle", angle));
      driveTelemetry.addChild(new TelemetryData("PID power", PIDOut));





      //rotate drive input to be relative to start angle, not current angle
      V2f in = new V2f(l.x, l.y);
      in = in.rotated(angle);
      //multiply x and y axis to compensate for speed differences when strafing vs driving
      in.x *= Constants.strafeMod;
      in.y *= Constants.driveMod;
      driveTelemetry.addChild(new TelemetryData("Drive", in));




      //set motors to apply
      DriveFNS.setPowerMechanum(
              s,
              driveTelemetry,
              in,
              //FLIPPED Z
              -PIDOut);
   }






   // m&r need to be between 0 and 1
   //sets the power of drive motors so that the bot will move by m and turn by r
   public static void setPowerMechanum(Hardware s, TelemetryData t, @NonNull V2f m, float turn) {
      float drive = m.y;
      float strafe = m.x;


      float[] speeds = {
              (drive - strafe - turn),
              (drive + strafe + turn),
              (drive + strafe - turn),
              (drive - strafe + turn),
      };


      //thank you internet, https://github.com/brandon-gong/ftc-mecanum/blob/master/MecanumDrive.java
      //this normalizes speeds[] to between 0-1
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
      s.FLDrive.setPower(speeds[0]);
      s.FRDrive.setPower(speeds[1]);
      s.BLDrive.setPower(speeds[2]);
      s.BRDrive.setPower(speeds[3]);
   }




   public static void setPowerX(Hardware s, TelemetryData t, @NonNull V2f m, float turn) {
      float drive = m.y;
      float strafe = m.x;


      float[] speeds = {
              (drive - strafe - turn),
              (drive + strafe + turn),
              (drive + strafe - turn),
              (drive - strafe + turn),
      };


      //thank you internet, https://github.com/brandon-gong/ftc-mecanum/blob/master/MecanumDrive.java
      //this normalizes speeds[] to between 0-1
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
      s.FLDrive.setPower(speeds[0]);
      s.FRDrive.setPower(speeds[1]);
      s.BLDrive.setPower(speeds[2]);
      s.BRDrive.setPower(speeds[3]);
   }
}