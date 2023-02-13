package me.missigdrift.missinghub.sidetimer;

import me.missigdrift.missinghub.commands.SideTimerCMD;

public class SideTimer {

    int i = 0;

    private String name;
    private String format;
    private int time;
    private boolean pause;

    public SideTimer(String name, String format, int time, boolean pause) {
        this.name = name;
        this.format = format;
        this.time = time;
        this.pause = pause;
    }

    public SideTimer(String name) {
        this.name = name;
        this.format = null;
        this.time = 0;
        this.pause = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time, MissingHub MissingHub) {
        this.time = time;
        i++;
        if(i == 30 || time <= 0){
            SideTimerCMD.saveConfig(this, MissingHub);
            i = 0;
        }
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public static SideTimer getSideTimer(String name, MissingHub MissingHub){
        for(SideTimer sideTimer : MissingHub.sidetimers){
            if(sideTimer.getName().equalsIgnoreCase(name)){
                return sideTimer;
            }
        }
        return null;
    }
}
