package com.example.martin.speechtotext;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

    private boolean headIsChecked = false;
    private boolean rightArmIsChecked = false;
    private boolean leftArmIsChecked = false;
    private boolean rightLegIsChecked = false;
    private boolean leftLegIsChecked = false;

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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("Head", head.isChecked());
        savedInstanceState.putBoolean("Right Arm", rightArm.isChecked());
        savedInstanceState.putBoolean("Left Arm", leftArm.isChecked());
        savedInstanceState.putBoolean("Right Leg", rightLeg.isChecked());
        savedInstanceState.putBoolean("Left Leg", leftLeg.isChecked());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        headIsChecked = savedInstanceState.getBoolean("Head");
        rightArmIsChecked = savedInstanceState.getBoolean("Right Arm");
        leftArmIsChecked = savedInstanceState.getBoolean("Left Arm");
        rightLegIsChecked = savedInstanceState.getBoolean("Right Leg");
        leftLegIsChecked = savedInstanceState.getBoolean("Left Leg");
    }

    protected void onResume(){
        super.onResume();
        head.setChecked(headIsChecked);
        rightArm.setChecked(rightArmIsChecked);
        leftArm.setChecked(leftArmIsChecked);
        rightLeg.setChecked(rightLegIsChecked);
        leftLeg.setChecked(leftLegIsChecked);
    }

    public void onStart(){
        super.onStart();
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
        txtText.setText("");
        mBlue = new BluetoothArduino(device);

        final ViewGroup layout = (ViewGroup) blueBtn.getParent();
        final TextView btStatus = new TextView(this);
        final Button dcButton = new Button(this);

        if(mBlue.Connect()) {
            if (layout != null) {
                layout.removeView(blueBtn);
            }
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER_HORIZONTAL;

            layout.addView(btStatus, lp);
            btStatus.setText("Connected to: " + device);
            btStatus.setTextColor(Color.WHITE);

            layout.addView(dcButton, lp);
            dcButton.setText("Disconnect");
        }else{
            txtText.setText("Error in connecting, try again!");
        }
        dcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layout != null) {
                    mBlue.Disconnect("FireFly-11AF");
                    layout.removeView(btStatus);
                    layout.removeView(dcButton);
                    layout.addView(blueBtn);
                }
            }
        });
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

                            headIsChecked = true;
                            rightArmIsChecked = true;
                            leftArmIsChecked = true;
                            rightLegIsChecked = true;
                            leftLegIsChecked = true;

                            mBlue.sendMessage("1");
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

                            headIsChecked = false;
                            rightArmIsChecked = false;
                            leftArmIsChecked = false;
                            rightLegIsChecked = false;
                            leftLegIsChecked = false;

                            mBlue.sendMessage("2");
                        } else if (txtText.getText().equals("head")) {
                            i = 3;
                            if(!head.isChecked()) {
                                h = h + 1;
                                db.execSQL("UPDATE bodyparts SET bodypart='" + "head" + "',used='" + h +
                                        "' WHERE id='" + i + "'");
                            }
                            head.toggle();
                            if(headIsChecked == false){
                                headIsChecked = true;
                            }else{
                                headIsChecked = false;
                            }

                            mBlue.sendMessage("3");
                        } else if (txtText.getText().equals("left arm")) {
                            i = 4;
                            if(!leftArm.isChecked()) {
                                la = la + 1;
                                db.execSQL("UPDATE bodyparts SET bodypart='" + "left arm" + "',used='" + la +
                                        "' WHERE id='" + i + "'");
                            }
                            leftArm.toggle();
                            if(leftArmIsChecked == false){
                                leftArmIsChecked = true;
                            }else{
                                leftArmIsChecked = false;
                            }

                            mBlue.sendMessage("7");
                        } else if (txtText.getText().equals("right arm")) {
                            i = 5;
                            if(!rightArm.isChecked()) {
                                ra = ra + 1;
                                db.execSQL("UPDATE bodyparts SET bodypart='" + "right arm" + "',used='" + ra +
                                        "' WHERE id='" + i + "'");
                            }
                            rightArm.toggle();
                            if(rightArmIsChecked == false){
                                rightArmIsChecked = true;
                            }else{
                                rightArmIsChecked = false;
                            }

                            mBlue.sendMessage("4");
                        } else if (txtText.getText().equals("left leg")) {
                            i = 6;
                            if(!leftLeg.isChecked()) {
                                ll = ll + 1;
                                db.execSQL("UPDATE bodyparts SET bodypart='" + "left leg" + "',used='" + ll +
                                        "' WHERE id='" + i + "'");
                            }
                            leftLeg.toggle();
                            if(leftLegIsChecked == false){
                                leftLegIsChecked = true;
                            }else{
                                leftLegIsChecked = false;
                            }

                            mBlue.sendMessage("6");
                        } else if (txtText.getText().equals("right leg")) {
                            i = 7;
                            if(!rightLeg.isChecked()) {
                                rl = rl + 1;
                                db.execSQL("UPDATE bodyparts SET bodypart='" + "right leg" + "',used='" + rl +
                                        "' WHERE id='" + i + "'");
                            }
                            rightLeg.toggle();
                            if(rightLegIsChecked == false){
                                rightLegIsChecked = true;
                            }else{
                                rightLegIsChecked = false;
                            }

                            mBlue.sendMessage("5");
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
        try {
            db.delete("bodyparts", null, null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if(!mBlue.Connect()) {
                mBlue.Connect();
                mBlue.sendMessage("2");
                mBlue.Disconnect("FireFly-11AF");
            }else{
                mBlue.sendMessage("2");
                mBlue.Disconnect("FireFly-11AF");
            }
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
}
