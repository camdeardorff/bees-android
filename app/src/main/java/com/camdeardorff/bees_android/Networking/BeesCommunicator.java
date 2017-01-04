package com.camdeardorff.bees_android.Networking;

import com.camdeardorff.bees_android.Models.BeesError;
import com.camdeardorff.bees_android.Models.BeesRecord;
import com.camdeardorff.bees_android.Models.BeesResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Cam on 12/23/16.
 */




public class BeesCommunicator {

    private interface GetDataCallback {
        void dataReceived(String message);
        void encounteredError();
    }

    public BeesCommunicator () {
        System.out.println("Bees Communicator INIT");
    }

    public void getTodaysRecords(String timeZone, final BeesNetworkingCallback callback) {

        String tz = timeZone.replaceAll("/", "*=SLASH=*");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://cafbees.herokuapp.com/report/today/" + tz)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                callback.onFailureWithError(null);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                final Gson gson = new Gson();
                gson.serializeNulls();

//                System.out.println("response: " + response.body().string());

                BeesResponse resp = gson.fromJson(response.body().charStream(), BeesResponse.class);
                callback.onSuccessWithRecords(resp.records);
            }
        });
    }



    private void getData(String timeZone, final GetDataCallback callback) {
        System.out.println("get data");
        // make new instance of okhttpclient
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://cafbees.herokuapp.com/report/today/America*=SLASH=*New_York")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                callback.encounteredError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                System.out.println("body: " + response.body().string());

                callback.dataReceived(response.body().string());

            }
        });
    }

    private ArrayList<BeesRecord> getRecordsFromMessage(String messageData) {
        System.out.println("get records from message");

        final Gson gson = new Gson();
        gson.serializeNulls();

        BeesResponse response = gson.fromJson(messageData, BeesResponse.class);
        return response.records;
    }

    private BeesError getErrorFromMessage(String messageData) {
        System.out.println("get error from messgae");
        final Gson gson = new Gson();
        gson.serializeNulls();
        return gson.fromJson(messageData, BeesResponse.class).error;
    }
}











/*

OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://cafbees.herokuapp.com/report/today/America*=SLASH=*New_York")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                callback.onFailureWithError(null);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            final Gson gson = new Gson();
            gson.serializeNulls();

            BeesResponse resp = gson.fromJson(response.body().charStream(), BeesResponse.class);
                callback.onSuccessWithRecords(resp.records);
            }
        });


 */