package com.camdeardorff.bees_android.Models;

/**
 * Created by Cam on 12/23/16.
 */

public class BeesRecord {
    public Double loudness;
    public BeesRange range;

    public BeesRecord(Double loudness, BeesRange range) {
        this.loudness = loudness;
        this.range = range;
    }
}
