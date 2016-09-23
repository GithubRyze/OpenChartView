package com.wongs.openchartview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    double temp_test[] = {18.2,12.5,12.5,10.4,16.7,19.8,10.8,14.9,17.7,20.1,
            5.4,8.0,5.6,18.9,10.4,3.4,17.6,19.4,12.6,9.2,
            15.5,12.5,10.4,13.8,4.9,11.7,2.1,17.6,19.4,20.0,
            2.4,12.0,5.6,18.9,10.4,13.4,17.6,19.4,15.0,21.2,
            10.4,16.7,19.8,9.8,7.9,17.6,19.4,3.0,9.2,17.6};
    private Queue<Double> queue = new LinkedList<>();
    private String texts[] = {" 5","10","15","20"};
    ChartView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (ChartView) findViewById(R.id.charview);
        view.setScaleText(texts);
        view.setTitle("%");
        view.setScale(5);
        timer.schedule(task, 2000, 1000);
        fillTestData();
    }


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 8) {
                if (!queue.isEmpty()){
                    double temp = queue.poll();
                    queue.add(temp);
                    String value  = String.valueOf(temp);
                    //temp_value.setText(value + getActivity().getResources().getString(R.string.percent_symbol));
                    view.invalidateView(temp);
                }

            }
        }
    };
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {

            Message msg = handler.obtainMessage();
            msg.what=8;
            msg.sendToTarget();
        }
    };

    public void fillTestData(){

        for(int i = 0;i<temp_test.length;i++){
            queue.add(temp_test[i]);
        }
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
