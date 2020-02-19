package com.zaber.helpseeking;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class StartingMenu extends Activity {
	
	ImageView Call;
	ImageView Save;
	ImageButton Add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_menu);    
        Call= (ImageView)findViewById(R.id.CallButton);
        Save= (ImageView)findViewById(R.id.SaveButton);
     
        Call.setOnClickListener(
        		new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						  Intent ourIntent=new Intent(StartingMenu.this,CallPlace.class);
						  ourIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						  startActivity(ourIntent);
					}
				}
        );
        
        Save.setOnClickListener(
        		new View.OnClickListener() {					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub	
						  Intent ourIntent=new Intent(StartingMenu.this,SaveByHelper.class);
						  ourIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						  startActivity(ourIntent);
					}
				}
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.starting_menu, menu);
        return true;
    }
}
