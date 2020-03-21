package com.sajjad.info;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.sajjad.info.Adapter.PersonInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    private String dbPath,tableName="infotbl";
    private Context context;
    private SQLiteDatabase sqLiteDatabase;
    private static String  dbName="infodb.db3";

    public SQLiteHelper(@Nullable Context context) {
        super(context, dbName, null, 1);

        this.context = context;
        dbPath = context.getApplicationInfo().dataDir + "/databases/";

        copyAndCheckExisting();
    }

    private void copyAndCheckExisting() {
        getReadableDatabase();
        File file = new File(dbPath);
        if (!file.exists()) {
            try {
                copyDbFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            close();
        }
    }

    private void copyDbFile() throws IOException {
        InputStream inputStream = context.getAssets().open(dbName);
        OutputStream outputStream = new FileOutputStream(dbPath + dbName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    List<PersonInfo> getAll() {
        sqLiteDatabase = getReadableDatabase();
        List<PersonInfo> temp = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(tableName, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            PersonInfo personModel =
                    new PersonInfo(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getBlob(3));
            temp.add(personModel);
        }
        cursor.close();
        sqLiteDatabase.close();
        return temp;
    }

    public List<PersonInfo> getLastThree() {
        sqLiteDatabase = getReadableDatabase();
        List<PersonInfo> temp = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(tableName, null, null, null, null, null, "id DESC","3");
        while (cursor.moveToNext()) {
            PersonInfo personModel =
                    new PersonInfo(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getBlob(3));
            temp.add(personModel);
        }
        cursor.close();
        sqLiteDatabase.close();
        return temp;
    }

    public PersonInfo getPersonAt(int id) {
        sqLiteDatabase = getReadableDatabase();
        PersonInfo personInfo;
        Cursor cursor = sqLiteDatabase.query(tableName, null, "id=" + id, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            personInfo = new PersonInfo(cursor.getInt(0), cursor.getString(1), cursor.getString(2)
                    , cursor.getBlob(3));
            return personInfo;
        }
        cursor.close();
        sqLiteDatabase.close();
        return null;
    }

    public void insertPerson(String personName,String personSaying,byte[]imageBytes) {
        sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("personsaying", personSaying);
        contentValues.put("personname", personName);
        contentValues.put("personpicture", imageBytes);
        sqLiteDatabase.insert(tableName, null, contentValues);
        sqLiteDatabase.close();
    }
    public void updatePerson(int personId,String personName,String personSaying,byte[]imageBytes) {
        sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("personsaying", personSaying);
        contentValues.put("personname", personName);
        contentValues.put("personpicture", imageBytes);
        sqLiteDatabase
                .update(tableName,contentValues,"id=?",new String[]{String.valueOf( personId)});
        sqLiteDatabase.close();
    }

    public void removePerson(int personId){
        sqLiteDatabase=getWritableDatabase();
        sqLiteDatabase.delete(tableName,"id=?",new String[]{String.valueOf( personId)});
        sqLiteDatabase.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.disableWriteAheadLogging();
    }
}
