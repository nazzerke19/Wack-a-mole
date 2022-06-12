package com.example.wack_a_mole;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton firstMole, secondMole, thirdMole, fourthMole, fifthMole, sixthMole, seventhMole, eighthMole, ninethMole;
    private TextView countDownTimerTxt, scoreTxt;

    private long curSeconds=0,curMillies =0;

    public static final String FINAL_SCORE_VALUE_EXTRA = "FINAL_SCORE_VALUE_EXTRA";


    private ArrayList<Integer> availableMoleIdArrayList = new ArrayList<>();

    int[] moleIdArray = {
        R.id.firstMoleBtn, R.id.secondMoleBtn, R.id.thirdMoleBtn, R.id.fourthMoleBtn, R.id.fifthMoleBtn, R.id.sixthMoleBnt,
            R.id.seventhMoleBtn, R.id.eigthMoleBtn, R.id.ninethMoleBtn
    };

    private CountDownTimer countDownTimer;
    private CountDownTimer gameTimer;

    private static boolean moleIsActive = false, countDownTimerEnabled = false;

    private static final int GAME_TIME = 30000;


    @Override
    public void onBackPressed() {  }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initElementsofUI();

        disableHoles();

        fillMolesArrayList(0);
        startGame(GAME_TIME);
    }
    private void startGame(int time){

                startTimer(time);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            default:
                break;
        }
    }
    private void pauseGame(){
        countDownTimerEnabled = false;
        gameTimer.cancel();

    }
    private void initElementsofUI(){
        firstMole = findViewById(R.id.firstMoleBtn);
        secondMole = findViewById(R.id.secondMoleBtn);
        thirdMole = findViewById(R.id.thirdMoleBtn);
        fourthMole = findViewById(R.id.fourthMoleBtn);
        fifthMole = findViewById(R.id.fifthMoleBtn);
        sixthMole = findViewById(R.id.sixthMoleBnt);
        seventhMole = findViewById(R.id.seventhMoleBtn);
        eighthMole = findViewById(R.id.eigthMoleBtn);
        ninethMole = findViewById(R.id.ninethMoleBtn);

        countDownTimerTxt = findViewById(R.id.countDownTimerTxt);
        scoreTxt = findViewById(R.id.scoreTxt);
    }

    private void disableHoles(){
        firstMole.setEnabled(false);
        secondMole.setEnabled(false);
        thirdMole.setEnabled(false);
        fourthMole.setEnabled(false);
        fifthMole.setEnabled(false);
        sixthMole.setEnabled(false);
        seventhMole.setEnabled(false);
        eighthMole.setEnabled(false);
        ninethMole.setEnabled(false);
    }

    private void fillMolesArrayList(int notAvailabeMoleId){
        for (int i=0;i<moleIdArray.length;i++)
            availableMoleIdArrayList.add(moleIdArray[i]);

        if (notAvailabeMoleId!=0)
            availableMoleIdArrayList.remove(new Integer(notAvailabeMoleId));

    }

    private void startTimer( int time){
        countDownTimer = new CountDownTimer(time,1) {
            @Override
            public void onTick(long l) {
                countDownTimerEnabled = true;
                long seconds = (l/1000)%60;
                curSeconds = seconds;
                long millies = l%1000;
                curMillies = millies;

                String stringSeconds = "", stringMillies ="";

                if (String.valueOf(seconds).length() < 2)
                    stringSeconds+="  "+ seconds;
                else if (String.valueOf(seconds).length()==2)
                    stringSeconds+=seconds;


                if ( seconds<10 && seconds>3)
                    countDownTimerTxt.setTextColor(getResources().getColor(R.color.black));
                else  if (seconds<=3)
                    countDownTimerTxt.setTextColor(getResources().getColor(R.color.black));

                countDownTimerTxt.setText(stringSeconds );
                if(!moleIsActive)
                    showMole();
            }

            @Override
            public void onFinish() {
                countDownTimerTxt.setText("0.000");
                startResultActivity();
            }
        }.start();
    }


    private void showMole(){
        Random randomMole = new Random();
        int randomMoleValue = randomMole.nextInt(20);
        ImageButton activeMole
                = findViewById(availableMoleIdArrayList.get(new Random().nextInt(availableMoleIdArrayList.size())));

        activeMole.setImageResource(R.drawable.ic_mole);
        activeMole.setEnabled(true);
        moleIsActive = true;

        final Handler handler = new Handler();
        final Runnable runnable = () -> {
            availableMoleIdArrayList.clear();
            fillMolesArrayList(activeMole.getId());

            activeMole.setImageResource(R.drawable.ic_mole_hole);
            activeMole.setEnabled(false);
            moleIsActive=false;
        };

        handler.postDelayed(runnable, 500);

        for (int i=0; i< moleIdArray.length; i++) {
            ImageButton imageButton = findViewById(moleIdArray[i]);
            imageButton.setOnClickListener(view -> {
                if (!imageButton.isEnabled())
                    return;

                imageButton.setImageResource(R.drawable.ic_mole_hole);
                scoreTxt.setText(String.valueOf(Integer.parseInt(scoreTxt.getText().toString()) + 1));

            });

        }
    }


    private void startResultActivity(){
        Intent intent = new Intent(GameActivity.this, ResultActivity.class);
        intent.putExtra(GameActivity.FINAL_SCORE_VALUE_EXTRA, scoreTxt.getText().toString());
        GameActivity.this.startActivity(intent);
    }
}