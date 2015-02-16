package com.example.administrateur.charadacrack;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.util.Random;
import java.util.regex.Pattern;


public class GameActivity extends ActionBarActivity {
    public List<Charade> listeCharades = new ArrayList<Charade>();
    List<Integer> listeLettrePresse = new ArrayList<Integer>();
    public int numeroCharadeCourrante = 0;


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
                String[] tableauCharades = line.split(Pattern.quote("|"));
                Charade charade = new Charade(tableauCharades[0],tableauCharades[1],tableauCharades[2]);
                listeCharades.add(charade);
            }
            charades.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String[] firstCharadeArray = listeCharades.get(numeroCharadeCourrante).getCharadeText().split(Pattern.quote("$"));
        String firstCharadeText = "";
        for(int i=0; i<firstCharadeArray.length;i++){
            firstCharadeText += firstCharadeArray[i];
            firstCharadeText += "\n\n";
        }
        ((TextView)findViewById(R.id.Charades)).setText(firstCharadeText);
        AfficheLettreHasard();
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

    public void NextCharades()
    {
        if(numeroCharadeCourrante < listeCharades.size() - 1)
        {
            TextView textViewReponse = (TextView)findViewById(R.id.txtviewReponse);
            textViewReponse.setText("");

            numeroCharadeCourrante++;
            String[] CharadeArray = listeCharades.get(numeroCharadeCourrante).getCharadeText().split(Pattern.quote("$"));
            String CharadeText = "";
            for(int i=0; i<CharadeArray.length;i++){
                CharadeText += CharadeArray[i];
                CharadeText += "\n\n";
            }
            ((TextView)findViewById(R.id.Charades)).setText(CharadeText);
            AfficheLettreHasard();
        }
        else {
            PartieFini();
        }
    }

    public void ButtonLettreClick(View view){
        int buttonID = view.getId();
        String valideReponse="";
        CharSequence essaie="";
        Button buttonClick = (Button)findViewById(buttonID);
        CharSequence lettrePresse = buttonClick.getText();
        listeLettrePresse.add(buttonID);

        TextView textViewReponse = (TextView)findViewById(R.id.txtviewReponse);
        essaie=textViewReponse.getText();

        valideReponse=essaie.toString()+lettrePresse.toString();

        String reponseCourrante = listeCharades.get(numeroCharadeCourrante).getReponse();
        textViewReponse.setText(valideReponse);
        buttonClick.setVisibility(View.INVISIBLE);

        VerifieReponse(valideReponse);
    }

    private void VerifieReponse(String valideReponse)
    {
        String reponseCourrante = listeCharades.get(numeroCharadeCourrante).getReponse();

        if(valideReponse.equals(reponseCourrante))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setMessage("Bravo!!! Vous avez trouvé la bonne réponse.")
                    .setIcon(android.R.drawable.alert_dark_frame)
                    .setPositiveButton("Continuer", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            NextCharades();
                        }
                    })
                    .show();
        }
    }

    public void SupprimerReponse(View view)
    {
        TextView textViewReponse = (TextView)findViewById(R.id.txtviewReponse);
        textViewReponse.setText("");

        for(int i=0; i< listeLettrePresse.size(); i++)
        {
            int currentButtonID = listeLettrePresse.get(i);
            Button buttonToShow = (Button)findViewById(currentButtonID);
            buttonToShow.setVisibility(View.VISIBLE);
        }
    }

    public List<Character> ObtenirTableauLettreHasard(){
        char[] lettreAlphabet = {'A','B','C','D','E','E','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
        String reponse = listeCharades.get(numeroCharadeCourrante).getReponse();
        char[] tableauLettresReponse =  reponse.toCharArray();
        List<Character> listeLettresPourAfficher = new ArrayList<Character>();
        Random rand = new Random();

        for(char lettre : reponse.toCharArray()) {
            listeLettresPourAfficher.add(lettre);
        }

        for(int i = listeLettresPourAfficher.size(); i < 12; i++)
        {
            char randomChar = lettreAlphabet[rand.nextInt(26)];
            listeLettresPourAfficher.add(randomChar);
        }

        return listeLettresPourAfficher;
    }

    public void AfficheLettreHasard()
    {
        List<Character> listeLettresPourAfficher = ObtenirTableauLettreHasard();
        List<Integer> buttonsLettres = new ArrayList<Integer>();
        Random rand = new Random();

        GridLayout buttonLayout = (GridLayout)findViewById(R.id.gridLayoutForLetterButton);
        int i =0;
        Object controlCourrant;

        controlCourrant = buttonLayout.getChildAt(i);

        while(controlCourrant != null)
        {
            if(controlCourrant instanceof Button)
            {
                int positionLettre = rand.nextInt(listeLettresPourAfficher.size());
                char lettre = listeLettresPourAfficher.get(positionLettre);
                listeLettresPourAfficher.remove(positionLettre);

                ((Button) controlCourrant).setText(Character.toString(lettre));
                ((Button) controlCourrant).setVisibility(View.VISIBLE);
            }

            i++;
            controlCourrant = buttonLayout.getChildAt(i);
        }
    }

    private void PartieFini()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setMessage("Félicitation!!! Vous avez terminé le jeu, vous avez trouvé la bonne réponse pour toutes les charades.")
                .setIcon(android.R.drawable.alert_dark_frame)
                .setPositiveButton("Retour à l'accueil", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        RetourAccueil();
                    }
                })
                .show();
    }

    private void RetourAccueil()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
