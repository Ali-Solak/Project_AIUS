package  Project_AIUS.Model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SatelliteData {

    private ArrayList<Integer> signalStrength;
    private int time;

    public SatelliteData() {
        this.signalStrength = new ArrayList<>();
    }

    public ArrayList<Integer> getSignalStrength() {
        return signalStrength;
    }

    public int getTime() {
        return time;
    }

    public void setSignalStrength(ArrayList<Integer> signalStrength) {
        this.signalStrength = signalStrength;
    }

    public void setTime(int time) {
        this.time = time;
    }
}


