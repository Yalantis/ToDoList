package com.yalantis.beamazingtoday.sample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.yalantis.beamazingtoday.interfaces.BatModel;

import java.util.ArrayList;
import java.util.List;

public class GoalDatabaseOpenHelper extends SQLiteOpenHelper {

  private static GoalDatabaseOpenHelper sInstance;

  // Database Info
  private static final String DATABASE_NAME = "GoalsDatabase";
  private static final int DATABASE_VERSION = 1;

  // Table Names
  private static final String TABLE_GOALS = "goals";

  // User Table Columns
  private static final String KEY_GOAL_ID = "id";
  private static final String KEY_GOAL_LABEL = "label";
  private static final String KEY_GOAL_CHECKED = "checked";

  private GoalDatabaseOpenHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  public static synchronized GoalDatabaseOpenHelper getInstance(Context context) {
    if (sInstance == null) {
      sInstance = new GoalDatabaseOpenHelper(context.getApplicationContext());
    }
    return sInstance;
  }

  @Override
  public void onConfigure(SQLiteDatabase db) {
    super.onConfigure(db);
    db.setForeignKeyConstraintsEnabled(true);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {

    String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_GOALS +
        "(" +
        KEY_GOAL_ID + " INTEGER PRIMARY KEY," +
        KEY_GOAL_LABEL + " TEXT," +
        KEY_GOAL_CHECKED + " BOOL DEFAULT 0" +
        ")";

    db.execSQL(CREATE_USERS_TABLE);

    List<BatModel> dummyGoals = new ArrayList<BatModel>() {{
        add(new Goal("first"));
        add(new Goal("second"));
        add(new Goal("third"));
        add(new Goal("fourth"));
        add(new Goal("fifth"));
        add(new Goal("sixth"));
        add(new Goal("seventh"));
        add(new Goal("eighth"));
        add(new Goal("ninth"));
        add(new Goal("tenth"));
    }};

    for (BatModel goal : dummyGoals) {
      ContentValues value = new ContentValues();
      value.put(KEY_GOAL_LABEL, goal.getText());
      value.put(KEY_GOAL_CHECKED, goal.isChecked());
      db.insert(TABLE_GOALS, null, value);
    }
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    if (oldVersion != newVersion) {
      // Will be implemented when needed
    }
  }

  public void addGoal(Goal goal) {
    // Create and/or open the database for writing
    SQLiteDatabase db = getWritableDatabase();
    db.beginTransaction();
    try {
      ContentValues values = new ContentValues();
      values.put(KEY_GOAL_LABEL, goal.getName());
      values.put(KEY_GOAL_CHECKED, goal.isChecked());
      db.insertOrThrow(TABLE_GOALS, null, values);

      db.setTransactionSuccessful();
    } catch (Exception e) {
      Log.d(this.toString(), "Error while trying to add goal to database.");
    } finally {
      db.endTransaction();
    }
  }

  public List<BatModel> getGoals() {
    List<BatModel> goalList = new ArrayList<>();

    SQLiteDatabase db = getReadableDatabase();
    Cursor cursor = db.query(TABLE_GOALS, null, null, null, null, null, null);
    try {
      if (cursor.moveToFirst()) {
        do {
          int id = cursor.getInt(cursor.getColumnIndex(KEY_GOAL_ID));
          String name = cursor.getString(cursor.getColumnIndex(KEY_GOAL_LABEL));
          boolean isChecked = cursor.getInt(cursor.getColumnIndex(KEY_GOAL_CHECKED)) != 0;
          System.err.println(isChecked);
          goalList.add(new Goal(id, name, isChecked));
        } while(cursor.moveToNext());
      }
    } catch (Exception e) {
      Log.d(this.toString(), "Error while trying to get goals from database.");
    } finally {
      if (cursor != null && !cursor.isClosed()) {
        cursor.close();
      }
    }
    return goalList;
  }

  public boolean removeGoal(Goal goal) {
    SQLiteDatabase db = getReadableDatabase();
    String whereClause = KEY_GOAL_ID + "=?";
    String[] whereArgs = new String[] { String.valueOf(goal.getId()) };
    return db.delete(TABLE_GOALS, whereClause, whereArgs) != 0;
  }

  public boolean updateCheckStatus(Goal goal) {
    SQLiteDatabase db = getReadableDatabase();
    String whereClause = KEY_GOAL_ID + "=" + goal.getId();
    ContentValues value = new ContentValues();
    value.put(KEY_GOAL_CHECKED, goal.isChecked());
    return db.update(TABLE_GOALS, value, whereClause, null) != 0;
  }
}
