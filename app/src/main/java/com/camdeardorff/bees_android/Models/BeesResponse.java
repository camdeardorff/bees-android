package com.camdeardorff.bees_android.Models;

import java.util.ArrayList;

/**
 * Created by Cam on 12/23/16.
 */

public class BeesResponse {
    public Boolean success;
    public ArrayList<BeesRecord> records;
    public BeesError error;

    public BeesResponse(Boolean success, ArrayList<BeesRecord> records, BeesError error) {
        this.success = success;
        this.records = records;
        this.error = error;
    }
}
