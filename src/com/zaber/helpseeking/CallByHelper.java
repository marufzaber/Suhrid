package com.zaber.helpseeking;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


import android.content.BroadcastReceiver;
import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;

import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;

import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class CallByHelper extends BroadcastReceiver{
	
	ArrayList<String> helperListName = new ArrayList<String>();
	ArrayList<String> helperListNumber = new ArrayList<String>();
	ArrayList<String> helpers = new ArrayList<String>();

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		SmsMessage[] msgs;
		SmsMessage initial = null;
   	    Bundle pudsBundle = intent.getExtras();
   	    String fullMessage = "";
   	    
   	    getAllHelperList(context);
		
   	    if(pudsBundle!=null){
			 Object[] pdus = (Object[]) pudsBundle.get("pdus");
	         initial = SmsMessage.createFromPdu((byte[]) pdus[0]); 
	         
	         msgs = new SmsMessage[pdus.length];
	         for(int i=0;i< msgs.length ; i++){
		         msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
		         fullMessage = fullMessage + msgs[i].getMessageBody();	 
		     }
	        
          /********************************************** Helper Add ********************************/
	             
	         if(initial.getMessageBody().startsWith("***$")){
	             abortBroadcast();
	             if(helperListNumber.contains(initial.getOriginatingAddress()))
	             {
	            	   CharSequence text = "Helper Already Exists";
				     	int duration = Toast.LENGTH_SHORT;
				     	Toast toast = Toast.makeText(context, text, duration);
				     	toast.show();
	            	 
	             }
	             else
	             {
	             
	             String helperTag=fullMessage.substring((fullMessage.indexOf('$'))+1);
	             String helperNumber=initial.getOriginatingAddress();
	             
	             insertintoDBHlper(helperTag,helperNumber,context);
	             insertContactList(context,helperTag,helperNumber);
	             
	             String sms="@%^^";
	             sms+=getContactListForHelper(context);
	             
	             String number=initial.getOriginatingAddress();
	             sendContactList(sms, number, context);
	              CharSequence text = "Helper Added";
			      int duration = Toast.LENGTH_SHORT;
			     Toast toast = Toast.makeText(context, text, duration);
			     	toast.show();
	             }
	         }
	         
	         else
	         
	         {
		       
	        	 /*********************************************** Call by SMS *************************************/
		            
		       for(int k=0; k<helperListNumber.size();k++)
		       {
	
		         if(initial.getOriginatingAddress().contains(helperListNumber.get(k)) && initial.getMessageBody().startsWith("@%^#")){
		             abortBroadcast();		         
			         int i=fullMessage.indexOf('#');
			         int j=fullMessage.indexOf('$');
			         String name=fullMessage.substring(i+1,j);
			         String lastTwoDigit=fullMessage.substring(j+1);
			         String number = getNumber(name,lastTwoDigit,context);  
			         
			         Intent intent1 = new Intent(context, ContactCall.class);
			         intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			         intent1.putExtra("name", name);
			         intent1.putExtra("number", number);
			         context.startActivity(intent1);
			         //call(number,context);
			     }
		         
		         
		         /********************************************** Contact Save ******************************************/
		         if(initial.getOriginatingAddress().contains(helperListNumber.get(k))  && initial.getMessageBody().startsWith("@%^*")){
			        int i= fullMessage.indexOf('*');
			        int j=fullMessage.indexOf('$');
			        String name = fullMessage.substring(i+1,j);
			        String number=fullMessage.substring(j+1);
			        insert(name,number,context);  
			        abortBroadcast();
			        
			        CharSequence text = "Contact Saved";
			     	int duration = Toast.LENGTH_SHORT;
			     	Toast toast = Toast.makeText(context, text, duration);
			     	toast.show();
		         } 
		       }
   	    }
		}		
		
	}
	
	/*********************** GETTING ALL HELPER LIST *********************/
	
	void getAllHelperList(Context context){
		
		DatabaseHandler db = new DatabaseHandler(context);
		 
		List<Helper> con = db.getAllHelper();    
		if(con!=null){
			for (Helper cn : con) {
			   
			    helperListName.add(cn.getName());
			    helperListNumber.add(cn.getPhoneNumber());
			    helpers.add(cn.getName()+'\n'+cn.getPhoneNumber());
			 }
		  }		        
		 db.close();
	}
	
	/********************************************** IINSERTING HELPER INTO DB ****************************/
	
	void insertintoDBHlper(String helperTag,String helperNumber,Context context){
		DatabaseHandler db = new DatabaseHandler(context);
		Helper helper = new Helper(helperTag, helperNumber);
		db.addHelper(helper);
		db.close();
	}
	
	/********************************************** IINSERTING CONTACT NUMBER IN PHONEBOOK ****************************/
	private void insert(String name, String number,Context context)
	{
	    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
	    Builder builder = ContentProviderOperation.newInsert(RawContacts.CONTENT_URI);
	    builder.withValue(RawContacts.ACCOUNT_TYPE, null);
	    builder.withValue(RawContacts.ACCOUNT_NAME, null);
	    ops.add(builder.build());

	    // Name
	    builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
	    builder.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0);
	    builder.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
	    builder.withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,null);
	    builder.withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
	    ops.add(builder.build());

	    // Number
	    builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
	    builder.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0);
	    builder.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
	    builder.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number);
	    builder.withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME);
	    ops.add(builder.build());
	    // Add the new contact
	    ContentProviderResult[] res;
	    try{
	        res = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
	    }
	    catch (Exception e){  e.printStackTrace();}
	}
	
	/********************************* GETTING NUMBER FROM CALL MESSAGE *****************************/
	private String getNumber(String name,String lastTwoDigit,Context context){
        String phoneNumber="";
		Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
		while (phones.moveToNext())
		{
		  String names=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));          
		  if(names.equals(name)){
			  String Numbers = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			  if(Numbers.endsWith(lastTwoDigit))
				  phoneNumber=Numbers;
		  }
		}
		phones.close();
		return phoneNumber;
	}
	
	/*************************************** SENDING PHONEBOOK ******************************/
	private void sendContactList(String sms, String number,Context context){
		
		if(sms.length()<=160)
        {
            SmsManager smsManager = SmsManager.getDefault();
    	    smsManager.sendTextMessage(number, null,sms, null, null);
        }
		else{
	       SmsManager smsManager = SmsManager.getDefault();
	       ArrayList<String> parts = smsManager.divideMessage(sms);
	       smsManager.sendMultipartTextMessage(number, null, parts, null, null);
	       
		}
	}

	
	
	private String getContactListForHelper(Context context)
	{
		String contactListName=new String();
        contactListName= new String();
		Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
		while (phones.moveToNext())
		{
		  String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
		  String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		  int i= phoneNumber.length();
		  String contact = new String();
		  contact+=phoneNumber.charAt(i-2);
		  contact+=phoneNumber.charAt(i-1);
		  contactListName+=name;
		  contactListName+=';';
		  contactListName+=contact;
		  contactListName+='$';
		}
		phones.close();
		return contactListName;
	}
	
	
	/****************** INSERT PHONEBOOK IN DATABASE *******************************************/
	private void insertContactList(Context context, String helperName,String helperNumber)
	{
		DatabaseHandler db = new DatabaseHandler(context);
		Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
		while (phones.moveToNext())
		{
		  String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
		  String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		  Contact contact = new Contact(helperName, helperNumber, name, phoneNumber);
		  db.addContact(contact);
		}
		phones.close();
		
		
		    	db.close();
	}
	
	/************************** PLACING A CALL *********************/
	private void call(String number,Context context) {
		String tell="tel:";
		tell+=number;
	    Intent callIntent = new Intent(Intent.ACTION_CALL);
	    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    callIntent.setData(Uri.parse(tell));
	    context.startActivity(callIntent);     
	}	
}
