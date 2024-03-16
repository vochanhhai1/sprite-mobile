package com.example.appqlsv.Models;

import java.util.List;

public class Filter {
    public int type;
    public String sort = "-price";
    public List<Float> range;
    public boolean wayUp = false;
    public Filter() {
    }
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public List<Float> getRange() {
        return range;
    }

    public void setRange(List<Float> range) {
        this.range = range;
    }

    public boolean isWayUp() {
        return wayUp;
    }

    public void setWayUp(boolean wayUp) {
        this.wayUp = wayUp;
    }


}
