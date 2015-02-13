package com.example.administrateur.charadacrack;

import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class GameActivity extends ActionBarActivity {
    public List<Charade> listeCharades = new ArrayList<Charade>();
    List<Integer> listeLettrePresse = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        try{
            InputStream charades = getAssets().open("Charades.txt");
            InputStreamReader streamReader = new InputStreamReader(charades,"UTF-8");
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Log.d("ligneé:",line);
                String[] tableauCharades = line.split(Pattern.quote("|"));
                Charade charade = new Charade(tableauCharades[0],tableauCharades[1],tableauCharades[2]);
                listeCharades.add(charade);
            }
            charades.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String[] firstCharadeArray = listeCharades.get(0).getCharadeText().split(Pattern.quote("$"));
        String firstCharadeText = "";
        for(int i=0; i<firstCharadeArray.length;i++){
            firstCharadeText += firstCharadeArray[i];
            firstCharadeText += "\n\n";
        }
        ((TextView)findViewById(R.id.Charades)).setText(firstCharadeText);
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

    public void LoadGame(){


    }

    public void ButtonLettreClick(View view){
        int buttonID = view.getId();
        Button buttonClick = (Button)findViewById(buttonID);
        CharSequence lettrePresse = buttonClick.getText();
        listeLettrePresse.add(buttonID);

        TextView textViewReponse = (TextView)findViewById(R.id.txtviewReponse);
        textViewReponse.append(lettrePresse);

        buttonClick.setVisibility(View.INVISIBLE);
    }

    public void SupprimerReponse(View view)
    {
        TextView textViewReponse = (TextView)findViewById(R.id.txtviewReponse);
        textViewReponse.setText("");

        for(int i=0; i<listeLettrePresse.size();i++)
        {
            int currentButtonID = listeLettrePresse.get(i);
            Button buttonToShow = (Button)findViewById(currentButtonID);
            buttonToShow.setVisibility(View.VISIBLE);
        }
    }
}
