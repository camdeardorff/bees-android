package com.camdeardorff.bees_android.Activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.camdeardorff.bees_android.Models.AxisFormatter;
import com.camdeardorff.bees_android.Models.BeesError;
import com.camdeardorff.bees_android.Models.BeesRecord;
import com.camdeardorff.bees_android.Networking.BeesCommunicator;
import com.camdeardorff.bees_android.Networking.BeesNetworkingCallback;
import com.camdeardorff.bees_android.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private Handler uiThreadHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // get the toolbar and setup the menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set the loading gif and show it
        ImageView loadingGif = (ImageView) findViewById(R.id.loadingGif);
        Glide.with(this).load(R.drawable.jumpy).into(loadingGif);
        showLoadingAnimation();

        // set the main thread handler
        uiThreadHandler = new Handler(Looper.getMainLooper());

        // configure and reload the chart
        configureChart();
        reloadChart();
    }


    public void reloadChart() {
        // get the default time zone id
        String timezone = TimeZone.getDefault().getID();

        // get the records for today at this time zone
        BeesCommunicator communicator = new BeesCommunicator();
        communicator.getTodaysRecords(timezone, new BeesNetworkingCallback() {
            @Override
            public void onSuccessWithRecords(final ArrayList<BeesRecord> records) {
                // get the main thread update the ui
                uiThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingAnimation();
                        if (records != null) {
                            displayData(records);
                        } else {
                            showNoDataMessage();
                        }
                    }
                });
            }

            @Override
            public void onFailureWithError(BeesError error) {
                System.out.println("received error with message: " + error.message);
                uiThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingAnimation();
                    }
                });
            }
        });
    }

    public void displayData(ArrayList<BeesRecord> records) {
        // get the chart and configure the value formatter with the new records
        LineChart chart = (LineChart) findViewById(R.id.chart);
        chart.getXAxis().setValueFormatter(new AxisFormatter(records));

        // an array list of MPAndroidChart entry objects
        ArrayList<Entry> lineDataEntries = new ArrayList<Entry>();

        // populate the entry array list with the records
        for (Integer i = 0; i < records.size(); i++) {
            lineDataEntries.add(new Entry(i.floatValue(), records.get(i).loudness.floatValue()));
        }

        // set the loudness data set
        LineDataSet loudnessDataSet = new LineDataSet(lineDataEntries, "MVNU Cafeteria Loudness Today");
        // set general line styles
        loudnessDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        loudnessDataSet.setColor(Color.rgb(51, 51, 51));
        loudnessDataSet.setLineWidth(2f);
        // chart fill
        loudnessDataSet.setFillColor(Color.rgb(0, 188, 255));
        loudnessDataSet.setDrawFilled(true);
        loudnessDataSet.setFillAlpha(153);
        // circle styles
        loudnessDataSet.setCircleColor(Color.rgb(51, 51, 51));
        loudnessDataSet.setCircleRadius(4f);
        // circle hole styles
        loudnessDataSet.setCircleColorHole(Color.rgb(0, 188, 255));
        loudnessDataSet.setCircleHoleRadius(3f);
        // hide the values
        loudnessDataSet.setValueTextColor(Color.alpha(255));

        // give the data to the chart
        LineData lineData = new LineData(loudnessDataSet);
        chart.setData(lineData);
        // refresh the chart
        chart.invalidate();
        chart.animateY(2000, Easing.EasingOption.EaseInOutCubic);

    }


    public void configureChart() {
        // get the chart
        LineChart chart = (LineChart) findViewById(R.id.chart);
        // give the chart an empty description
        Description emptyDescription = new Description();
        emptyDescription.setText("");
        chart.setDescription(emptyDescription);

        chart.setNoDataText("");
        chart.setNoDataTextColor(R.color.Black);
        // configure axis styles
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setTextColor(Color.BLACK);
        chart.getAxisRight().setEnabled(false);
    }

    private void showNoDataMessage() {
        System.out.println("Show no data message");

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.no_data_message)
                .setTitle(R.string.no_data_title);

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();

        builder.setPositiveButton(R.string.no_data_action_done, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        });
        builder.show();
    }

    public void showLoadingAnimation() {
        ImageView loadingGif = (ImageView) findViewById(R.id.loadingGif);
        loadingGif.setVisibility(View.VISIBLE);
    }

    public void hideLoadingAnimation() {
        ImageView loadingGif = (ImageView) findViewById(R.id.loadingGif);
        loadingGif.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            showLoadingAnimation();
            reloadChart();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}