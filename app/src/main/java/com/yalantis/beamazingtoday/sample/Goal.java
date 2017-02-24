package com.yalantis.beamazingtoday.sample;

import com.yalantis.beamazingtoday.interfaces.BatModel;

/**
 * Created by galata on 22.08.16.
 */
public class Goal implements BatModel {

    private String name;

    private boolean isChecked;

    private int id;

    public Goal(String name) {
        this.name = name;
    }

    public Goal(int id, String name, boolean isChecked) {
        this.id = id;
        this.name = name;
        this.isChecked = isChecked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getId() { return id;}

    public void setId(int id) { this.id = id; }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public String getText() {
        return getName();
    }

}
