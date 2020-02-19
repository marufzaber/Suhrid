package com.zaber.helpseeking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AddHelper extends ListActivity{
	
	List<String> contactList;
	FileOutputStream fos;
    String contactListName;

	private void getContactList()
	{
		contactList=new Vector<String>();
        contactListName= new String();
		Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
		while (phones.moveToNext())
		{
		  String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
		  String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		  String contact = name+'\n'+phoneNumber;
		  contactList.add(contact);
		  contactListName+=name;
		  contactListName+=';';
		}
		phones.close();
	}
	
	
	private void sendContactList(String number){

		/************************************* SENDING MESSAGE ******************************/
		
		String str="$%^&*(";
		getContactList();
		str+=contactListName;
	    SmsManager smsManager = SmsManager.getDefault();
	    ArrayList<String> parts = smsManager.divideMessage(str);
	    smsManager.sendMultipartTextMessage(number, null, parts, null, null);
	    
	    /************************** SHOW SUCESS MESSAGE *******************************/
	    Context context = getApplicationContext();
	    CharSequence text = "Successfully Added";
	    int duration = Toast.LENGTH_SHORT;
	    Toast toast = Toast.makeText(context, text, duration);
	    toast.show();
	}
	
	private void sendSms(String number){
		
	    String message = "$%^&*(";
	    SmsManager smsManager = SmsManager.getDefault();
	    smsManager.sendTextMessage(number, null, message, null, null);

	    sendContactList(number);
	    /************************** SHOW SUCESS MESSAGE *******************************/
	    Context context = getApplicationContext();
	    CharSequence text = "Successfully Added";
	    int duration = Toast.LENGTH_SHORT;
	    Toast toast = Toast.makeText(context, text, duration);
	    toast.show();
	}
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getContactList();
		setListAdapter(new ArrayAdapter<String>(AddHelper.this, android.R.layout.simple_list_item_1, contactList));
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		String cheese= contactList.get(position);
		int i=cheese.indexOf('\n');
		String number=cheese.substring(i+1);
		sendSms(number);
		//sendContactList(number);
	}
}
