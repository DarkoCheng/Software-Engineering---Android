/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package de.freewarepoint.whohasmystuff.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import de.freewarepoint.whohasmystuff.APICaller;
import de.freewarepoint.whohasmystuff.ContactHelper;
import de.freewarepoint.whohasmystuff.MainActivity;
import de.freewarepoint.whohasmystuff.LentObject;
import de.freewarepoint.whohasmystuff.ListLentObjects;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.freewarepoint.whohasmystuff.MainActivity.LOG_TAG;

public class OpenLendDbAdapter {

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_TYPE = "type";
    public static final String KEY_DATE = "date";
    public static final String KEY_MODIFICATION_DATE = "modification_date";
    public static final String KEY_PERSON = "person";
	public static final String KEY_PERSON_KEY = "person_key";
    public static final String KEY_BACK = "back";
    public static final String KEY_CALENDAR_ENTRY = "calendar_entry";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_SERVERID = "server_id";

    public static final String KEY_LENDER_PHONE_NUM = "lender_phone_num";
    public static final String KEY_LENDEE_PHONE_NUM = "lendee_phone_num";

    //General columns to obtain when calling 'cursor fetchX()' functions
    private static final String[] SQL_FETCH_COL_GENERAL = new String[] {KEY_ROWID,
                                                        KEY_SERVERID,
                                                        KEY_DESCRIPTION,
                                                        KEY_TYPE,
                                                        KEY_DATE,
                                                        KEY_MODIFICATION_DATE,
                                                        KEY_PERSON,
                                                        KEY_PERSON_KEY,
                                                        KEY_BACK,
                                                        KEY_CALENDAR_ENTRY,
                                                        KEY_LENDER_PHONE_NUM,
                                                        KEY_LENDEE_PHONE_NUM};

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private APICaller api = new APICaller();

    private static Map<Context, OpenLendDbAdapter> instances;

    /**
     * Database creation sql statement
     */
    private static final String LENTOBJECTS_DATABASE_CREATE =
        "create table lentobjects (" + KEY_ROWID + " integer primary key autoincrement, " + KEY_SERVERID + " integer not null, "
        + KEY_DESCRIPTION + " text not null, " + KEY_TYPE + " integer, " + KEY_DATE + " date not null, "
        + KEY_MODIFICATION_DATE + " date not null, " + KEY_PERSON + " text not null, " + KEY_PERSON_KEY + " text, "
		+ KEY_BACK + " integer not null, " + KEY_LENDER_PHONE_NUM + " text not null, " +KEY_LENDEE_PHONE_NUM + " text not null, "
        + KEY_CALENDAR_ENTRY + " text);";

    private static final String DATABASE_NAME = "data";
    private static final String LENTOBJECTS_DATABASE_TABLE = "lentobjects";
    static final int DATABASE_VERSION = 5;

    private static final String CREATE_CALENDAR_ENTRY_COLUMN =
            "ALTER TABLE " + LENTOBJECTS_DATABASE_TABLE + " ADD COLUMN " + KEY_CALENDAR_ENTRY + " text";
    
    private static final String CREATE_TYPE_COLUMN =
            "ALTER TABLE " + LENTOBJECTS_DATABASE_TABLE + " ADD COLUMN " + KEY_TYPE + " integer";

    private static final String CREATE_MODIFICATION_DATE_COLUMN =
            "ALTER TABLE " + LENTOBJECTS_DATABASE_TABLE + " ADD COLUMN " + KEY_MODIFICATION_DATE + " date";

    private static final String COPY_DATES =
            "UPDATE " + LENTOBJECTS_DATABASE_TABLE + " SET " + KEY_MODIFICATION_DATE + " = " + KEY_DATE;



    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        final Context context;

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(LENTOBJECTS_DATABASE_CREATE);
            } catch (SQLException e) {
                Log.println(Log.DEBUG,"Database Creation Error",e.getMessage());
            }

            SharedPreferences preferences =
                    context.getSharedPreferences(ListLentObjects.class.getSimpleName(), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(MainActivity.FIRST_START, true);
            editor.commit();
        }

        public void createWithoutExampleData(SQLiteDatabase db) {
            db.execSQL(LENTOBJECTS_DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i(LOG_TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);

            if (oldVersion < 2) {
                db.execSQL(CREATE_CALENDAR_ENTRY_COLUMN);
            }

            if (oldVersion < 3) {
                db.execSQL(CREATE_TYPE_COLUMN);
            }

            if (oldVersion < 4) {
                db.execSQL(CREATE_MODIFICATION_DATE_COLUMN);
                db.execSQL(COPY_DATES);
            }
            if (oldVersion != 5 && newVersion == 5)
            {
                db.execSQL("DROP TABLE IF EXISTS lentobjects");
                db.execSQL(LENTOBJECTS_DATABASE_CREATE);
            }

        }
    }

    public static synchronized OpenLendDbAdapter getInstance(Context ctx) {
        if (instances == null) {
            instances = new HashMap<Context, OpenLendDbAdapter>();
        }

        OpenLendDbAdapter instance;

        if (!instances.containsKey(ctx)) {
            instance = new OpenLendDbAdapter(ctx);
            instances.put(ctx, instance);
        }
        else {
            instance = instances.get(ctx);
        }

        return instance;
    }

    private OpenLendDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public OpenLendDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public void clearDatabase() {
        mDb.execSQL("DROP TABLE IF EXISTS lentobjects");
        mDbHelper.createWithoutExampleData(mDb);
    }

    public long createLentObject(LentObject lentObject) {
        ContentValues initialValues = new ContentValues();
        //Add an item's data to the a bundle
        initialValues.put(KEY_SERVERID, lentObject.id);
        initialValues.put(KEY_DESCRIPTION, lentObject.description);
        initialValues.put(KEY_LENDER_PHONE_NUM, lentObject.lenderPhoneNum);
        initialValues.put(KEY_LENDEE_PHONE_NUM, lentObject.lendeePhoneNum);
        initialValues.put(KEY_TYPE, lentObject.type);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        initialValues.put(KEY_DATE, dateFormat.format(lentObject.date));
        initialValues.put(KEY_MODIFICATION_DATE, dateFormat.format(lentObject.modificationDate));
        initialValues.put(KEY_PERSON, lentObject.personName);
		initialValues.put(KEY_PERSON_KEY, lentObject.personKey);
        initialValues.put(KEY_BACK, lentObject.returned);
        if (lentObject.calendarEventURI != null) {
            initialValues.put(KEY_CALENDAR_ENTRY, lentObject.calendarEventURI.toString());
        }

        //Attempt to insert new values into database
        long rowID = -1;
        rowID = mDb.insert(LENTOBJECTS_DATABASE_TABLE, null, initialValues);
        if (rowID == -1)
        {
            Log.println(Log.DEBUG,"Error","Error Inserting data");
            return  rowID;
        }

        //Query the databse for the lender's/lendee's phone numbers.If phone numbers were inserted, they should be retrieved.
        Cursor c;
        ContentResolver cr;
        c = mDb.query(LENTOBJECTS_DATABASE_TABLE, new String[] {KEY_LENDEE_PHONE_NUM,KEY_LENDER_PHONE_NUM}, null, null, null, null, KEY_DATE + " ASC");
        if(c.moveToFirst())
         {
             Log.println(Log.DEBUG, "Lender_Phone:", c.getString(c.getColumnIndex(KEY_LENDER_PHONE_NUM)));
             Log.println(Log.DEBUG, "Lendee_Phone:", c.getString(c.getColumnIndex(KEY_LENDEE_PHONE_NUM)));
             Log.println(Log.DEBUG, "", " ");
             while(c.moveToNext())
             {
                 Log.println(Log.DEBUG, "Lender_Phone:", c.getString(c.getColumnIndex(KEY_LENDER_PHONE_NUM)));
                 Log.println(Log.DEBUG, "Lendee_Phone:", c.getString(c.getColumnIndex(KEY_LENDEE_PHONE_NUM)));
             }
         }
         return (rowID);
    }

    public Item recordToItem(LentObject object) {
        String phoneNumber = object.lenderPhoneNum;
        phoneNumber = phoneNumber.replaceAll("[^\\d]", "");
        User user = new User(phoneNumber);
        return new Item(new Long(object.id), user, object.lendeePhoneNum.replaceAll("[^\\d]", ""), String.valueOf(object.type), object.description, object.date, object.modificationDate);
    }

    public LentObject itemToRecord(Item item, ContentResolver cr) {
        long cID;

        LentObject lentObject = new LentObject();
        lentObject.id = item.getId().intValue();
        lentObject.description = item.getDescription();
        lentObject.lenderPhoneNum = item.getLender().getPhoneNumber();
        lentObject.lendeePhoneNum = item.getLendee();
        lentObject.type = Integer.valueOf(item.getType());
        lentObject.date = item.getDateCreated();
        lentObject.modificationDate = item.getDateModified();

        cID = ContactHelper.getContactID(item.getLendee(),cr);
        lentObject.personName = ContactHelper.getContactName(cID,cr);
        lentObject.personKey = OpenLendDbAdapter.KEY_PERSON_KEY;

        return lentObject;
    }

    public boolean SyncDB(String phoneNumber, ContentResolver cr) {
        String JSON = api.MakeGetRequest(phoneNumber.replaceAll("[^\\d]", ""));

        if(!JSON.equals("")) {
            List<Item> itemList = api.ReadJSONArray(JSON);

            mDb.execSQL("DROP TABLE IF EXISTS lentobjects");
            mDb.execSQL(LENTOBJECTS_DATABASE_CREATE);

            for (Item item: itemList) {
                this.createLentObject(itemToRecord(item, cr));
            }

            return true;
        }

        return false;
    }

    public boolean deleteLentObject(long rowId) {
        return mDb.delete(LENTOBJECTS_DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

	public Cursor fetchAllObjects() {
    return mDb.query(LENTOBJECTS_DATABASE_TABLE, SQL_FETCH_COL_GENERAL, null, null, null, null, KEY_DATE + " ASC");
	}

    public Cursor fetchLentObjects() {
        return mDb.query(LENTOBJECTS_DATABASE_TABLE, SQL_FETCH_COL_GENERAL, KEY_BACK + "=0", null, null, null, KEY_DATE + " ASC");
    }

    public Cursor fetchLentObjectById(Long id) {
        return mDb.query(LENTOBJECTS_DATABASE_TABLE, SQL_FETCH_COL_GENERAL, KEY_ROWID + "=" + id, null, null, null, null, KEY_DATE + " ASC");
    }

    public Cursor fetchLentBooks() {
        return mDb.query(LENTOBJECTS_DATABASE_TABLE, SQL_FETCH_COL_GENERAL, KEY_BACK + "=0" + " AND " + KEY_TYPE + "=1", null, null, null, KEY_DATE + " ASC");
    }

	public Cursor fetchReturnedObjects() {
		return mDb.query(LENTOBJECTS_DATABASE_TABLE, SQL_FETCH_COL_GENERAL, KEY_BACK + "=1", null, null, null, KEY_DATE + " ASC");
	}
    //Added to conform to the ShowCategories Class, doesn't return anything
    public Cursor fetchNone() {
        return mDb.query(LENTOBJECTS_DATABASE_TABLE, SQL_FETCH_COL_GENERAL, KEY_BACK + "=2", null, null, null, KEY_DATE + " ASC");
    }

    public Cursor fetchType(int i) {    
        return mDb.query(LENTOBJECTS_DATABASE_TABLE, SQL_FETCH_COL_GENERAL,KEY_TYPE + "=" + i + " and " + KEY_BACK + "=0", null, null, null, KEY_DATE + " ASC");
}
    public Cursor fetchName(String  i) {
        return mDb.query(LENTOBJECTS_DATABASE_TABLE, SQL_FETCH_COL_GENERAL,KEY_LENDEE_PHONE_NUM + "=\"" + i + "\" and " + KEY_BACK + "=0", null, null, null, KEY_DATE + " ASC");
    }
    public Cursor fetchDates(String  i) {
        return mDb.query(LENTOBJECTS_DATABASE_TABLE, SQL_FETCH_COL_GENERAL,KEY_DATE + " LIKE \"" + i + "%\" and " + KEY_BACK + "=0", null, null, null, KEY_DATE + " ASC");
    }
    public Cursor sortByName() {
        return mDb.query(LENTOBJECTS_DATABASE_TABLE, SQL_FETCH_COL_GENERAL, KEY_BACK + "=0", null, null, null, KEY_PERSON + " COLLATE NOCASE ASC");
    }

    public Cursor sortByDate() {
        return mDb.query(LENTOBJECTS_DATABASE_TABLE, SQL_FETCH_COL_GENERAL, KEY_BACK + "=0", null, null, null, KEY_MODIFICATION_DATE + " ASC");
    }

    public Cursor sortByItem() {
        return mDb.query(LENTOBJECTS_DATABASE_TABLE, SQL_FETCH_COL_GENERAL, KEY_BACK + "=0", null, null, null, KEY_DESCRIPTION + " COLLATE NOCASE ASC");
    }

    public Cursor fetchSearchObjects(String q) {
        String sqlSrch = KEY_DESCRIPTION +" LIKE \'%"+ q + "%\' OR " + KEY_DATE +" LIKE \'%"+ q + "%\' OR " + KEY_PERSON + " LIKE \'%"+ q + "%\'";
        return mDb.query(LENTOBJECTS_DATABASE_TABLE,SQL_FETCH_COL_GENERAL,KEY_BACK + "=0 AND ("+sqlSrch + ")" ,null,null,null,KEY_DATE + " ASC");
    }

    public boolean updateLentObject(long rowId, LentObject lentObject) {
        ContentValues args = new ContentValues();
        args.put(KEY_DESCRIPTION, lentObject.description);
        args.put(KEY_LENDER_PHONE_NUM, lentObject.lenderPhoneNum);
        args.put(KEY_LENDEE_PHONE_NUM, lentObject.lendeePhoneNum);
        args.put(KEY_TYPE, lentObject.type);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        args.put(KEY_DATE, dateFormat.format(lentObject.date));
        args.put(KEY_PERSON, lentObject.personName);
		args.put(KEY_PERSON_KEY, lentObject.personKey);

		return updateLentObject(rowId, args);
    }

	public boolean markLentObjectAsReturned(long rowId) {
		ContentValues values = new ContentValues();
		values.put(KEY_BACK, true);
		return updateLentObject(rowId, values);
	}

    public boolean markReturnedObjectAsLentAgain(long rowId) {
        ContentValues values = new ContentValues();
        values.put(KEY_BACK, false);
        return updateLentObject(rowId, values);
    }

	private boolean updateLentObject(long rowId, ContentValues values) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        values.put(KEY_MODIFICATION_DATE, dateFormat.format(new Date()));
		return mDb.update(LENTOBJECTS_DATABASE_TABLE, values, KEY_ROWID + "=" + rowId, null) > 0;
	}
}
