package hu.gyulbor.norbirontable.webservice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelperR extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final String DATABASE_NAME = "NodeBackup.db";
    private static final String NODES_TABLE_NAME = "nodes";
    private static final String NODES_COLUMN_TYPE = "type";
    private static final String NODES_COLUMN_POSITION_X = "x";
    private static final String NODES_COLUMN_POSITION_Y = "y";
    private static final String NODES_COLUMN_USER = "user";
    private static final String NODES_COLUMN_NODE = "node";

    public DBHelperR(Context context) {
        super(context, DATABASE_NAME, null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table nodes" +
                        "(type integer, x integer, y integer, user varchar, node long primary key unique)"
        );
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS nodes");
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean insertNode(int type, int x, int y, String user, long nodeID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("type", type);
        contentValues.put("x", x);
        contentValues.put("y", y);
        contentValues.put("user", user);
        contentValues.put("node", nodeID);
        db.insert("nodes", null, contentValues);
        db.close();
        return true;
    }

    public boolean updateNode(int type, int x, int y, String user, long nodeID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("type", type);
        contentValues.put("x", x);
        contentValues.put("y", y);
        contentValues.put("user", user);
        contentValues.put("node", nodeID);
        db.update("nodes", contentValues, "node = ? ", new String[]{Long.toString(nodeID)});
        db.close();
        return true;
    }

    public Integer deleteNode(long nodeID) {
        SQLiteDatabase db = this.getWritableDatabase();
        Integer result = db.delete("nodes",
                "node = ? ",
                new String[]{Long.toString(nodeID)});
        db.close();
        return result;
    }

    public int getType(long nodeID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select type from nodes where node = '" + nodeID + "'", null);

        int type = 111;

        if (cursor.moveToFirst()) {
           type = Integer.parseInt(cursor.getString(cursor.getColumnIndex(NODES_COLUMN_TYPE)));
        }

        cursor.close();
        db.close();

        return type;
    }

    public int getX(long nodeID) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select x from nodes where node = '" + nodeID + "'", null);

        cursor.moveToFirst();
        int x = Integer.parseInt(cursor.getString(cursor.getColumnIndex(NODES_COLUMN_POSITION_X)));

        cursor.close();
        db.close();

        return x;
    }

    public int getY(long nodeID) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select y from nodes where node = '" + nodeID + "'", null);

        cursor.moveToFirst();
        int y = Integer.parseInt(cursor.getString(cursor.getColumnIndex(NODES_COLUMN_POSITION_Y)));

        cursor.close();
        db.close();

        return y;
    }

    public String getUser(long nodeID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select user from nodes where node = '" + nodeID + "'", null);

        cursor.moveToFirst();
        String userID = (cursor.getString(cursor.getColumnIndex(NODES_COLUMN_USER)));

        cursor.close();
        db.close();

        return userID;
    }

    public ArrayList<Long> getNodeIDs() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select node from nodes", null);

        ArrayList<Long> nodes = new ArrayList<>();
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            nodes.add(cursor.getLong(cursor.getColumnIndex(NODES_COLUMN_NODE)));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

        return nodes;
    }

    public int countRows() {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = (int) DatabaseUtils.queryNumEntries(db, NODES_TABLE_NAME);
        db.close();
        return result;
    }
}