package edu.uw.sensordemo;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class MotionActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "Motion";

    private TextView txtX, txtY, txtZ;

    private boolean sensorOn;

    private SensorManager sManager;

    private Sensor mSensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion);

        //views for easy access
        txtX = (TextView)findViewById(R.id.txt_x);
        txtY = (TextView)findViewById(R.id.txt_y);
        txtZ = (TextView)findViewById(R.id.txt_z);

        sManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        List<Sensor> sensors = sManager.getSensorList(Sensor.TYPE_ALL);
        for(Sensor s : sensors) {
            Log.v(TAG, sensors.toString());
        }

        mSensor = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if(mSensor == null) {
            //finish();
        }
    }


    private void startSensor() {
        sManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

        sensorOn = true;
    }

    private void stopSensor() {
        sManager.unregisterListener(this, mSensor);
        sensorOn = false;
    }

    @Override
    protected void onResume() {
        startSensor();
        super.onResume();
    }

    @Override
    protected void onPause() {
        stopSensor();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;

        Log.v(TAG, Arrays.toString(values));

        txtX.setText(String.format("%.3f", values[0]));
        txtY.setText(String.format("%.3f", values[1]));
        txtZ.setText(String.format("%.3f", values[2]));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_toggle:
                if(sensorOn) {
                    item.setTitle(getString(R.string.start_menu));
                    stopSensor();
                }
                else {
                    item.setTitle(getString(R.string.stop_menu));
                    startSensor();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
