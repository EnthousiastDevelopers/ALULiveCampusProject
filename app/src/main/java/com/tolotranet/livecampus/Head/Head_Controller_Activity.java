package com.tolotranet.livecampus.Head;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.tolotranet.livecampus.R;

public class Head_Controller_Activity extends Activity {
	Button startService,stopService;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.head_service_controller);
		startService=(Button)findViewById(R.id.startService);
		stopService=(Button)findViewById(R.id.stopService);
		startService.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startService(new Intent(getApplication(), Head_Service_Activity.class));
				
			}
		});
		stopService.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				stopService(new Intent(getApplication(), Head_Service_Activity.class));
				Log.d("hellor", "trying to stop service in service controller");
			}
		});
	}
	@Override
	public void onPause() {
		super.onPause();
		//Log.d("hellor", "pause chatheadservicemainactivity");
		//startService(new Intent(getApplication(), Head_Service_Activity.class));
	}

	@Override
	public void onStop() {
		super.onStop();
		//Log.d("hellor", "stop chatheadservicemainactivity");
		//startService(new Intent(getApplication(), Head_Service_Activity.class));
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		//Log.d("hellor", "destroy chatheadservicemainactivity");
		//startService(new Intent(getApplication(), Head_Service_Activity.class));
	}
}
