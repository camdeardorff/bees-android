package com.camdeardorff.bees_android.Models;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;

/**
 * Created by Cam on 12/26/16.
 */

public class AxisFormatter implements IAxisValueFormatter {

    private ArrayList<BeesRecord> records;

    public AxisFormatter(ArrayList<BeesRecord> records) {
        this.records = records;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if (value % 1.0 == 0) {
            return this.records.get((int) value).range.from;
        } else {
            return "";
        }
    }
}

