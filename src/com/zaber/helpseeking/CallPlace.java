package com.zaber.helpseeking;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;



import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CallPlace extends ListActivity{


	//String helpers[]={"+8801786265679","+8801711144144","+8801758936362"};
	ArrayList<String> helperListName = new ArrayList<String>();
	ArrayList<String> helperListNumber = new ArrayList<String>();
	ArrayList<String> helpers = new ArrayList<String>();
	public static boolean sIsSendMessage = false;

	static String smsad="";
	static String numberad="";
	ArrayList<String> contactNameNumber = new ArrayList<String>();
	public static boolean emptyFlag = false;
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(emptyFlag)
		{
			finish();
			emptyFlag = false;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if(sIsSendMessage)
		{
			System.out.println(numberad+ " " + smsad);
			if(smsad.length()<=160)
	        {
	            SmsManager smsManager = SmsManager.getDefault();
	    	    smsManager.sendTextMessage(numberad, null,smsad, null, null);
	        }
	        
	        else{
	           SmsManager smsManager = SmsManager.getDefault();
		       ArrayList<String> parts = smsManager.divideMessage(smsad);
		       smsManager.sendMultipartTextMessage(numberad, null, parts, null, null);
	        }
			sIsSendMessage = false;
		}
		
		
		
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customlist);
		
		getAllHelperList();	
		if(helpers.isEmpty())
		{
			emptyFlag = true;
			Intent ourIntent = new Intent(CallPlace.this, EmptyHelper.class);
			ourIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(ourIntent);
			
		}
		else
		{
			//setListAdapter(new ArrayAdapter<String>(CallPlace.this, android.R.layout.simple_list_item_1, helpers));
			
			setListAdapter(new MyAdapter<String>(CallPlace.this,android.R.layout.simple_list_item_1,R.id.tvTitle,helpers));
		}
	}
	
	void getAllHelperList(){
		
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());

		        List<Helper> con = db.getAllHelper();  
		        int i=0;
		        if(con!=null){
			        for (Helper cn : con) {
			            helperListName.add(cn.getName());
			            helperListNumber.add(cn.getPhoneNumber());
			            helpers.add(cn.getName()+'\n'+cn.getPhoneNumber());
			        }
		        }	       	        
		    	db.close();
	}
	
	private void call(String number) {
		String tell="tel:";
		tell+=number;
		sIsSendMessage = true;
		Intent callIntent = new Intent(Intent.ACTION_CALL);
	    callIntent.setData(Uri.parse(tell));
	    startActivity(callIntent);
	    
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		int i;
		
		String name=helperListName.get(position);
		String number=helperListNumber.get(position);
		
		
		String sms="@%^^";
        sms+=getContactListForHelper(name,number);
        numberad = number;
        smsad = sms;
        System.out.println(numberad+"   "+smsad);
        
       
        /*try{
        	Thread.sleep(10000);
        }catch(InterruptedException e){
        	e.printStackTrace();
        }*/
        
        
        call(number);
        
	}
	
	
	private void getAllContactFromDB(String helperName, String helperNumber)
	{
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
	 
        List<Contact> con = db.getAllContact(helperName, helperNumber);    
        if(con!=null){
	        for (Contact cn : con) {
	        	contactNameNumber.add(cn.getContactName()+"\n"+cn.getContactNumber());
	        }
        }
    	db.close();
	}
	
	private String getContactListForHelper(String helperName,String helperNumber)
	{
		ArrayList<String> conName = new ArrayList<String>();
		ArrayList<String> conNumber = new ArrayList<String>();
		
		String contactListName=new String();
        contactListName= new String();
        
        getAllContactFromDB(helperName, helperNumber);
		
        Cursor phones = this.getApplicationContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
		while (phones.moveToNext())
		{
		  String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
		  String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		  
		  conName.add(name);
		  conNumber.add(phoneNumber);
		  
		  if(!contactNameNumber.contains(name+"\n"+phoneNumber))
		  {
			  int i= phoneNumber.length();
			  String contact = new String();
			  contact+=phoneNumber.charAt(i-2);
			  contact+=phoneNumber.charAt(i-1);
			  contactListName+=name;
			  contactListName+=';';
			  contactListName+=contact;
			  contactListName+='$';
		  }
		}
		
		phones.close();
		
		updateContactListIntoDB(helperName,helperNumber);
		
		return contactListName;
	}
	
	/*************** UPDATE DATABASE **************************************************/
	private void updateContactListIntoDB(String helperName,String helperNumber)
	{
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		
		db.deleteAllContact(helperName, helperNumber);
		
		Cursor phones = this.getApplicationContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
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
	
	
	@SuppressWarnings("hiding")
	private class MyAdapter<String> extends ArrayAdapter<String>{

		public MyAdapter(Context context, int resource, int textViewResourceId,
				ArrayList<String> strings) {
			super(context, resource, textViewResourceId, strings);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View row = inflater.inflate(R.layout.row_items, parent, false);
			@SuppressWarnings("unchecked")
			
			ImageView iv = (ImageView) row.findViewById(R.id.ivTitle);
			TextView tvtitle = (TextView) row.findViewById(R.id.tvTitle);
			TextView tvsub = (TextView) row.findViewById(R.id.tvSub);
			
			
			tvtitle.setText((CharSequence) helperListName.get(position));
			tvsub.setText((CharSequence) helperListNumber.get(position));
			
			iv.setImageResource(R.drawable.avatar);
			
			
			
			return row;
		}
		
	}
	
	
	
	
}




