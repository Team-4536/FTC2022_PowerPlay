package org.firstinspires.ftc.teamcode.functions;

import org.firstinspires.ftc.teamcode.util.Data.DriveData;
import org.firstinspires.ftc.teamcode.util.Step;
import org.firstinspires.ftc.teamcode.util.Data.EncoderOdometry;

public abstract class SequencerFunctions {
    public static int stepIndex = 0;

    public static Step getStep(Step[] steps, float time){
        float durationAcc = time;

        for(int i = 0; i < steps.length; i++){

            Step s = steps[i];

            if(durationAcc < s.duration){
                return s;
            }

            durationAcc -= s.duration;
        }

        return new Step();
    }

    public static Step getTickStep(Step[] steps, EncoderOdometry robotOdom, DriveData drive){
        double distancemm = robotOdom.findDistance(drive);

        Step s = steps[stepIndex];
        if(s.duration - distancemm > 0){
            return s;
        }
        else if(stepIndex + 1 < steps.length) {
            stepIndex += 1;
            return steps[stepIndex];
        }
        return new Step();

    }

}
