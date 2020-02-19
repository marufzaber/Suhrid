package com.zaber.helpseeking;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper{

	//All static variable
	
	// Logcat tag
    private static final String LOG = DatabaseHandler.class.getName();
	
	// Database version
	private static final int DATABASE_VERSION = 1;
	
	// Database Name
    private static final String DATABASE_NAME = "contactsManager";
 
    // Table names
    private static final String TABLE_HELPER= "helper";
    private static final String TABLE_CONTACT= "contact";
    private static final String TABLE_RECENT_CALL= "recent_call";
 
    // Helper Table Columns names
    private static final String HELPER_ID = "id";
    private static final String HELPER_NAME = "name";
    private static final String HELPER_PH_NO = "phone_number";
    
    // Contact Table Columns names
    private static final String CONTACT_ID = "id";
    private static final String CONTACT_NAME = "name";
    private static final String CONTACT_PH_NO = "phone_number";
    private static final String CONTACT_CON_NAME = "contact_name";
    private static final String CONTACT_CON_PH_NO = "contact_number";
	
    // Recent Call Table Columns names
    private static final String RECENT_CALL_ID = "id";
    private static final String RECENT_CALL_NAME = "name";
    private static final String RECENT_CALL_PH_NO = "phone_number";
    private static final String RECENT_CALL_NUMBER = "recent_number";
    private static final String RECENT_CALL_TYPE = "type";
    
    // Table create statements
    
    // Helper table create statement
    private static final String CREATE_TABLE_HELPER = "CREATE TABLE "
            + TABLE_HELPER + " ( " + HELPER_ID + " INTEGER PRIMARY KEY, " + HELPER_NAME
            + " TEXT, " + HELPER_PH_NO + " TEXT" + ")";
    
    // Contact table create statement
    private static final String CREATE_TABLE_CONTACT = "CREATE TABLE "
            + TABLE_CONTACT + "(" + CONTACT_ID + " INTEGER PRIMARY KEY," + CONTACT_NAME
            + " TEXT," + CONTACT_PH_NO + " TEXT," + CONTACT_CON_NAME + " TEXT," 
            + CONTACT_CON_PH_NO + " TEXT" + ")";
    
    // Recent Call table create statement
    private static final String CREATE_TABLE_RECENT_CALL = "CREATE TABLE "
            + TABLE_RECENT_CALL + "(" + RECENT_CALL_ID + " INTEGER PRIMARY KEY," + RECENT_CALL_NAME
            + " TEXT," + RECENT_CALL_PH_NO + " TEXT," + RECENT_CALL_NUMBER + " TEXT," 
            + RECENT_CALL_TYPE + " TEXT" + ")";
    
    
	// Constructor
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		//Creating required tables
		db.execSQL(CREATE_TABLE_HELPER);
		db.execSQL(CREATE_TABLE_CONTACT);
		db.execSQL(CREATE_TABLE_RECENT_CALL);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
		// on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_HELPER);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_CONTACT);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_RECENT_CALL);
 
        // create new tables
        onCreate(db);
		
	}
	
	public void dropAllDatabase(SQLiteDatabase db)
	{
		 db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_HELPER);
	        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_CONTACT);
	        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_RECENT_CALL);
	}
	/**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
	
	// ------------------------ "helper" table methods ----------------//
	
	// check helper name
	
	Helper checkHelperName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        String selectQuery = "SELECT  * FROM " + TABLE_HELPER + " WHERE "
                + HELPER_NAME + " = \"" + name + "\"";
 
        Log.e(LOG, selectQuery);
 
        Cursor cursor = db.rawQuery(selectQuery, null);
        //System.out.println(cursor.toString());
        if (cursor.getCount() != 0)
        {
        	//System.out.println(cursor.getCount());
        	//System.out.println("Curser...");
            cursor.moveToFirst();
            //System.out.println(cursor.getCount());
	        Helper helper = new Helper(Integer.parseInt(cursor.getString(0)),
	                cursor.getString(1), cursor.getString(2));
	        // return helper
	        cursor.close();
	        db.close(); // Closing database connection
	        return helper;
        }
        cursor.close();
        db.close();
        return null;
    }
    
    // check helper number
	
    Helper checkHelperNumber(String number) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        String selectQuery = "SELECT  * FROM " + TABLE_HELPER + " WHERE "
                + HELPER_PH_NO + " = \"" + number + "\"";
 
        Log.e(LOG, selectQuery);
 
        Cursor cursor = db.rawQuery(selectQuery, null);
        //System.out.println(cursor.toString());
        if (cursor.getCount() != 0)
        {
        	//System.out.println(cursor.getCount());
        	//System.out.println("Curser...");
            cursor.moveToFirst();
            //System.out.println(cursor.getCount());
	        Helper helper = new Helper(Integer.parseInt(cursor.getString(0)),
	                cursor.getString(1), cursor.getString(2));
	        // return helper
	        cursor.close();
	        db.close(); // Closing database connection
	        return helper;
        }
        cursor.close();
        db.close();
        return null;
    }
	
	// Adding new helper
    void addHelper(Helper helper) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(HELPER_NAME, helper.getName()); // HELPER Name
        values.put(HELPER_PH_NO, helper.getPhoneNumber()); // HELPER Phone
 
        // Inserting Row
        db.insert(TABLE_HELPER, null, values);
        
        db.close(); // Closing database connection
    }
    
    // Getting single helper
    Helper getHelper(String name, String phone_number) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        String selectQuery = "SELECT  * FROM " + TABLE_HELPER + " WHERE "
                + HELPER_NAME + " = \"" + name + "\" AND " + HELPER_PH_NO + " = \"" + phone_number + "\"";
 
        Log.e(LOG, selectQuery);
 
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount()!=0)
        {
            cursor.moveToFirst();
 
	        Helper helper = new Helper(Integer.parseInt(cursor.getString(0)),
	                cursor.getString(1), cursor.getString(2));
	        // return helper
	        cursor.close();
	        db.close(); // Closing database connection
	        return helper;
        }
        cursor.close();
        db.close();
        return null;
    }
    
    // Getting All Helper
    public List<Helper> getAllHelper() {
        List<Helper> helperList = new ArrayList<Helper>();
        
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_HELPER;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        
      if(cursor.getCount()!= 0)
      {
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Helper helper = new Helper();
                helper.setID(Integer.parseInt(cursor.getString(0)));
                helper.setName(cursor.getString(1));
                helper.setPhoneNumber(cursor.getString(2));
                // Adding contact to list
                helperList.add(helper);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close(); // Closing database connection
        // return contact list
        return helperList;
      }
      
      cursor.close();
      db.close();
      return null;
    }
 
    // Updating single helper
    public int updateHelper(Helper helper) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(HELPER_NAME, helper.getName());
        values.put(HELPER_PH_NO, helper.getPhoneNumber());
 
        // updating row
        return db.update(TABLE_HELPER, values, HELPER_NAME + " = ? AND " + HELPER_PH_NO + " = ?" ,
                new String[] { helper.getName(), helper.getPhoneNumber()});
    }
 
    // Deleting single Helper
    public void deleteHelper(Helper helper) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HELPER, HELPER_NAME + " = ? AND " + HELPER_PH_NO + " = ?",
                new String[] { helper.getName(), helper.getPhoneNumber()});
        db.close();
    }
 
 
    // Getting HELPER Count
    public int getHelperCount() {
        String countQuery = "SELECT  * FROM " + TABLE_HELPER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        db.close();
 
        // return count
        return cursor.getCount();
    }
    
    // ------------------------ "contact" table methods ----------------//
	
    // Adding new contact
    void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(CONTACT_NAME, contact.getName()); // Helper Name
        values.put(CONTACT_PH_NO, contact.getPhoneNumber()); // Helper Phone
        values.put(CONTACT_CON_NAME, contact.getContactName()); // Contact Name
        values.put(CONTACT_CON_PH_NO, contact.getContactNumber()); // Contact Number
 
        // Inserting Row
        db.insert(TABLE_CONTACT, null, values);
        db.close(); // Closing database connection
    }
    
    // Getting single contact
    Contact getContact(Contact con) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACT + " WHERE "
                + CONTACT_NAME + " = " + con.getName() + " AND " + CONTACT_PH_NO + " = " + con.getPhoneNumber()
                + " AND " + CONTACT_CON_NAME + " = " + con.getContactName() + " AND "
                + CONTACT_CON_PH_NO + " = " + con.getContactNumber();
 
        Log.e(LOG, selectQuery);
 
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount()!=0)
        {
            cursor.moveToFirst();
 
        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        // return contact
        cursor.close();
        db.close(); // Closing database connection
        return contact;
        }
        cursor.close();
        db.close();
        return null;
    }
    
    // Getting All Contact
    public List<Contact> getAllContact(String name, String phone_number) {
        List<Contact> contactList = new ArrayList<Contact>();
        
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACT + " WHERE "
                + CONTACT_NAME + " = \"" + name + "\" AND " + CONTACT_PH_NO + " = \"" + phone_number + "\"";
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
 
        // looping through all rows and adding to list
        if(cursor.getCount()!=0)
        {
	        if (cursor.moveToFirst()) {
	            do {
	            	Contact contact = new Contact();
	            	contact.setID(Integer.parseInt(cursor.getString(0)));
	            	contact.setName(cursor.getString(1));
	            	contact.setPhoneNumber(cursor.getString(2));
	            	contact.setContactName(cursor.getString(3));
	            	contact.setContactNumber(cursor.getString(4));
	                // Adding contact to list
	            	contactList.add(contact);
	            } while (cursor.moveToNext());
	        }
	        cursor.close();
	        db.close(); // Closing database connection
	        // return contact list
	        return contactList;
        }
        
       cursor.close();
       db.close();
       return null;
        
    }
    
    // Updating single contact
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(CONTACT_NAME, contact.getName());
        values.put(CONTACT_PH_NO, contact.getPhoneNumber());
        values.put(CONTACT_CON_NAME, contact.getContactName());
        values.put(CONTACT_CON_PH_NO, contact.getContactNumber());
 
        // updating row
        return db.update(TABLE_CONTACT, values,  CONTACT_NAME + " = ? AND " + CONTACT_PH_NO + " = ? AND "  
        + CONTACT_CON_NAME + " = ? AND "   + CONTACT_CON_PH_NO + " = ? " ,
                new String[] { contact.getName(), contact.getPhoneNumber(), contact.getContactName(), contact.getContactNumber()});
    }
    
    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACT, CONTACT_NAME + " = ? AND " + CONTACT_PH_NO + " = ? AND "  
        + CONTACT_CON_NAME + " = ? AND "   + CONTACT_CON_PH_NO + " = ? " ,
                new String[] { contact.getName(), contact.getPhoneNumber(), contact.getContactName(), contact.getContactNumber()});
        db.close();
    }
    
    // Deleting ALL contact
    public void deleteAllContact(String helperName, String helperNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACT, CONTACT_NAME + " = ? AND " + CONTACT_PH_NO + " = ? ",
                new String[] { "\""+helperName+"\"","\""+ helperNumber +"\""});
        db.close();
    }
 
 
    // Getting contact Count
    public int getContactCount(String name, String phone_number) {
    	String countQuery = "SELECT  * FROM " + TABLE_CONTACT + " WHERE "
                + CONTACT_NAME + " = \"" + name + "\" AND " + CONTACT_PH_NO + " = \"" + phone_number + "\"";
    	SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        db.close();
 
        // return count
        return cursor.getCount();
    }
    
    // ------------------------ "contact" table methods ----------------//
    
    // Adding new recent call
    void addRecentCall(RecentCall recentCall) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(RECENT_CALL_NAME, recentCall.getName()); // Helper Name
        values.put(RECENT_CALL_PH_NO, recentCall.getPhoneNumber()); // Helper Phone
        values.put(RECENT_CALL_NUMBER, recentCall.getRecentNumber()); // Recent number
        values.put(RECENT_CALL_TYPE, recentCall.getType()); // Recent number type
 
        // Inserting Row
        db.insert(TABLE_RECENT_CALL, null, values);
        db.close(); // Closing database connection
    }
    
    
    
    // Getting Recent Call
    public List<RecentCall> getRecentCall(String name, String phone_number, String type) {
        List<RecentCall> recentCallList = new ArrayList<RecentCall>();
        
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RECENT_CALL + " WHERE "
                + RECENT_CALL_NAME + " = \"" + name + "\" AND " + RECENT_CALL_PH_NO + " = \"" + phone_number
                +"\" AND "+ RECENT_CALL_TYPE + " = \"" + type + "\"";
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	RecentCall recentCall = new RecentCall();
            	recentCall.setID(Integer.parseInt(cursor.getString(0)));
            	recentCall.setName(cursor.getString(1));
            	recentCall.setPhoneNumber(cursor.getString(2));
            	recentCall.setRecentNumber(cursor.getString(3));
            	recentCall.setType(cursor.getString(4));
                // Adding contact to list
            	recentCallList.add(recentCall);
            } while (cursor.moveToNext());
        }
        db.close(); // Closing database connection
        // return contact list
        return recentCallList;
    }
    
    // Deleting ALL recent call
    public void deleteAllRecentCall(String name, String number) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECENT_CALL, RECENT_CALL_NAME + " = ? AND " + RECENT_CALL_PH_NO + " = ? ",
                new String[] { name, number});
        db.close();
    }
    
    
    }
