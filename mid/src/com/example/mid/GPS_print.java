package com.example.mid;





import android.app.Activity;
import android.content.Context;


import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Bundle;


import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



public class GPS_print extends Activity implements LocationListener {
	Button bt_back;
	TextView text_temperature,text_city,longitude_txt,latitude_txt,weather_txt;
	private LocationManager lms;
	private boolean getService=false;
	private String bestProvider = LocationManager.GPS_PROVIDER;//best data provider
	Double longitude,latitude;
	String chooice="",woeid="",weather="",time;
	  
	class MyWeather{//weather constructor
		String city;
		String temperature;//當前溫度
		String humidity;//濕度
		String date;//當前時間
		public String toString(){
			String s;
			s = "city: " + city + "\n"
				+ "current temperature: "+ temperature+"\n"
				+ "humidity: "+humidity+"%\n"+date;		
				
			return s;
		}
	}
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_print);
		findView();
		
		getmenu_data();
		bt_back.setOnClickListener(toMenu);

		
	};
	
	
	
	public void findView(){
		bt_back=(Button)findViewById(R.id.button1);
		text_temperature=(TextView)findViewById(R.id.textView1);
		text_city=(TextView)findViewById(R.id.textView2);
		
		longitude_txt = (TextView)findViewById(R.id.longitude);
		latitude_txt = (TextView)findViewById(R.id.latitude);
		weather_txt=(TextView)findViewById(R.id.weather);
	}
	
	private OnClickListener toMenu = new OnClickListener(){//返回menu
		public void onClick(View v){
			finish();
			/*Intent intent = new Intent();
			intent.setClass(Menu_print.this,Main_menu.class);
			startActivity(intent);*/
		}
	};
	
	
	private void getmenu_data(){//秀出menu的內容
		
		
		
			LocationManager status = (LocationManager)
					(this.getSystemService(Context.LOCATION_SERVICE));
			//取得系統定位服務
			if(status.isProviderEnabled(LocationManager.GPS_PROVIDER)||
			status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){//如果GPS或網路定位開啟，呼叫此function更新位置
				getService=true;//確認開啟定位服務*/
				locationServiceInitial();
			}else{
				Toast.makeText(this,"請開啟定位服務",Toast.LENGTH_SHORT).show();
			}
		
		
	}
	
	private void locationServiceInitial(){//取得系統定位服務
		lms=(LocationManager)getSystemService(LOCATION_SERVICE);
		Location location=lms.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		getLocation(location);	
	}
	
	private void getLocation(Location location){//取得經度緯度
		if(location !=null){		
			longitude = location.getLongitude();
			latitude = location.getLatitude();
			
			longitude_txt.setText(String.valueOf(longitude));
			latitude_txt.setText(String.valueOf(latitude));	
			//getwoeid();
		}else{
			Toast.makeText(this, "無法定位座標", Toast.LENGTH_LONG).show();
			weather_txt.setText("請再重開一次頁面");
		}
	}
	
	protected void onResume(){//更新頻率
		super.onResume();
		if(getService){
			lms.requestLocationUpdates(bestProvider, 1000,1,this);
			//服務提供者、更新頻率(1000毫秒=一秒)、最短距離、地點改變時呼叫物件
		}
	}
	


	
    	
    
	protected void onPause(){
		super.onPause();
		if(getService){
			lms.removeUpdates(this);//離開頁面時停止更新
		}
	}
	
	protected boolean isRouteDisplayed(){
		return false;
	}
	
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	
}