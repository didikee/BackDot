package com.inno.backdot.model;

/**
 * Created by didik on 2016/5/17.
 */
public class DotMode {

    private int back=1;
    private int lock=2;
    private int long_click=3;
    private int pull_down=4;
    private int pull_up=5;

    public DotMode(int back, int lock, int long_click, int pull_down, int pull_up) {
        this.back = back;
        this.lock = lock;
        this.long_click = long_click;
        this.pull_down = pull_down;
        this.pull_up = pull_up;
    }

    public DotMode() {
    }

    public int getBack() {
        return back;
    }

    public void setBack(int back) {
        this.back = back;
    }

    public int getLock() {
        return lock;
    }

    public void setLock(int lock) {
        this.lock = lock;
    }

    public int getLong_click() {
        return long_click;
    }

    public void setLong_click(int long_click) {
        this.long_click = long_click;
    }

    public int getPull_down() {
        return pull_down;
    }

    public void setPull_down(int pull_down) {
        this.pull_down = pull_down;
    }

    public int getPull_up() {
        return pull_up;
    }

    public void setPull_up(int pull_up) {
        this.pull_up = pull_up;
    }
}
