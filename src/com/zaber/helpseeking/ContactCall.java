package com.zaber.helpseeking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactCall extends Activity {

	String number;
	String name;
	
	
	
	public static boolean pauseac = false;
	public static boolean resumeac = false;
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(pauseac) 
		{
			pauseac = false;
			finish();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if(resumeac)
		{
			 pauseac = true;
			 resumeac = false;
			 Intent ourIntent=new Intent(ContactCall.this,StartingMenu.class);
			 ourIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 startActivity(ourIntent);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.callpermission);
		
		ImageView ivOk = (ImageView) findViewById(R.id.ivOk);
		ImageView ivClose = (ImageView) findViewById(R.id.ivClose);
		TextView tvtitle = (TextView) findViewById(R.id.tvTitle);
		TextView tvsub = (TextView) findViewById(R.id.tvSub);
		
		System.out.println("ON");
		
		Intent intents = getIntent();
		number = intents.getStringExtra("number");
		name = intents.getStringExtra("name");
		
		tvtitle.setText((CharSequence) name);
		tvsub.setText((CharSequence) number);
		
		ivOk.setOnClickListener(
        		new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						resumeac = true;
						call(number);
						
					}
				}
        );
        
        ivClose.setOnClickListener(
        		new View.OnClickListener() {					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub	
						  pauseac = true;
						  
						  Intent ourIntent=new Intent(ContactCall.this,StartingMenu.class);
						  ourIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						  startActivity(ourIntent);
						  
						  //finish();
						  
					}
				}
        );
		
		
	}
	
	/************************** PLACING A CALL *********************/
	private void call(String number) {
		String tell="tel:";
		tell+=number;
	    Intent callIntent = new Intent(Intent.ACTION_CALL);
	    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    callIntent.setData(Uri.parse(tell));
	    startActivity(callIntent);     
	}
	
	
	

}
