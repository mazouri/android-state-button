package com.mazouri.statebutton.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import mazouri.statebutton.StateButton;

public class MainActivity extends AppCompatActivity {

    StateButton stateButton;
    StateButton stateButton2;
    StateButton stateButton3;

    private Timer mTimer;
    private TimerTask mTimerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stateButton = (StateButton) findViewById(R.id.state_button);
        stateButton2 = (StateButton) findViewById(R.id.state_button2);
        stateButton3 = (StateButton) findViewById(R.id.state_button3);
        setup();
    }

    private void setup() {
        final Random random = new Random();
        mTimer = new Timer();

        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int nextInt = random.nextInt(3);
                            StateButton.BUTTON_STATES button_states = StateButton.BUTTON_STATES.fromValue(nextInt);
                            stateButton.setState(button_states);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    });
            }
        };
        mTimer.schedule(mTimerTask, 200, 2000);

        stateButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stateButton2.getState() == StateButton.BUTTON_STATES.ENABLED) {
                    stateButton2.setState(StateButton.BUTTON_STATES.SELECTED);
                } else if (stateButton2.getState() == StateButton.BUTTON_STATES.SELECTED) {
                    stateButton2.setState(StateButton.BUTTON_STATES.ENABLED);
                }
            }
        });

        stateButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "current state is " + stateButton3.getState().name(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }

    private void cancelTimer(){
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }
}
