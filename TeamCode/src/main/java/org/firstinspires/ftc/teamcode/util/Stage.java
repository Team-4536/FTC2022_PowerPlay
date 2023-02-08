package org.firstinspires.ftc.teamcode.util;


import org.firstinspires.ftc.teamcode.functions.DriveFunctions;
import org.firstinspires.ftc.teamcode.functions.PIDFunctions;
import org.firstinspires.ftc.teamcode.util.Data.PIDData;

public abstract class Stage {


    public boolean run() { return true; };
    public void init() { };




    public static class MoveTimed extends Stage {

        double startTime = 0;
        double duration;
        V2f dir;

        public MoveTimed(V2f dir, double time) {
            this.dir = dir;
            this.duration = time;
        }

        @Override
        public void init() {
            this.startTime = XRobot.nav.timer.seconds();
        }

        @Override
        public boolean run() {

            float PIDOut = PIDFunctions.updatePIDAngular(
                    XRobot.drivePID,
                    XRobot.nav.heading,
                    (float)XRobot.nav.dt
            );

            DriveFunctions.setPower(
                    XRobot.drive,
                    this.dir,
                    PIDOut);

            return this.duration < (XRobot.nav.timer.seconds() - startTime);
        }
    }



    public static class MoveByEncoders extends Stage {

    }

    public static class CenterOnImage extends Stage {

        PIDData xpid = new PIDData(0.1f, 0.0f, 0.0f);
        PIDData ypid = new PIDData(xpid.Kp, xpid.Ki, xpid.Kd);

        public CenterOnImage(int id, V2f pos) {

        }
    }
}
