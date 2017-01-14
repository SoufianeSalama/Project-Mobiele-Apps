package be.ucll.project2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Soufiane on 14/01/2017.
 */

public class testDB {

    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public testDB(Context context){
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    private void openReadableDB(){
        db = dbHelper.getReadableDatabase();
    }

    private void openWriteableDB(){
        db = dbHelper.getWritableDatabase();
    }

    private void closeDB(){
        if (db != null){
            db.close();
        }
    }

    public static final String DB_NAME = "tasklist.db";
    public static final int DB_VERSION = 1;
    public static final String TASK_TABLE = "taken";

    public static final String TASK_ID = "id";
    public static final int TASK_ID_COL = 0;

    public static final String TASK_NAME = "titel";
    public static final int TASK_NAME_COL = 1;

    public static final String TASK_NOTES = "beschrijving";
    public static final int TASK_NOTES_COL = 2;

    public static final String TASK_DATE = "datum";
    public static final int TASK_DATE_COL = 3;

    public static final String CREATE_TASK_TABLE =
            "CREATE TABLE " + TASK_TABLE + " (" +
                    TASK_ID         + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TASK_NAME       + " TEXT NOT NULL, " +
                    TASK_NOTES      + " TEXT, " +
                    TASK_DATE  + " TEXT); ";

    public static final String DROP_TASK_TABLE =
            "DROP TABLE IF EXISTS " + TASK_TABLE;




    public static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(CREATE_TASK_TABLE);

            db.execSQL("INSERT INTO taken VALUES (1, 'Project Mobiele Apps',    'Takenlijst toevoegen', '16-01-2017')");

            db.execSQL("INSERT INTO taken VALUES (2, 'Project Remise56',        'webshop toevoegen',    '13-01-2017' )");

            Log.d("Task List", "db aangemaakt en 2 rijen erin gezet " );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            Log.d("Task List", "Upgrading db from version " + oldVersion + " to " + newVersion);

            db.execSQL(DROP_TASK_TABLE);
            onCreate(db);
        }
    }

    public ArrayList<Taak> getTasks(){
        ArrayList<Taak> takenLijst = new ArrayList<Taak>();

        this.openReadableDB();
        //Cursor cursor = db.query(TASK_TABLE, new String[]{TASK_ID}, null, null, null, null, null);
        Cursor cursor = db.query(TASK_TABLE, null, null, null, null, null, null);


        //Cursor cursor = db.query(TASK_TABLE, null, where, whereArgs, null, null, null);

        //ArrayList<Taak> tasks = new ArrayList<Taak>();

        while(cursor.moveToNext()){
            takenLijst.add(getTaskFromCursor(cursor));
        }
        if (cursor != null){
            cursor.close();
        }
        this.closeDB();

        return takenLijst;
    }

    /*public Task getTask(int id){
        String where = TASK_ID + "= ?";
        String[] whereArgs = {Integer.toString(id)};

        this.openReadableDB();
        Cursor cursor = db.query(TASK_TABLE, null, where, whereArgs, null, null, null);

        cursor.moveToFirst();
        Task task = getTaskFromCursor(cursor);

        if (cursor != null){
            cursor.close();
        }
        this.closeDB();

        return task;
    }*/

    private static Taak getTaskFromCursor(Cursor cursor){
        if (cursor == null || cursor.getCount()==0){
            return null;
        }
        else{
            try{
                Taak taak = new Taak();
                taak.setTaakId(cursor.getInt(TASK_ID_COL));
                taak.setTaakTitel(cursor.getString(TASK_NAME_COL));
                taak.setTaakBeschrijving(cursor.getString(TASK_NOTES_COL));
                taak.setTaakDatum(cursor.getString(TASK_DATE_COL));
                return taak;
            }
            catch (Exception e){
                return null;
            }
        }
    }

    public long insertTask(Taak task){
        ContentValues cv = new ContentValues();
        cv.put(TASK_ID, task.getTaakId());
        cv.put(TASK_NAME, task.getTaakTitel());
        cv.put(TASK_NOTES, task.getTaakBeschrijving());
        cv.put(TASK_DATE, task.getTaakDatum());

        this.openWriteableDB();
        long rowID = db.insert(TASK_TABLE, null, cv);
        this.closeDB();

        return rowID;
    }

    public int updateTask(Taak task){
        ContentValues cv = new ContentValues();
        cv.put(TASK_ID, task.getTaakId());
        cv.put(TASK_NAME, task.getTaakTitel());
        cv.put(TASK_NOTES, task.getTaakBeschrijving());
        cv.put(TASK_DATE, task.getTaakDatum());

        String where = TASK_ID + "= ?";
        String[] whereArgs = {String.valueOf(task.getTaakId())};

        this.openWriteableDB();
        int rowCount = db.update(TASK_TABLE, cv, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    public int deleteTask(long id){
        String where = TASK_ID + "= ?";
        String[] whereArgs = {String.valueOf(id)};

        this.openWriteableDB();
        int rowCount = db.delete(TASK_TABLE, where, whereArgs);
        this.closeDB();

        return rowCount;

    }
}