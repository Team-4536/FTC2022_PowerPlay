package org.firstinspires.ftc.teamcode.util;


import org.firstinspires.ftc.teamcode.functions.DriveFunctions;
import org.firstinspires.ftc.teamcode.functions.PIDFunctions;
import org.firstinspires.ftc.teamcode.util.Data.NavImageDetectionPipeline;
import org.firstinspires.ftc.teamcode.util.Data.PIDData;
import org.firstinspires.ftc.teamcode.util.Data.TelemetryData;
import org.firstinspires.ftc.teamcode.util.Data.EncoderOdometry;
import org.firstinspires.ftc.teamcode.util.XRobot;

public abstract class Stage {


    public boolean run() { return true; };
    public void init() { };



    public static class Wait extends Stage {
        double startTime;
        double duration;

        public Wait(double d) { this.duration = d; }

        @Override
        public void init() { this.startTime = XRobot.nav.timer.seconds(); }

        @Override
        public boolean run() {
            return (XRobot.nav.timer.seconds() - this.startTime) < this.duration;
        }
    }

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
        double requiredmm;
        V2f dir;
        EncoderOdometry robotOdom;


        public MoveByEncoders(V2f dir, double requiredmm, EncoderOdometry robotOdom) {
            this.dir = dir;
            this.requiredmm = requiredmm;
            this.robotOdom = robotOdom;
        }

        @Override
        public boolean run(){
            float PIDOut = PIDFunctions.updatePIDAngular(
                    XRobot.drivePID,
                    XRobot.nav.heading,
                    (float)XRobot.nav.dt
            );

            DriveFunctions.setPower(
                    XRobot.drive,
                    this.dir,
                    PIDOut);

            if(robotOdom.findDistance(XRobot.drive) >= requiredmm){
                return true;
            }
            else{
                return false;
            }

        }
    }

    public static class CenterOnImage extends Stage {

        int imageId;
        V2f target;
        NavImageDetectionPipeline pip;

        public CenterOnImage(int id, V2f pos, NavImageDetectionPipeline p) {
            this.imageId = id;
            this.target = p.imagePositions.get(id).add(pos);
            this.pip = p;
        }


        @Override
        public boolean run() {



            float PIDOut = PIDFunctions.updatePIDAngular(
                    XRobot.drivePID,
                    XRobot.nav.heading,
                    (float) XRobot.nav.dt);



            if (pip.imageVisible == this.imageId) {

                V2f rel = new V2f(this.target.x - pip.translation.x, this.target.y - pip.translation.y);

                XRobot.telemetry.addChild(new TelemetryData("Relative", rel));
                XRobot.telemetry.addChild(new TelemetryData("Target", target));

                rel = rel.normalized();

                DriveFunctions.setPower(XRobot.drive, rel.multiply(0.2f), PIDOut);

                return false;
            }

            DriveFunctions.setPower(XRobot.drive, new V2f(0.0f, 0.0f), PIDOut);
            return false;
        }
    }
}
