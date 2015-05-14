package com.example.martin.speechtotext;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends Activity {

    protected static final int RESULT_SPEECH = 1;

    private ImageButton btnSpeak;

    private Switch head;
    private Switch leftArm;
    private Switch rightArm;
    private Switch leftLeg;
    private Switch rightLeg;

    private TextView txtText;

    private Button blueButton;

    private BluetoothArduino mBlue;

    //private LogDatabase myDb;

    private int lOn = 0;
    private int lOff = 0;
    private int h = 0;
    private int ra = 0;
    private int la = 0;
    private int rl = 0;
    private int ll = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //myDb = new LogDatabase(this);

        mBlue = BluetoothArduino.getInstance("ExampleRobot");

        head = (Switch) findViewById(R.id.headSwitch);
        leftArm = (Switch) findViewById(R.id.leftArmSwitch);
        rightArm = (Switch) findViewById(R.id.rightArmSwitch);
        leftLeg = (Switch) findViewById(R.id.leftLegSwitch);
        rightLeg = (Switch) findViewById(R.id.rightLegSwitch);

        txtText = (TextView) findViewById(R.id.textView);

        blueButton = (Button) findViewById(R.id.bluetoothButton);

        blueButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mBlue.Connect()){
                    txtText.setText("Connected to Arduino");
                }else{
                    txtText.setText("Error in connecting to Arduino");
                }
            }
        });

        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Choose Command");
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);

                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(getApplicationContext(),
                            "Ops! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected synchronized void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data.
                            getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    txtText.setText(text.get(0));



                    if(txtText.getText().equals("lights on")) {
                        lOn = lOn + 1;
                        //myDb.increment("lights on", lOn);
                        head.setChecked(true);
                        leftArm.setChecked(true);
                        rightArm.setChecked(true);
                        leftLeg.setChecked(true);
                        rightLeg.setChecked(true);
                        mBlue.sendMessage("lights on");
                    }else if(txtText.getText().equals("lights off")) {
                        lOff = lOff + 1;
                        //myDb.increment("lights off", lOff);
                        head.setChecked(false);
                        leftArm.setChecked(false);
                        rightArm.setChecked(false);
                        leftLeg.setChecked(false);
                        rightLeg.setChecked(false);
                        mBlue.sendMessage("lights off");
                    }else if(txtText.getText().equals("head")){
                        h = h + 1;
                        //myDb.increment("head", h);
                        switchOn(head);
                        mBlue.sendMessage("head");
                    }else if(txtText.getText().equals("left arm")){
                        la = la + 1;
                        //myDb.increment("left arm", la);
                        switchOn(leftArm);
                        mBlue.sendMessage("left arm");
                    }else if(txtText.getText().equals("right arm")){
                        ra = ra + 1;
                        //myDb.increment("right arm", ra);
                        switchOn(rightArm);
                        mBlue.sendMessage("right arm");
                    }else if(txtText.getText().equals("left leg")){
                        ll = ll + 1;
                        //myDb.increment("left leg", ll);
                        switchOn(leftLeg);
                        mBlue.sendMessage("left leg");
                    }else if(txtText.getText().equals("right leg")){
                        rl = rl + 1;
                        //myDb.increment("right leg", rl);
                        switchOn(rightLeg);
                        mBlue.sendMessage("right leg");
                    }
                }
                break;
            }
        }
    }

    private void switchOn(View v){
        ((Switch) v).toggle();
    }
}
