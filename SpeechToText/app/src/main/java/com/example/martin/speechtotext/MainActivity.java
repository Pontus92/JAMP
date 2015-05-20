package com.example.martin.speechtotext;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Switch;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;

public class MainActivity extends Activity {

    protected static final int RESULT_SPEECH = 1;

    private ImageButton btnSpeak;

    private Switch head;
    private Switch leftArm;
    private Switch rightArm;
    private Switch leftLeg;
    private Switch rightLeg;

    private TextView txtText;

    private Button blueBtn;

    private BluetoothArduino mBlue;
    private Intent btActivity;

    private SQLiteDatabase db;

    private int lOn = 0;
    private int lOff = 0;
    private int h = 0;
    private int ra = 0;
    private int la = 0;
    private int rl = 0;
    private int ll = 0;
    private int i = 1;

    private String device;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        head = (Switch) findViewById(R.id.headSwitch);
        leftArm = (Switch) findViewById(R.id.leftArmSwitch);
        rightArm = (Switch) findViewById(R.id.rightArmSwitch);
        leftLeg = (Switch) findViewById(R.id.leftLegSwitch);
        rightLeg = (Switch) findViewById(R.id.rightLegSwitch);

        txtText = (TextView) findViewById(R.id.textView);

        blueBtn = (Button) findViewById(R.id.bluetoothButton);

        db = openOrCreateDatabase("SpeechToText.db", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS bodyparts(id integer," +
                "bodypart VARCHAR,used integer);");

        db.execSQL("INSERT INTO bodyparts VALUES('"+1+
                "', '"+"lights on"+"', '"+lOn+"');");
        db.execSQL("INSERT INTO bodyparts VALUES('"+2+
                "', '"+"lights off"+"', '"+lOff+"');");
        db.execSQL("INSERT INTO bodyparts VALUES('"+3+
                "', '"+"head"+"', '"+h+"');");
        db.execSQL("INSERT INTO bodyparts VALUES('"+4+
                "', '"+"left arm"+"', '"+la+"');");
        db.execSQL("INSERT INTO bodyparts VALUES('"+5+
                "', '"+"right arm"+"', '"+ra+"');");
        db.execSQL("INSERT INTO bodyparts VALUES('"+6+
                "', '"+"left leg"+"', '"+ll+"');");
        db.execSQL("INSERT INTO bodyparts VALUES('"+7+
                "', '"+"right leg"+"', '"+rl+"');");

        btActivity = new Intent(this, Main_Bluetooth.class);

        blueBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivityForResult(btActivity, 2);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            showDatabase();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void btDevice(String device){
        txtText.setText(device);
        mBlue = BluetoothArduino.getInstance(device);
    }

    @Override
    protected synchronized void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        device = data.getDataString();

        if(requestCode == 2){
            if(resultCode == RESULT_OK) {
                Log.i("Log", "Device recieved: " + device);
                btDevice(device);
            }
        }else {
            switch (requestCode) {
                case RESULT_SPEECH: {
                    if (resultCode == RESULT_OK && null != data) {

                        ArrayList<String> text = data.
                                getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                        txtText.setText(text.get(0));

                        if (txtText.getText().equals("lights on")) {
                            i = 1;
                            lOn = lOn + 1;
                            db.execSQL("UPDATE bodyparts SET bodypart='" + "lights on" + "',used='" + lOn +
                                    "' WHERE id='" + i + "'");

                            head.setChecked(true);
                            leftArm.setChecked(true);
                            rightArm.setChecked(true);
                            leftLeg.setChecked(true);
                            rightLeg.setChecked(true);
                            mBlue.sendMessage("lights on");
                        } else if (txtText.getText().equals("lights off")) {
                            i = 2;
                            lOff = lOff + 1;
                            db.execSQL("UPDATE bodyparts SET bodypart='" + "lights off" + "',used='" + lOff +
                                    "' WHERE id='" + i + "'");

                            head.setChecked(false);
                            leftArm.setChecked(false);
                            rightArm.setChecked(false);
                            leftLeg.setChecked(false);
                            rightLeg.setChecked(false);
                            mBlue.sendMessage("lights off");
                        } else if (txtText.getText().equals("head")) {
                            i = 3;
                            h = h + 1;
                            db.execSQL("UPDATE bodyparts SET bodypart='" + "head" + "',used='" + h +
                                    "' WHERE id='" + i + "'");

                            switchOn(head);
                            mBlue.sendMessage("head");
                        } else if (txtText.getText().equals("left arm")) {
                            i = 4;
                            la = la + 1;
                            db.execSQL("UPDATE bodyparts SET bodypart='" + "left arm" + "',used='" + la +
                                    "' WHERE id='" + i + "'");

                            switchOn(leftArm);
                            mBlue.sendMessage("left arm");
                        } else if (txtText.getText().equals("right arm")) {
                            i = 5;
                            ra = ra + 1;
                            db.execSQL("UPDATE bodyparts SET bodypart='" + "right arm" + "',used='" + ra +
                                    "' WHERE id='" + i + "'");

                            switchOn(rightArm);
                            mBlue.sendMessage("right arm");
                        } else if (txtText.getText().equals("left leg")) {
                            i = 6;
                            ll = ll + 1;
                            db.execSQL("UPDATE bodyparts SET bodypart='" + "left leg" + "',used='" + ll +
                                    "' WHERE id='" + i + "'");

                            switchOn(leftLeg);
                            mBlue.sendMessage("left leg");
                        } else if (txtText.getText().equals("right leg")) {
                            i = 7;
                            rl = rl + 1;
                            db.execSQL("UPDATE bodyparts SET bodypart='" + "right leg" + "',used='" + rl +
                                    "' WHERE id='" + i + "'");

                            switchOn(rightLeg);
                            mBlue.sendMessage("right leg");
                        }
                    }
                    break;
                }
            }
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    //Fires after the OnStop() state
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            db.delete("bodyparts", null, null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void showDatabase(){
        Cursor c=db.rawQuery("SELECT * FROM bodyparts", null);
        if(c.getCount()==0)
        {
            showMessage("Error", "No records found");
            return;
        }
        StringBuffer buffer=new StringBuffer();
        while(c.moveToNext())
        {
            buffer.append("Id: "+c.getString(0)+"\n");
            buffer.append("Bodypart: "+c.getString(1)+"\n");
            buffer.append("Used: "+c.getString(2)+"\n\n");
        }
        showMessage("Bodypart Details", buffer.toString());
    }

    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    private void switchOn(View v){
        ((Switch) v).toggle();
    }
}
