package org.firstinspires.ftc.teamcode.util.Data;

import org.firstinspires.ftc.teamcode.util.Stage;
import org.firstinspires.ftc.teamcode.util.XRobot;

import java.util.ArrayList;
import java.util.List;

public class AutoData {


    public List<Stage> stages = new ArrayList<>();
    public int index = 0;


    public void run() {

        if(this.index >= this.stages.size()) { return; }

        if(this.stages.get(this.index).run()) {

            this.index += 1;
            this.stages.get(this.index).init();
        }
    }
}
