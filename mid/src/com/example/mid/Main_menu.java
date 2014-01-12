package com.example.mid;





import java.util.Calendar;

import android.content.Context;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;

import android.content.Intent;
import android.content.SharedPreferences;
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
	Button btn_slides,btn_apply,btn_gps,btn_chooice;
	TextView text;
	String temperature[],city[],weather[],udpate_time[];
	boolean chooice=true;
	int te_index=0,city_index=0,wea_index=0,udpate_time_index=0;
	Spinner spinner_temperature,spinner_city,spinner_udpate_time,spinner_weather; 
	
	//---------以上menu	
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
      
        
        ad_temperature.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ad_city.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ad_udpate_time.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      
        // Apply the adapter to the spinner
        
        spinner_temperature.setAdapter(ad_temperature); 
        spinner_city.setAdapter(ad_city);
        spinner_udpate_time.setAdapter(ad_udpate_time);
      
        
        
        spinner_temperature.setOnItemSelectedListener(spf);
        spinner_city.setOnItemSelectedListener(spf);
        spinner_udpate_time.setOnItemSelectedListener(spf);
     
        
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
					chooice=!chooice;
					mutex();
				}
				
				else if(id==R.id.button_apply){//to print
					sendshared();
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
         chooice=settings.getBoolean("chooice",true);//目前模式
         
    }
    
    public void mutex(){//spinner的互斥   	
    	if(chooice){//現在選擇城市		
			btn_chooice.setText("選擇City模式");
			spinner_city.setEnabled(false);					
			text.setText("GPS模式");	
			btn_gps.setEnabled(true);
		}else if(!chooice){//現在選擇gps		
			btn_chooice.setText("選擇GPS模式");
			spinner_city.setEnabled(true);				
			text.setText("City模式");
			btn_gps.setEnabled(false);
		}	
		
    }
   
    public void sendshared(){//保存上次menu的資料，傳給自己  和widget
    	SharedPreferences settings = getSharedPreferences ("menu_data",1);
    	//1才可與其他套件共用
        SharedPreferences.Editor PE = settings.edit();
        
        
        PE.putString("temperature",temperature[te_index]);
        PE.putString("city",city[city_index]);
        PE.putString("udpate_time",udpate_time[udpate_time_index]);      
        PE.putInt("te_index", te_index);
        PE.putInt("city_index",city_index);
        PE.putInt("udpate_time_index", udpate_time_index);
        
        PE.putBoolean("chooice",chooice);
       
        PE.commit();
    }
    
    protected void onPause(){//傳送資料和保存資料
    	super.onPause();
    	sendshared();
        
    }
    
    
    
   
	
	
	
}
