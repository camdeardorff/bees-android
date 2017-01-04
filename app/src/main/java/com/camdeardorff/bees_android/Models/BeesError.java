package com.camdeardorff.bees_android.Models;

/**
 * Created by Cam on 12/23/16.
 */

public class BeesError {
    public String code;
    public String message;

    public BeesError(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
