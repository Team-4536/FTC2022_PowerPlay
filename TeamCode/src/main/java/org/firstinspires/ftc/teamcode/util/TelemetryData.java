package org.firstinspires.ftc.teamcode.util;
import androidx.annotation.NonNull;

import java.util.ArrayList;

public class TelemetryData{
    public String title;
    public String info;
    public TelemetryData parent;
    @NonNull
    public ArrayList<TelemetryData> children = new ArrayList<TelemetryData>();




    public TelemetryData() { };
    public TelemetryData(String _name) { this.title = _name; }
    public TelemetryData(String _name, String _info) {
        this.title = _name;
        this.info = _info;
    }
    public TelemetryData(String _name, double _info){
        this.title = _name;
        this.info = String.valueOf(_info);
    }




    public void addChild(TelemetryData d){
        this.children.add(d);
    }

    public String getString(){
        int level = this.getLevel();

        String r = "";
        for(int i = 0; i < level; i++){
            r += constants.telemetryIndent; }


        r += this.title + ": " + this.info + "\n";
        for (TelemetryData t : this.children) {
            r += t.getString();
        }

        return r;
    }

    public int getLevel(){
        if(this.parent == null){
            return 0;
        }else{
            return this.parent.getLevel() + 1;
        }
    }

}
