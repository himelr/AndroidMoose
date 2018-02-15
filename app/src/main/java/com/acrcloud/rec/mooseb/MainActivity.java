package com.acrcloud.rec.mooseb;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.acrcloud.rec.sdk.ACRCloudConfig;
import com.acrcloud.rec.sdk.ACRCloudClient;
import com.acrcloud.rec.sdk.IACRCloudListener;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements IACRCloudListener {
    //NOTE: You can also implement IACRCloudResultWithAudioListener, replace "onResult(String result)" with "onResult(ACRCloudResult result)"

	private ACRCloudClient mClient;
	private ACRCloudConfig mConfig;
	
	private TextView mVolume, mResult, tv_time;
	
	private boolean mProcessing = false;
	private boolean initState = false;
	
	private String path = "";

	private long startTime = 0;
	private long stopTime = 0;
	private final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 23;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		path = Environment.getExternalStorageDirectory().toString()
				+ "/acrcloud/model";
		
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
		}		
			
		mVolume = (TextView) findViewById(R.id.volume);
		mResult = (TextView) findViewById(R.id.result);
		tv_time = (TextView) findViewById(R.id.time);
		
		Button startBtn = (Button) findViewById(R.id.start);
		startBtn.setText(getResources().getString(R.string.start));

		Button stopBtn = (Button) findViewById(R.id.stop);
		stopBtn.setText(getResources().getString(R.string.stop));

		findViewById(R.id.stop).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						stop();
					}
				});
		
		Button cancelBtn = (Button) findViewById(R.id.cancel);
		cancelBtn.setText(getResources().getString(R.string.cancel));
		
		findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				start();
			}
		});
		
		findViewById(R.id.cancel).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						cancel();
					}
				});


        this.mConfig = new ACRCloudConfig();
        this.mConfig.acrcloudListener = this;
        
        // If you implement IACRCloudResultWithAudioListener and override "onResult(ACRCloudResult result)", you can get the Audio data.
        //this.mConfig.acrcloudResultWithAudioListener = this;
        
        this.mConfig.context = this;
        this.mConfig.host = "identify-eu-west-1.acrcloud.com";
        this.mConfig.dbPath = path; // offline db path, you can change it with other path which this app can access.
        this.mConfig.accessKey = "9b9af828f7503bd9b56c202090bf94ee";
        this.mConfig.accessSecret = "E45NF3Y3RdqDPEJB6NR2bx6mDQwXl7C5EsrvxuUW";
        this.mConfig.protocol = ACRCloudConfig.ACRCloudNetworkProtocol.PROTOCOL_HTTP; // PROTOCOL_HTTPS
        this.mConfig.reqMode = ACRCloudConfig.ACRCloudRecMode.REC_MODE_REMOTE;
        //this.mConfig.reqMode = ACRCloudConfig.ACRCloudRecMode.REC_MODE_LOCAL;
        //this.mConfig.reqMode = ACRCloudConfig.ACRCloudRecMode.REC_MODE_BOTH;

        this.mClient = new ACRCloudClient();
        // If reqMode is REC_MODE_LOCAL or REC_MODE_BOTH,
        // the function initWithConfig is used to load offline db, and it may cost long time.
        this.initState = this.mClient.initWithConfig(this.mConfig);
        if (this.initState) {
            this.mClient.startPreRecord(3000); //start prerecord, you can call "this.mClient.stopPreRecord()" to stop prerecord.
        }
	}

	
	public void start() {
		System.out.println("aaa22");
		if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.RECORD_AUDIO)
				!= PackageManager.PERMISSION_GRANTED) {
			System.out.println("aaa");


			// Should we show an explanation?
			if (ActivityCompat.shouldShowRequestPermissionRationale(this,
					Manifest.permission.RECORD_AUDIO)) {


				// Show an explanation to the user *asynchronously* -- don't block
				// this thread waiting for the user's response! After the user
				// sees the explanation, try again to request the permission.

			} else {

				// No explanation needed, we can request the permission.

				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.RECORD_AUDIO},
						MY_PERMISSIONS_REQUEST_RECORD_AUDIO);

				// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
				// app-defined int constant. The callback method gets the
				// result of the request.
			}
		}
		else {
			recognizeMusic();

		}
	}

	protected void stop() {
		if (mProcessing && this.mClient != null) {
			this.mClient.stopRecordToRecognize();
		}
		mProcessing = false;

		stopTime = System.currentTimeMillis();
	}
	
	protected void cancel() {
		if (mProcessing && this.mClient != null) {
			mProcessing = false;
			this.mClient.cancel();
			tv_time.setText("");
			mResult.setText("");
		} 		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    // Old api
	@Override
	public void onResult(String result) {	
		if (this.mClient != null) {
			this.mClient.cancel();
			mProcessing = false;
		} 
		
		String tres = "\n";
		
		try {
		    JSONObject j = new JSONObject(result);
		    JSONObject j1 = j.getJSONObject("status");
		    int j2 = j1.getInt("code");
		    if(j2 == 0){
		    	JSONObject metadata = j.getJSONObject("metadata");
		    	//
		    	if (metadata.has("humming")) {
		    		JSONArray hummings = metadata.getJSONArray("humming");
		    		for(int i=0; i<hummings.length(); i++) {
		    			JSONObject tt = (JSONObject) hummings.get(i); 
		    			String title = tt.getString("title");
		    			JSONArray artistt = tt.getJSONArray("artists");
		    			JSONObject art = (JSONObject) artistt.get(0);
		    			String artist = art.getString("name");
		    			tres = tres + (i+1) + ".  " + title + "\n";
		    		}
		    	}
		    	if (metadata.has("music")) {
		    		JSONArray musics = metadata.getJSONArray("music");
		    		for(int i=0; i<musics.length(); i++) {
		    			JSONObject tt = (JSONObject) musics.get(i); 
		    			String title = tt.getString("title");
		    			JSONArray artistt = tt.getJSONArray("artists");
		    			JSONObject art = (JSONObject) artistt.get(0);
		    			String artist = art.getString("name");
		    			tres = tres + (i+1) + ".  Title: " + title + "    Artist: " + artist + "\n";
		    		}
		    	}
		    	if (metadata.has("streams")) {
		    		JSONArray musics = metadata.getJSONArray("streams");
		    		for(int i=0; i<musics.length(); i++) {
		    			JSONObject tt = (JSONObject) musics.get(i); 
		    			String title = tt.getString("title");
		    			String channelId = tt.getString("channel_id");
		    			tres = tres + (i+1) + ".  Title: " + title + "    Channel Id: " + channelId + "\n";
		    		}
		    	}
		    	if (metadata.has("custom_files")) {
		    		JSONArray musics = metadata.getJSONArray("custom_files");
		    		for(int i=0; i<musics.length(); i++) {
		    			JSONObject tt = (JSONObject) musics.get(i); 
		    			String title = tt.getString("title");
		    			tres = tres + (i+1) + ".  Title: " + title + "\n";
		    		}
		    	}
		    	tres = tres + "\n\n" + result;
		    }else{
		    	tres = result;
		    }
		} catch (JSONException e) {
			tres = result;
		    e.printStackTrace();
		}
		
		mResult.setText(tres);	
	}

	@Override
	public void onVolumeChanged(double volume) {
		long time = (System.currentTimeMillis() - startTime) / 1000;
		mVolume.setText(getResources().getString(R.string.volume) + volume + "\n\nTime：" + time + " s");
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		switch (requestCode) {
			case MY_PERMISSIONS_REQUEST_RECORD_AUDIO: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					recognizeMusic();


					// permission was granted, yay! Do the
					// contacts-related task you need to do.

				} else {

					// permission denied, boo! Disable the
					// functionality that depends on this permission.
				}
				return;
			}

			// other 'case' lines to check for other
			// permissions this app might request.
		}
	}

	@Override  
    protected void onDestroy() {  
        super.onDestroy();  
        Log.e("MainActivity", "release");
        if (this.mClient != null) {
        	this.mClient.release();
        	this.initState = false;
        	this.mClient = null;
        }
    }
    private void recognizeMusic(){

		if (!this.initState) {
			Toast.makeText(this, "init error", Toast.LENGTH_SHORT).show();
			return;
		}

		if (!mProcessing) {
			mProcessing = true;
			mVolume.setText("");
			mResult.setText("");
			if (this.mClient == null || !this.mClient.startRecognize()) {
				mProcessing = false;
				mResult.setText("start error!");
			}
			startTime = System.currentTimeMillis();
		}

	}
}
