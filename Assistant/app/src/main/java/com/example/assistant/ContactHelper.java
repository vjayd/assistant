package com.example.assistant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ContactHelper extends SQLiteOpenHelper {

	// database version
	private static final int database_VERSION = 1;
	// database name
	private static final String database_NAME = "Analysis";
	private static final String table_call = "call";
	private static final String call_num = "number";
	private static final String call_type = "type";
	private static final String call_day = "day";
	private static final String call_month="month";
    private static final String call_duration= "duration";

	private static final String[] COLUMNS = { call_num,call_type,call_day,call_month,call_duration};
	Map<Long,Long> hm=new LinkedHashMap<Long,Long>();

	public ContactHelper(Context context) {
		super(context, database_NAME, null, database_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// SQL statement to create book table
		String CREATE_CALL_TABLE = "CREATE TABLE call ( " + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "number TEXT, " + "type TEXT, "+
				 "day INTEGER, month TEXT,"+"duration NUMBER )";
		db.execSQL(CREATE_CALL_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// drop books table if already exists
		db.execSQL("DROP TABLE IF EXISTS call");
		this.onCreate(db);
	}

	public void createContact(Contact contact) {
		// get reference of the BookDB database
		SQLiteDatabase db = this.getWritableDatabase();

		// make values to be inserted
		ContentValues values = new ContentValues();
		values.put(call_num, contact.getNumber());
		values.put(call_type, contact.getType());
        values.put(call_day, contact.getDay());
		values.put(call_month, contact.getMonth());
		values.put(call_duration, contact.getDuration());
        System.out.print(contact.getNumber() + "" + contact.getType());
		// insert book
		db.insert(table_call, null, values);

		// close database transaction
		db.close();
	}

	public String getIncomming(){
		String query = "SELECT  number, SUM(duration) FROM call WHERE day='Aug' and type='Incoming' GROUP BY month ORDER BY month ASC";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			do {
			hm.put(Long.parseLong(cursor.getString(0)),Long.parseLong(cursor.getString(1)));
			}while (cursor.moveToNext());
		}

        return cursor.getString(0);

	}





	public List<Contact> getAllContacts() {
		List<Contact> contacts= new LinkedList<Contact>();
		String query = "SELECT  month, SUM(duration) FROM call WHERE day='Aug' GROUP BY month ORDER BY month DESC";


		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		// parse all results
		Contact contact = null;
		if (cursor.moveToFirst()) {
			do {

				contact= new Contact();

				contact.setday(cursor.getString(0));
				contact.setDuration(cursor.getString(1));

				contacts.add(contact);
			} while (cursor.moveToNext());
		}
		return contacts;
	}


}
