package com.example.martin.speechtotext;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import java.util.List;


public class Database extends ListActivity {

    private BodypartsDataSource body;

    public Database(){
        body = new BodypartsDataSource(this);
        body.open();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        List<Bodypart> values = body.getAllBodyparts();

        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        ArrayAdapter<Bodypart> adapter = new ArrayAdapter<Bodypart>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

    public void add(String bodypart){
        ArrayAdapter<Bodypart> adapter = (ArrayAdapter<Bodypart>) getListAdapter();

        // save the new comment to the database
        Bodypart newBodypart = body.createBodypart(bodypart);
        adapter.add(newBodypart);

        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_database, menu);
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
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
