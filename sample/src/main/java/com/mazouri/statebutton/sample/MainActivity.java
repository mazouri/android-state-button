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
    StateButton stateButton4;
    StateButton stateButton5;
    StateButton stateButton6;
    StateButton stateButton7;
    StateButton stateButton8;
    StateButton stateButton9;
    StateButton stateButton10;
    StateButton stateButton11;
    StateButton stateButton12;
    StateButton stateButton13;
    StateButton stateButton14;
    StateButton stateButton15;
    StateButton stateButton16;

    private Timer mTimer;
    private TimerTask mTimerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stateButton = (StateButton) findViewById(R.id.state_button);
        stateButton2 = (StateButton) findViewById(R.id.state_button2);
        stateButton3 = (StateButton) findViewById(R.id.state_button3);
        stateButton4 = (StateButton) findViewById(R.id.state_button4);
        stateButton5 = (StateButton) findViewById(R.id.state_button5);
        stateButton6 = (StateButton) findViewById(R.id.state_button6);
        stateButton7 = (StateButton) findViewById(R.id.state_button7);
        stateButton8 = (StateButton) findViewById(R.id.state_button8);
        stateButton9 = (StateButton) findViewById(R.id.state_button9);
        stateButton10 = (StateButton) findViewById(R.id.state_button10);
        stateButton11 = (StateButton) findViewById(R.id.state_button11);
        stateButton12 = (StateButton) findViewById(R.id.state_button12);
        stateButton13 = (StateButton) findViewById(R.id.state_button13);
        stateButton14 = (StateButton) findViewById(R.id.state_button14);
        stateButton15 = (StateButton) findViewById(R.id.state_button15);
        stateButton16 = (StateButton) findViewById(R.id.state_button16);
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
                            stateButton4.setState(button_states);
                            stateButton7.setState(button_states);
                            stateButton10.setState(button_states);
                            stateButton13.setState(button_states);
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
                changState(stateButton2);
            }
        });

        stateButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDisableToast(stateButton3);
            }
        });

        stateButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changState(stateButton5);
            }
        });
        stateButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDisableToast(stateButton6);
            }
        });

        stateButton8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changState(stateButton8);
            }
        });
        stateButton9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDisableToast(stateButton9);
            }
        });

        stateButton11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changState(stateButton11);
            }
        });
        stateButton12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDisableToast(stateButton12);
            }
        });

        stateButton14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changState(stateButton14);
            }
        });
        stateButton15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDisableToast(stateButton15);
            }
        });
        stateButton16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateButton16.setState(StateButton.BUTTON_STATES.LOADING);
            }
        });
        stateButton16.setCountdownProgressListener(new StateButton.OnCountdownListener() {
            @Override
            public void onProgress(int progress) {
                stateButton16.setProgressText(progress + "");
            }
        });
    }

    private void changState(StateButton stateButton) {
        if (stateButton.getState() == StateButton.BUTTON_STATES.ENABLED) {
            stateButton.setState(StateButton.BUTTON_STATES.SELECTED);
        } else if (stateButton.getState() == StateButton.BUTTON_STATES.SELECTED) {
            stateButton.setState(StateButton.BUTTON_STATES.ENABLED);
        }
    }

    private void showDisableToast(StateButton stateButton) {
        Toast.makeText(MainActivity.this, "current state is " + stateButton.getState().name(), Toast.LENGTH_SHORT).show();
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
