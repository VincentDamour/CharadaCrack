package com.example.administrateur.charadacrack;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Administrateur on 2015-02-27.
 */
public class ChoixNiveau extends ActionBarActivity {
    public static final String EXTRA_MESSAGE="nbcharades";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choixniveau);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
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

    public void onClickXCharades(View view)
    {
        int buttonID = view.getId();

        Button buttonClick = (Button) findViewById(buttonID);
        String btnpresse = buttonClick.getText().toString();
        Intent intent = new Intent(this, GameActivity.class);

        switch(btnpresse)
        {
            case "10 charades":intent.putExtra(EXTRA_MESSAGE,10) ;
                break;
            case "25 charades":intent.putExtra(EXTRA_MESSAGE,25);
                break;
            case "50 charades":intent.putExtra(EXTRA_MESSAGE,50);
                break;
            case "100 charades":intent.putExtra(EXTRA_MESSAGE,100);
                break;
            case "Toutes les charades": intent.putExtra(EXTRA_MESSAGE, 1000);
                break;
            default: onClickStatistique(view);
        }
        startActivity(intent);
    }

    public void onClickStatistique(View view)
    {
        //Intent intent = new Intent(this, ChoixNiveau.class);

        // startActivity(intent);
    }
}