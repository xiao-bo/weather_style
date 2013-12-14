package com.example.mid;




import java.util.Calendar;
import android.content.Context;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;



public class Main_menu extends Activity {
	Button btn_back,btn_next,btn_gps,btn_chooice;
	TextView text;
	String temperature[],city[],weather[],udpate_time[];
	String chooice="city",next="gps";
	int te_index=0,city_index=0,wea_index=0,udpate_time_index=0;
	Spinner spinner_temperature,spinner_city,spinner_udpate_time,spinner_weather; 
	
	
	public void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);
        findView();
        
       
        
        btn_next.setOnClickListener(btf);
        btn_back.setOnClickListener(btf);   
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
		 btn_back = (Button)findViewById(R.id.button_back);
	     btn_next = (Button)findViewById(R.id.button_next);
	     btn_chooice =(Button)findViewById(R.id.button_chooice);
	     text=(TextView)findViewById(R.id.textView1);
	     spinner_temperature = (Spinner) findViewById(R.id.spinner1);
	     spinner_city = (Spinner) findViewById(R.id.spinner2);
	     spinner_udpate_time = (Spinner) findViewById(R.id.spinner3);
	     spinner_weather = (Spinner) findViewById(R.id.spinner4);
	}
	
	private OnItemSelectedListener spf = new OnItemSelectedListener(){//spinner content
		public void onItemSelected(AdapterView<?> adapterView,View view,int position, long id){
			switch(adapterView.getId()){
				case R.id.spinner1:
            		te_index=adapterView.getSelectedItemPosition();
            		temperature=getResources().getStringArray(R.array.temperature_array);
            		break;
            		
				case R.id.spinner2:
					city_index=adapterView.getSelectedItemPosition();
            		city=getResources().getStringArray(R.array.city_array);
            		break;
				case R.id.spinner3:
					udpate_time_index=adapterView.getSelectedItemPosition();
            		udpate_time=getResources().getStringArray(R.array.time_array);
            		break;
				case R.id.spinner4:
					wea_index=adapterView.getSelectedItemPosition();
					weather=getResources().getStringArray(R.array.weather_array);
					break;
			}
		}
		public void onNothingSelected(AdapterView<?> adapterView) {
		
		}
	};
        
           
	private OnClickListener btf = new OnClickListener() {
    	public void onClick(View v) {
    		//finish();
    		switch(v.getId()){
				case R.id.button_back://to main
					/*Intent back_intent = new Intent();
					back_intent.setClass(Main_menu.this, Main.class);
					startActivity(back_intent);*/
					
					/*Intent intent=new Intent();
					intent.setClass(Main_menu.this,Wallpaper.class);
					startService(intent);*/
					
					Intent i=new Intent();
					if (Build.VERSION.SDK_INT > 15)
		            {
		                i.setAction(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
		                String pkg = Wallpaper.class.getPackage().getName();
		                String cls = Wallpaper.class.getCanonicalName();
		                i.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(pkg, cls));
		            }
		            else
		            {
		                i.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
		            }
		            startActivityForResult(i, 0);
					break;	
					
				case R.id.button_chooice:
					mutex();
					sendshared();
					break;
					
				case R.id.button_next://to print
					
					/*Intent next_intent = new Intent();
					next_intent.setClass(Main_menu.this, widget.class);
					Bundle bundle = new Bundle();				
					bundle.putString("chooice",chooice);
					bundle.putString("tem",temperature[te_index]);
					bundle.putString("city",city[city_index]);
					bundle.putString("weather",weather[wea_index]);
					bundle.putString("udpate_time",udpate_time[udpate_time_index]);
					
					next_intent.putExtras(bundle);
					startActivity(next_intent);
					*/
					Calendar cal = Calendar.getInstance();
					   
				    cal.add(Calendar.SECOND, 1);
				    
				    Intent intent = new Intent("ALARM_UPDATE"); 
					PendingIntent pendingIntent = PendingIntent.getBroadcast(Main_menu.this, 0, 
							intent, PendingIntent.FLAG_ONE_SHOT);
					AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE); 
					alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
					onPause();
					break;
    		}
    	}
    };
    
    public void readdata(){//讀保存的設定資料
    	 SharedPreferences settings = getSharedPreferences("menu_data", 1);
         spinner_udpate_time.setSelection(settings.getInt("udpate_time_index", 0));
         spinner_temperature.setSelection(settings.getInt("te_index", 0));
         spinner_city.setSelection(settings.getInt("city_index", 0)); 
         spinner_weather.setSelection(settings.getInt("weather_index", 0));
         next=settings.getString("next","weather");//目前模式
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
		
		text.setText(chooice+"模式"+"next:"+next);
    }
   
    public void sendshared(){
    	SharedPreferences settings = getSharedPreferences ("menu_data",1);
    	//1才可與其他套件共用
        SharedPreferences.Editor PE = settings.edit();
        
        
        PE.putString("temperature",temperature[te_index]);
        PE.putString("city",city[city_index]);
        PE.putString("weather",weather[wea_index]);
        PE.putString("udpate_time",udpate_time[udpate_time_index]);
        PE.putString("chooice",chooice);
        
        PE.putInt("te_index", te_index);
        PE.putInt("city_index",city_index);
        PE.putInt("weather_index", wea_index);
        PE.putInt("udpate_time_index", udpate_time_index);
       
        PE.commit();
    }
    
    protected void onPause(){//傳送資料和保存資料
    	super.onPause();
    	SharedPreferences settings = getSharedPreferences ("menu_data",1);
    	//1才可與其他套件共用
        SharedPreferences.Editor PE = settings.edit();
        
        
        PE.putString("temperature",temperature[te_index]);
        PE.putString("city",city[city_index]);
        PE.putString("weather",weather[wea_index]);
        PE.putString("udpate_time",udpate_time[udpate_time_index]);
        PE.putString("chooice",chooice);
        
        PE.putInt("te_index", te_index);
        PE.putInt("city_index",city_index);
        PE.putInt("weather_index", wea_index);
        PE.putInt("udpate_time_index", udpate_time_index);
       
        PE.commit();
        
    }
    
   
}
