package org.firstinspires.ftc.teamcode.functions;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.util.Constants;
import org.firstinspires.ftc.teamcode.util.Fptr;
import org.firstinspires.ftc.teamcode.util.Hardware;
import org.firstinspires.ftc.teamcode.util.TelemetryData;
import org.firstinspires.ftc.teamcode.util.V2f;


//DriveFS has all of the functions to control the bots drive motors.
//drive motors should not be messed with outside of these functions.


public abstract class DriveFS {

   //note: this is not normalized with time currently, see TODO
   public static void updateDrive(Hardware s, TelemetryData t,
                                  Fptr targetAngle, @NonNull V2f l, @NonNull V2f r){

      TelemetryData driveTelemetry = new TelemetryData("Drive");
      t.addChild(driveTelemetry);

      //change target angle with input
      targetAngle.v = (float)(targetAngle.v + r.x * Constants.turnRate);
      driveTelemetry.addChild(new TelemetryData("Target Angle", targetAngle.v));


      //get the dist to angle target
      //s.imu.getAngularOrientation().axesOrder;
      Orientation heading = s.imu.getAngularOrientation(
              AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
      //axis returns angle flipped
      float angle = -heading.firstAngle;
      float angleDiff = DriveFS.angleWrap(angle - targetAngle.v);
      if(angleDiff > 180){ angleDiff -= 360; }
      driveTelemetry.addChild(new TelemetryData("Angle", angle));
      driveTelemetry.addChild(new TelemetryData("Angle diff", angleDiff));





      //rotate drive input to be relative to start angle, not current angle
      V2f in = new V2f(
              l.x * Constants.strafeMod,
              l.y * Constants.driveMod);
      in = in.rotated(-angle);
      driveTelemetry.addChild(new TelemetryData("Drive", in));





      //set turning to move towards target, clamped by MAX
      float nr = angleDiff;
      if(Math.abs(nr) > Constants.MaxTurnTargetDist){
         nr = Math.signum(angleDiff) * Constants.MaxTurnTargetDist; }
      driveTelemetry.addChild(new TelemetryData("Turning", nr/Constants.MaxTurnTargetDist));
      //get angle as a percent of max
      nr /= Constants.MaxTurnTargetDist;
      //clamp speed to max
      if(nr > Constants.MaxTurnSpeed) {
         nr = Constants.MaxTurnSpeed; }

      //set motors to apply
      DriveFS.setPower(
              s,
              in,
              //also check out PID controllers/smoothing fns
              nr);
   }







   // m&r need to be between 0 and 1
   //sets the power of drive motors so that the bot will move by m and turn by r
   public static void setPower(Hardware s, @NonNull V2f m, float turn) {
      float drive = m.y;
      float strafe = m.x;


      float[] speeds = {
              (drive + strafe - turn),
              (drive - strafe + turn),
              (drive - strafe - turn),
              (drive + strafe + turn),
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










   // This function normalizes the angle so it returns a value between -180° and 180° instead of 0° to 360°.
   //YOINK https://www.ctrlaltftc.com/practical-examples/controlling-heading
   public static float angleWrap(float deg) {

      float modifiedAngle = deg % 360;
      return ((modifiedAngle + 360) % 360);
   }
}