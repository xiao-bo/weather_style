package com.example.mid;




import java.io.IOException;
import java.util.Calendar;

import android.content.Context;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



public class Main_menu extends Activity {
	Button btn_slides,btn_apply,btn_gps,btn_chooice;
	TextView text;
	String temperature[],city[],weather[],udpate_time[];
	String chooice="city",next="gps";
	int te_index=0,city_index=0,wea_index=0,udpate_time_index=0;
	Spinner spinner_temperature,spinner_city,spinner_udpate_time,spinner_weather; 
	
	//---------以上menu
	//---------gps
	private LocationManager lms;
	private boolean getService=false;
	Double longitude,latitude;
	private String bestProvider = LocationManager.GPS_PROVIDER;//best data provider
	String lon,lat;
	//---------
	private static final Integer[] image_array = {R.drawable.sunny,//change wallpaper
		R.drawable.wind,R.drawable.rain, };
	
	
	public void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);
        findView();
        
       
        
        btn_apply.setOnClickListener(btf);
        btn_slides.setOnClickListener(btf);   
        btn_gps.setOnClickListener(btf);
        btn_chooice.setOnClickListener(btf);
        
        ArrayAdapter<CharSequence> ad_temperature = ArrayAdapter.createFromResource(this,
                R.array.temperature_array, android.R.layout.simple_spinner_item);
        //create temperature_array
        ArrayAdapter<CharSequence> ad_city = ArrayAdapter.createFromResource(this,
                R.array.city_array, android.R.layout.simple_spinner_item);
        //create city_array
        ArrayAdapter<CharSequence> ad_udpate_time = ArrayAdapter.createFromResource(this,
                R.array.time_array, android.R.layout.simple_spinner_item);
       //create time_array
        ArrayAdapter<CharSequence> ad_weather = ArrayAdapter.createFromResource(this,
                R.array.weather_array, android.R.layout.simple_spinner_item);
       //create time_array
        
        ad_temperature.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ad_city.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ad_udpate_time.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ad_weather.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        
        spinner_temperature.setAdapter(ad_temperature); 
        spinner_city.setAdapter(ad_city);
        spinner_udpate_time.setAdapter(ad_udpate_time);
        spinner_weather.setAdapter(ad_weather); 
        
        
        spinner_temperature.setOnItemSelectedListener(spf);
        spinner_city.setOnItemSelectedListener(spf);
        spinner_udpate_time.setOnItemSelectedListener(spf);
        spinner_weather.setOnItemSelectedListener(spf);
        
        readdata();//讀保存的設定
        mutex();//spinner互斥存許
        
       
	}
	
	
	public void findView(){
		 btn_slides = (Button)findViewById(R.id.button_slides);
		 btn_gps = (Button)findViewById(R.id.button_gps);
	     btn_apply = (Button)findViewById(R.id.button_apply);
	     btn_chooice =(Button)findViewById(R.id.button_chooice);
	     text=(TextView)findViewById(R.id.textView1);
	     spinner_temperature = (Spinner) findViewById(R.id.spinner1);
	     spinner_city = (Spinner) findViewById(R.id.spinner2);
	     spinner_udpate_time = (Spinner) findViewById(R.id.spinner3);
	     spinner_weather = (Spinner) findViewById(R.id.spinner4);
	}
	
	private OnItemSelectedListener spf = new OnItemSelectedListener(){//spinner content
		public void onItemSelected(AdapterView<?> adapterView,View view,int position, long id){
			id=adapterView.getId();
				if(id==R.id.spinner1){
            		te_index=adapterView.getSelectedItemPosition();
            		temperature=getResources().getStringArray(R.array.temperature_array);
            		
				}else if(id==R.id.spinner2){
					city_index=adapterView.getSelectedItemPosition();
            		city=getResources().getStringArray(R.array.city_array);
				}else if(id==R.id.spinner3){
					udpate_time_index=adapterView.getSelectedItemPosition();
            		udpate_time=getResources().getStringArray(R.array.time_array);
				}else if(id==R.id.spinner4){
					wea_index=adapterView.getSelectedItemPosition();
					weather=getResources().getStringArray(R.array.weather_array);
				}
			}
		
		public void onNothingSelected(AdapterView<?> adapterView) {
		
		}
	};
        
           
	private OnClickListener btf = new OnClickListener() {
    	public void onClick(View v) {
    		
    		
    		int id=v.getId();
				if(id==R.id.button_gps){//to main
					Intent back_intent = new Intent();
					back_intent.setClass(Main_menu.this,GPS_print.class);
					startActivity(back_intent);
					
				}else if(id==R.id.button_slides){//to main
					Intent slides_intent = new Intent();
					slides_intent.setClass(Main_menu.this, Slides.class);
					startActivity(slides_intent);
					
				}else if(id==R.id.button_chooice){
					mutex();
				}
				
				else if(id==R.id.button_apply){//to print
					sendshared();
					
					changewallpaper();
					//location();
					
					Calendar cal = Calendar.getInstance();
					   
				    cal.add(Calendar.MILLISECOND, 1);
				   
				    Intent intent = new Intent("ALARM_UPDATE"); 
					PendingIntent pendingIntent = PendingIntent.getBroadcast(Main_menu.this, 0, 
							intent, PendingIntent.FLAG_ONE_SHOT);
					AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE); 
					alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
					
					
				}
    		
    	}
    };
    
    public void readdata(){//讀保存的設定資料
    	 SharedPreferences settings = getSharedPreferences("menu_data", 1);
         spinner_udpate_time.setSelection(settings.getInt("udpate_time_index", 0));
         spinner_temperature.setSelection(settings.getInt("te_index", 0));
         spinner_city.setSelection(settings.getInt("city_index", 0)); 
         spinner_weather.setSelection(settings.getInt("weather_index", 0));
         next=settings.getString("chooice","city");//目前模式
    }
    
    public void mutex(){//spinner的互斥
    	
    	if(next.equals("city")){//現在選擇城市
			
			btn_chooice.setText("選擇GPS定位");
			spinner_weather.setEnabled(false);//做GPS、城市、天氣互斥
			spinner_city.setEnabled(true);
			spinner_udpate_time.setEnabled(false);
			
			chooice="city";
			next="gps";
			
		}else if(next.equals("gps")){//現在選擇gps
			
			btn_chooice.setText("選擇天氣桌布");
			spinner_city.setEnabled(false);
			spinner_weather.setEnabled(false);
			spinner_udpate_time.setEnabled(true);
			
			chooice="gps";
			next="weather";
			
		}else if(next.equals("weather")){//現在選擇天氣
			btn_chooice.setText("選擇城市定位");
			spinner_city.setEnabled(false);
			spinner_weather.setEnabled(true);
			spinner_udpate_time.setEnabled(false);
			
			chooice="weather";
			next="city";
			
		}
		
		text.setText(chooice+"模式");
    }
   
    public void sendshared(){//保存上次menu的資料，傳給自己  和widget
    	SharedPreferences settings = getSharedPreferences ("menu_data",1);
    	//1才可與其他套件共用
        SharedPreferences.Editor PE = settings.edit();
        
        
        PE.putString("temperature",temperature[te_index]);
        PE.putString("city",city[city_index]);
        PE.putString("weather",weather[wea_index]);
        PE.putString("udpate_time",udpate_time[udpate_time_index]);
        PE.putString("chooice",chooice);
        
        PE.putString("latitude", lat);
        PE.putString("longitude", lon);
        
        PE.putInt("te_index", te_index);
        PE.putInt("city_index",city_index);
        PE.putInt("weather_index", wea_index);
        PE.putInt("udpate_time_index", udpate_time_index);
        
        
        PE.commit();
    }
    
    protected void onPause(){//傳送資料和保存資料
    	super.onPause();
    	sendshared();
        
    }
    
    
    
    public void changewallpaper(){//change wallpaper by code from widget
    	int weather_code=0;
    	SharedPreferences settings = getSharedPreferences("widget_data", 1);
    	weather_code=settings.getInt("weather_code", 0);
    	
    	if(chooice.equals("city")){    		
    		System.out.println("Menu_index"+weather_code);//test data 同步 
    	}else if(chooice.equals("weather")){
    		weather_code=wea_index;//spinner chooice weather index
    		
    	}
		WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        Resources res = getResources();
        Bitmap bitmap=BitmapFactory.decodeResource(res,image_array[weather_code]); 
		 
		
        try{
			wallpaperManager.setBitmap(bitmap);
		}catch (IOException e){
			e.printStackTrace();
		}
		
	}
    
    
   
   
    /*private void location(){//取得系統定位服務
		if(chooice.equals("gps")){
			LocationManager status = (LocationManager)
					(this.getSystemService(Context.LOCATION_SERVICE));
			//取得系統定位服務
			if(status.isProviderEnabled(LocationManager.GPS_PROVIDER)||
					status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){//如果GPS或網路定位開啟，呼叫此function更新位置
				getService=true;//確認開啟定位服務//
				locationServiceInitial();
			}else{
				Toast.makeText(this,"請開啟定位服務",Toast.LENGTH_SHORT).show();
			
			}
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
			lon=Double.toString(longitude);
			lat=Double.toString(latitude);
			
			sendshared();       
			text.setText(chooice+"模式"+""+longitude+"~~"+latitude);
			
		}else{
			Toast.makeText(this, "無法定位座標", Toast.LENGTH_LONG).show();
			
		}
	}
	
	@Override
	protected void onResume(){//更新頻率
		super.onResume();
		if(getService){
			lms.requestLocationUpdates(bestProvider, 1000,1,this);
			//服務提供者、更新頻率(1000毫秒=一秒)、最短距離、地點改變時呼叫物件
		}
	}
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onProviderDisabled(String provider) {
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
	
	
	*/
	
}
