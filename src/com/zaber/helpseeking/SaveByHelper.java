package com.zaber.helpseeking;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SaveByHelper extends ListActivity{
	
	ArrayList<String> helperListName = new ArrayList<String>();
	ArrayList<String> helperListNumber = new ArrayList<String>();
	ArrayList<String> helpers = new ArrayList<String>();
	
	static String smsad="";
	static String numberad="";
	public static boolean sIsSendMessage = false;
	
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
			sendCallLog(smsad, numberad);
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
			Intent ourIntent = new Intent(SaveByHelper.this, EmptyHelper.class);
			ourIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(ourIntent);
		}
		else
		{
			//setListAdapter(new ArrayAdapter<String>(CallPlace.this, android.R.layout.simple_list_item_1, helpers));
			
			setListAdapter(new MyAdapter<String>(SaveByHelper.this,android.R.layout.simple_list_item_1,R.id.tvTitle,helpers));
		}
	}
	
	void getAllHelperList(){
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
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
		String cheese= helpers.get(position);
		int i;
		i=cheese.indexOf('\n');
		String number=cheese.substring(i+1);
		
		String sms=getCallLog();
		
		smsad = sms;
		numberad= number;
		
		call(number);
	}
	
    private void sendCallLog(String sms, String number){
	    SmsManager smsManager = SmsManager.getDefault();
	    ArrayList<String> parts = smsManager.divideMessage(sms);
	    smsManager.sendMultipartTextMessage(number, null, parts, null, null);
	}
	
	private String getCallLog(){
		String missedCall="";
		String receivedCall="";
		String dialledCall="";		
		Cursor managedCursor = this.getApplicationContext().getContentResolver().query( CallLog.Calls.CONTENT_URI,null, null,null, null);
		int number = managedCursor.getColumnIndex( CallLog.Calls.NUMBER );
		int type = managedCursor.getColumnIndex( CallLog.Calls.TYPE );
		int missedCallCount,dialledCallCount,receivedCallCount;
		missedCallCount=dialledCallCount=receivedCallCount=0;
		while ( managedCursor.moveToNext() ) {
		      String phNumber = managedCursor.getString( number );
		      String callType = managedCursor.getString( type );
		      int dircode = Integer.parseInt( callType );
		      switch( dircode ) {
		         case CallLog.Calls.OUTGOING_TYPE:
		        	 if(dialledCallCount<=5){
		                  dialledCall+=phNumber;
		                  dialledCall+=';';
		                  dialledCallCount++;
		        	 }
		             break;
		         case CallLog.Calls.INCOMING_TYPE:
		        	 if(receivedCallCount<=5){
		                receivedCall+=phNumber;
		                receivedCall+=';';
		                receivedCallCount++;
		        	 }
		             break;
		         case CallLog.Calls.MISSED_TYPE:
		        	 if(missedCallCount<=5){
		                 missedCall+=phNumber;
		                 missedCall+=';';
		                 missedCallCount++;
		        	 }
		            break;
		       }	      
		}
		managedCursor.close();
		missedCall=missedCall.substring(0,(missedCall.length()-2));
		receivedCall=receivedCall.substring(0,(receivedCall.length()-2));
		dialledCall=dialledCall.substring(0,(dialledCall.length()-2));
		String callLog="@%^!"+missedCall+'$'+receivedCall+'$'+dialledCall+'$';
		return callLog;
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
