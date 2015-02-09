package com.example.administrateur.charadacrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RequeteBDCharadaCrack {
    private static final String TABLE_CHARADE = "table_charade";
    private static final String COL_ID = "id";
    private static final int NUM_COL_ID = 0;
    private static final String COL_CHARADETEXT = "charade_text";
    private static final int NUM_COL_CHARADETEXT = 1;
    private static final String COL_REPONSE = "reponse";
    private static final int NUM_COL_REPONSE = 2;

    private SQLiteDatabase bdd;
    private DataBaseCharadaCrack DataBaseCharadaCrack;

    public RequeteBDCharadaCrack(Context context){
        DataBaseCharadaCrack = new DataBaseCharadaCrack(context);
    }

    public void open(){
        bdd = DataBaseCharadaCrack.getWritableDatabase();
    }
    public void close(){
        bdd.close();
    }

    public SQLiteDatabase getBDD(){
        return bdd;
    }

    public long insertCharade(Charade charade){
        ContentValues values = new ContentValues();

        values.put(COL_CHARADETEXT, charade.getCharadeText());
        values.put(COL_REPONSE, charade.getReponse());

        return bdd.insert(TABLE_CHARADE, null, values);
    }

    public int updateCharade(int id, Charade charade){
        ContentValues values = new ContentValues();

        values.put(COL_CHARADETEXT, charade.getCharadeText());
        values.put(COL_REPONSE, charade.getReponse());

        return bdd.update(TABLE_CHARADE, values, COL_ID + " = " +id, null);
    }

    public int removeCharadeWithID(int id){
        return bdd.delete(TABLE_CHARADE, COL_ID + " = " +id, null);
    }

    public Charade getCharadeWithID(int ID){
        Cursor c = bdd.query(TABLE_CHARADE, new String[]{COL_ID,COL_CHARADETEXT,COL_REPONSE},COL_ID + "LIKE \""+ ID + "\"", null , null, null, null);
        c.moveToFirst();
        Charade charade = new Charade();
        charade.setId(c.getString(NUM_COL_ID));
        charade.setCharadeText(c.getString(NUM_COL_CHARADETEXT));
        charade.setReponse(c.getString(NUM_COL_REPONSE));
        c.close();
        return charade;
    }

}
