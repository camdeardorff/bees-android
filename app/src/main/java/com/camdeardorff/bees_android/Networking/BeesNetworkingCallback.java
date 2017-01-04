package com.camdeardorff.bees_android.Networking;

import com.camdeardorff.bees_android.Models.BeesError;
import com.camdeardorff.bees_android.Models.BeesRecord;

import java.util.ArrayList;

/**
 * Created by Cam on 12/24/16.
 */

public interface BeesNetworkingCallback {
    // The callback interface
    void onSuccessWithRecords(ArrayList<BeesRecord> records);
    void onFailureWithError(BeesError error);
}
