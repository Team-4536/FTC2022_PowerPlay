package org.firstinspires.ftc.teamcode.functions;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.util.BotState;
import org.firstinspires.ftc.teamcode.util.constants;
import org.firstinspires.ftc.teamcode.util.v2f;


//DriveFS has all of the functions to control the bots drive motors.
//drive motors should not be messed with outside of these functions.


public abstract class DriveFS {


   public static void updateDrive(BotState s, float dt, v2f l, v2f r){

      //change target angle with input
      s.targetAngle += r.x * constants.turnRate;


      //get the dist to angle target
      //s.imu.getAngularOrientation().axesOrder;
      Orientation heading = s.imu.getAngularOrientation(
              AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
      float angle = heading.firstAngle;
      float angleDiff = DriveFS.angleWrap(s.targetAngle - angle);


      //rotate drive input to be relative to start angle, not current angle
      v2f in = new v2f(
              l.x * constants.strafeMod,
              l.y * constants.driveMod);
      in = in.rotated(-angle);

      //set turning to move towards target, clamped by MAX
      float nr = angleDiff;
      if(Math.abs(nr) > constants.MaxTurnTargetDist){
         nr = Math.signum(angleDiff) * constants.MaxTurnTargetDist;
      }


      //set motors to apply
      DriveFS.setPower(
              s,
              in,
              //also check out PID controllers/smoothing fns
              nr / constants.MaxTurnTargetDist);
   }







   // m&r need to be between 0 and 1
   //sets the power of drive motors so that the bot will move by m and turn by r
   public static void setPower(BotState s, @NonNull v2f m, float turn) {
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

      final float TAU = (float)(Math.PI*2);
      float modifiedAngle = (float)Math.toRadians(deg) % TAU;
      return (float)Math.toDegrees((modifiedAngle + TAU) % TAU);
   }
}