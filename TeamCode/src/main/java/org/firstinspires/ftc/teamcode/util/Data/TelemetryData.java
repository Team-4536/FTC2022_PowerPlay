package org.firstinspires.ftc.teamcode.util.Data;
import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.util.Constants;
import org.firstinspires.ftc.teamcode.util.V2f;

import java.util.ArrayList;

public class TelemetryData{
    @NonNull
    public String title = "";
    @NonNull
    public String info = "";


    public TelemetryData parent;

    @NonNull
    private final ArrayList<TelemetryData> children = new ArrayList<>();




    public TelemetryData() { }
    public TelemetryData(@NonNull String _name) { this.title = _name; }
    public TelemetryData(@NonNull String _name, @NonNull TelemetryData parent) {
        this.title = _name;
        parent.addChild(this);
    }
    public TelemetryData(@NonNull String _name, @NonNull String _info) {
        this.title = _name;
        this.info = _info;
    }
    public TelemetryData(@NonNull String _name, double _info){
        this.title = _name;
        this.info = String.valueOf(_info);
    }
    public TelemetryData(@NonNull String _name, float _info){
        this.title = _name;
        this.info = String.valueOf(_info);
    }
    public  TelemetryData(@NonNull String _name, boolean _info){
        this.title = _name;
        this.info = _info?"true":"false";
    }
    public TelemetryData(@NonNull String _name, V2f _info){
        this.title = _name;
        this.addChild("X", _info.x);
        this.addChild("Y", _info.y);
    }




    public void addChild(TelemetryData d){
        this.children.add(d);
        d.parent = this;
    }

    public void addChild(@NonNull String a, String b){
        TelemetryData t = new TelemetryData(a, b);
        this.addChild(t);
    }
    public void addChild(@NonNull String a, double b){
        TelemetryData t = new TelemetryData(a, b);
        this.addChild(t);
    }
    public void addChild(@NonNull String a, boolean b){
        TelemetryData t = new TelemetryData(a, b?"true":"false");
        this.addChild(t);
    }


    public String getString(){
        int level = this.getLevel();

        String r = "";
        //r = r + level + " ";
        for(int i = 0; i < level; i++){
            r = r + Constants.TELEMETRY_INDENT; }


        r += this.title + ((!this.info.equals(""))? ": " : "") + this.info + "\n";
        for (TelemetryData t : this.children) {
            r = r + t.getString();
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


    public void clear(){
        this.children.clear();
    }

}
