package org.firstinspires.ftc.teamcode.functions;

import org.firstinspires.ftc.teamcode.util.Data.DriveData;
import org.firstinspires.ftc.teamcode.util.Step;

public abstract class SequencerFunctions {


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

}
