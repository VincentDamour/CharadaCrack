package com.example.administrateur.charadacrack;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DataBaseCharadaCrack extends SQLiteOpenHelper{
    private static final int VERSION_BDD = 1;
    private static final String NOM_BDD = "CharadaCrack.db";
    private static final String TABLE_CHARADE = "table_charade";
    private static final String COL_ID = "id";
    private static final String COL_CHARADETEXT = "charade_text";
    private static final String COL_REPONSE = "reponse";


    private static final String CREATE_BDD = "CREATE TABLE " + TABLE_CHARADE + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_CHARADETEXT + " TEXT NOT NULL, "
            + COL_REPONSE + " TEXT NOT NULL);";
    private Context charadeContext;

    public DataBaseCharadaCrack(Context context) {
        super(context, NOM_BDD, null, VERSION_BDD);
        charadeContext = context;
        Log.d("debut1", "debut1");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("debut2", "debut2");
        db.execSQL(CREATE_BDD);

        try{
            Log.d("debut2", "debut2");
            InputStream charades = charadeContext.getAssets().open("Charades.txt");
            InputStreamReader streamReader = new InputStreamReader(charades);
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] tableauCharades = line.split("|");
                Log.d(tableauCharades[0], tableauCharades[0]);
            }
            charades.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_CHARADE + ";");
        onCreate(db);
    }
}
