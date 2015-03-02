package com.example.administrateur.charadacrack;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;


public class GameActivity extends ActionBarActivity {
    public List<Charade> listeCharades = new ArrayList<Charade>();
    List<Integer> listeLettrePresse = new ArrayList<Integer>();
    public int numeroCharadeCourrante = 0;
    public int totalniveau;
    public int niveaucourrant=1;
    Timer timer;
    int Minutes=2;
    int Secondes=0;
    int points = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);
        Intent intent=getIntent();

        int nbcharadesmax = intent.getIntExtra(ChoixNiveau.EXTRA_MESSAGE,0);
        totalniveau=nbcharadesmax;
        try{
            InputStream charades = getAssets().open("Charades.txt");
            InputStreamReader streamReader = new InputStreamReader(charades,"UTF-8");
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            String line;
            int nbcharades=0;
            while ((line = bufferedReader.readLine()) != null) {
                String[] tableauCharades = line.split(Pattern.quote("|"));
                Charade charade = new Charade(tableauCharades[0],tableauCharades[1],tableauCharades[2]);
                if(nbcharades<nbcharadesmax)
                {
                    listeCharades.add(charade);
                    nbcharades++;
                }
            }
            charades.close();
        }
        catch (IOException e) {
            e.printStackTrace() ;
        }
        LoadGame();
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
        long seed = System.nanoTime();
        Collections.shuffle(listeCharades, new Random(seed));

        String[] firstCharadeArray = listeCharades.get(numeroCharadeCourrante).getCharadeText().split(Pattern.quote("$"));
        String firstCharadeText = "";
        for(int i=0; i<firstCharadeArray.length;i++){
            firstCharadeText += firstCharadeArray[i];
            firstCharadeText += "\n\n";
        }
        ((TextView)findViewById(R.id.txtview_charade)).setText(firstCharadeText);
        AfficheLettreHasard();
        MAJInfoJoueur();
        startTimerJeux();
    }

    public void NextCharades()
    {


        if(listeCharades.isEmpty())
        {
            PartieFini();
        }
        else
        {
            Random rand = new Random();
            numeroCharadeCourrante=rand.nextInt(listeCharades.size());
            TextView txtview_Temps = (TextView)findViewById(R.id.textView_temps);
            TextView textViewReponse = (TextView)findViewById(R.id.txtviewReponse);

            textViewReponse.setText("");
            txtview_Temps.setText("2:00");
            listeLettrePresse = new ArrayList<Integer>();
            //numeroCharadeCourrante++;
            String[] CharadeArray = listeCharades.get(numeroCharadeCourrante).getCharadeText().split(Pattern.quote("$"));
            String CharadeText = "";
            for(int i=0; i<CharadeArray.length;i++){
                CharadeText += CharadeArray[i];
                CharadeText += "\n\n";
            }
            ((TextView)findViewById(R.id.txtview_charade)).setText(CharadeText);
            AfficheLettreHasard();
            MAJInfoJoueur();
            Minutes=2;
            Secondes=0;
            startTimerJeux();
        }
    }

    public void ButtonLettreClick(View view){
        int buttonID = view.getId();

        if(!listeLettrePresse.contains(buttonID)) {
            listeLettrePresse.add(buttonID);
            String valideReponse = "";
            CharSequence essaie = "";
            Button buttonClick = (Button) findViewById(buttonID);
            CharSequence lettrePresse = buttonClick.getText();

            TextView textViewReponse = (TextView) findViewById(R.id.txtviewReponse);
            essaie = textViewReponse.getText();

            valideReponse = essaie.toString() + lettrePresse.toString();

            textViewReponse.setText(valideReponse);
            buttonClick.setVisibility(View.INVISIBLE);

            VerifieReponse(valideReponse);
        }
    }

    private void VerifieReponse(String valideReponse)
    {
        String reponseCourrante = listeCharades.get(numeroCharadeCourrante).getReponse();

        if(valideReponse.equals(reponseCourrante))
        {
            timer.cancel();
            timer.purge();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setMessage("Bravo!!! Vous avez trouvé la bonne réponse.")
                    .setIcon(android.R.drawable.alert_dark_frame)
                    .setPositiveButton("Continuer", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            AjoutPoints();
                            SupprimeCharades();
                            NextCharades();
                        }
                    })
                    .show();
        }
    }

    public void SupprimeCharades()
    {
        listeCharades.remove(numeroCharadeCourrante);
        niveaucourrant++;
    }

    public void SupprimerReponse(View view)
    {
        if(listeLettrePresse.size() > 0) {
            TextView textViewReponse = (TextView) findViewById(R.id.txtviewReponse);
            String reponseCourrante = textViewReponse.getText().toString();
            int lastButtonID = listeLettrePresse.get(listeLettrePresse.size() - 1);

            listeLettrePresse.remove(listeLettrePresse.size() - 1);

            reponseCourrante = reponseCourrante.substring(0, reponseCourrante.length() - 1);
            textViewReponse.setText(reponseCourrante);

            Button buttonToShow = (Button) findViewById(lastButtonID);
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
            if(controlCourrant instanceof Button && ((Button) controlCourrant).getId() != R.id.btn_DeleteLetter)
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
                .setMessage("Félicitation!!! Vous avez terminé "+totalniveau+" charades avec un total de points de "+points+".")
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
        Intent intent = new Intent(this, ChoixNiveau.class);
        startActivity(intent);
    }

    private void MAJInfoJoueur()
    {
        TextView txtviewNiveau = (TextView)findViewById(R.id.textView_niveau);
        txtviewNiveau.setText("Niveau: "+ niveaucourrant + "/" + totalniveau);
    }

    private void startTimerJeux()
    {
        final TextView txtviewTemps = (TextView)findViewById(R.id.textView_temps);
        final Context c=this;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       if(Minutes>0 || Secondes>0)
                       {
                           if(Secondes==0)
                           {
                               Minutes--;
                               Secondes=59;
                           }
                           else
                           {
                               Secondes--;
                           }
                           if(Secondes<10)
                           {
                               txtviewTemps.setText(Minutes+":0"+Secondes);
                           }
                           else
                           {
                               txtviewTemps.setText(Minutes + ":" + Secondes);
                           }
                       }
                       else
                       {
                           TempsFini();
                       }
                    }
                });
            }
        }, 2000, 1000);
    }

    private void TempsFini()
    {
        timer.cancel();
        timer.purge();

        TextView txtview_Temps = (TextView)findViewById(R.id.textView_temps);
        txtview_Temps.setText(">>");
    }

    public void TempFiniClick(View view)
    {
        TextView txtview_Temps = (TextView)findViewById(R.id.textView_temps);

        if(txtview_Temps.getText() == ">>")
        {
            txtview_Temps.setText("2:00");
            NextCharades();
        }
    }

    private void AjoutPoints()
    {
        int pointsTemps = Minutes*500 + Secondes*10;
        points += 500 + pointsTemps;

        TextView txtviewPoint = (TextView)findViewById(R.id.textView_points);
        txtviewPoint.setText("Points: "+points);
    }
}
