package com.example.mid;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import java.util.Calendar;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



import android.app.AlarmManager;
import android.app.PendingIntent;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


import android.os.AsyncTask;

import android.util.Log;


import android.widget.RemoteViews;




public class Widget extends AppWidgetProvider {
	//public static PendingIntent serviceIntent = null;
	
	public static PendingIntent serviceIntent = null;
	public Context context;
	
	///////BUZZ 12/15
	public static String weatherResult="";
	

	private static int index = 0; 	 
	Double longitude=0.0,latitude=0.0;
	String chooice="",woeid="",weather="",time="";
	int I_time=10;
	@Override
	public void onReceive(Context contextR, Intent intent) {
		context = contextR;
		super.onReceive(context, intent); 
		if("android.appwidget.action.APPWIDGET_UPDATE".equals(intent.getAction()) ||
				"ALARM_UPDATE".equals(intent.getAction())
				){ 
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context); 
			ComponentName thisWidget = new ComponentName(context.getPackageName(),
					Widget.class.getName());
			int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget); 
			
			onUpdate(context, appWidgetManager, appWidgetIds); 
		}
	} 
	
	@Override
    public void onUpdate(Context context,AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		RemoteViews updateViews = new RemoteViews( context.getPackageName(), 
				R.layout.widget_layout);
		
	    getmenu_data();
	    updateViews.setTextViewText(R.id.tv_widget_text, weatherResult );
	    System.out.println("Buzz Debug"+weatherResult );
	    appWidgetManager.updateAppWidget(appWidgetIds, updateViews);
	    
	    //////BUZZ 12/15
	    Calendar cal = Calendar.getInstance();		   
	    cal.add(Calendar.SECOND, 1000);
	    Log.e("time",""+I_time);
	    Intent intent = new Intent("ALARM_UPDATE"); 
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, 
				intent, PendingIntent.FLAG_ONE_SHOT);
		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE); 
		alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }
	
	
	
	  
	class MyWeather{//weather constructor
		String city;
		String temperature;//當前溫度
		String humidity;//濕度
		String date;//當前時間
		String text;//當前狀態
		String code;//當前狀態號碼
		public String toString(){
			String s;
			s = "temperature: "+ temperature+"\n"
					+ "humidity: "+humidity+"%";
			return s;
		}
	}

	public int timechange(String time){//把時間轉成數字
		int a=0,min=60,hour=3600;//min has 60s
		if(time.equals("30分鐘")){
			a=30*min*1000;
		}else if(time.equals("1小時")){
			a=hour*1000;
		}else if(time.equals("2小時")){
			a=hour*2000;
		}else if(time.equals("3小時")){
			a=hour*3000;
		}else if(time.equals("6小時")){
			a=hour*6000;
		}else if(time.equals("12小時")){
			a=hour*12000;
		}else if(time.equals("24小時")){
			a=hour*24000;
		}
		
		
		return a;
	}
	
	private void getmenu_data(){//秀出menu的內容
		SharedPreferences settings = context.getSharedPreferences("menu_data", 1);
		chooice=settings.getString("chooice", "gps");
		weather=settings.getString("weather", null);
		time=settings.getString("udpate_time","");
		
		I_time=timechange(time);
		System.out.println("time: "+I_time);
		if(chooice.equals("gps")){//做天氣、GPS、城市互斥
				longitude=Double.parseDouble(settings.getString("longitude","1"));
				latitude=Double.parseDouble(settings.getString("latitude","1"));
				getwoeid();
				
		}else if(chooice.equals("city")){
			String city=settings.getString("city", null);
			
			if(city.equals("台北")){
				woeid="2306179";
			}else if(city.equals("台中")){
				woeid="2306181";
			}else if(city.equals("高雄")){
				woeid="2306199";
			}
			new MyQueryYahooWeatherTask(woeid).execute();
			
		}else if(chooice.equals("weather")){//暫無功能
			
			
		}
		
	}
	
	
	
	public void getwoeid(){//取得woeid
		
		if (121.69>longitude&&longitude>121.28&&25.29>latitude&&latitude>24.89){
			//taipei
			woeid="2306179";
		}else if(120.84>longitude&&longitude>120.49&&24.35>latitude&&latitude>24.00){
			//Taichung
			woeid="2306181";
		}else if(120.85>longitude&&longitude>120.17&&23.44>latitude&&latitude>22.48){
			//Kaohsiung
			woeid="2306199";
		}		
		System.out.println("woeid: "+woeid);
		//return woeid;
		new MyQueryYahooWeatherTask(woeid).execute();
	}
	
	private String tempunitchange(String temperature){//將得到的溫度轉換成menu要求的溫度單位
		String temper="";
		SharedPreferences settings = context.getSharedPreferences("menu_data", 1);
		String temp_units=settings.getString("temperature", "℃");

		if(temp_units.equals("℃")){
			int temp=Integer.parseInt(temperature);
			temp=((temp-32)*5)/9;
			temper=String.valueOf(temp)+" ℃";
		}else{
			temper=temperature+" ℉";
		}
		Log.i("temper", ""+temper);
		return temper;
	}
	
	
	private class MyQueryYahooWeatherTask extends AsyncTask<Void, Void, Void>{

		String woeid;
		//String weatherResult;
		String weatherString;
		

		MyQueryYahooWeatherTask(String w){
			woeid = w;
		
		}
		
		@Override
		protected void onPostExecute(Void result) {//print result
			
			
			super.onPostExecute(result);
		}

		@Override
		protected Void doInBackground(Void... arg0) {//get source and assign to result
			weatherString = QueryYahooWeather();//get yahoo weather source from web
			Document weatherDoc = convertStringToDocument(weatherString);
			if(weatherDoc != null){
				weatherResult = parseWeather(weatherDoc).toString();
				//Have source form web change to data, and assign to result 
			}else{
				weatherResult = "Cannot convertStringToDocument!";
			}

			return null;
		}

		
		private String QueryYahooWeather(){//get yahoo weather data from web
			String qResult = "";
			String queryString = "http://weather.yahooapis.com/forecastrss?w=" + woeid;

			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(queryString);

			try {
				HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();

				if (httpEntity != null){
					InputStream inputStream = httpEntity.getContent();
					Reader in = new InputStreamReader(inputStream);
					BufferedReader bufferedreader = new BufferedReader(in);
					StringBuilder stringBuilder = new StringBuilder();

					String stringReadLine = null;

					while ((stringReadLine = bufferedreader.readLine()) != null) {
						stringBuilder.append(stringReadLine + "\n");  
					}

					qResult = stringBuilder.toString();  
				}  
			} catch (ClientProtocolException e) {
				e.printStackTrace(); 
			} catch (IOException e) {
				e.printStackTrace(); 
			}
			return qResult;   
		}

		private Document convertStringToDocument(String src){//convert to doc(別問我為什麼)
			Document dest = null;

			DocumentBuilderFactory dbFactory =DocumentBuilderFactory.newInstance();
			DocumentBuilder parser;

			try {
				parser = dbFactory.newDocumentBuilder();
				dest = parser.parse(new ByteArrayInputStream(src.getBytes())); 
			} catch (ParserConfigurationException e1) {
				e1.printStackTrace(); 
			} catch (SAXException e) {
				e.printStackTrace(); 
			} catch (IOException e) {
				e.printStackTrace(); 
			}

			return dest; 
		}

		private MyWeather parseWeather(Document srcDoc){//weather data

			MyWeather myWeather = new MyWeather();

			//<yweather:location city="New York"/>
			NodeList locationNodeList = srcDoc.getElementsByTagName("yweather:location");
			if(locationNodeList != null && locationNodeList.getLength() > 0){
				Node locationNode = locationNodeList.item(0);
				NamedNodeMap locNamedNodeMap = locationNode.getAttributes();
				myWeather.city = locNamedNodeMap.getNamedItem("city").getNodeValue().toString();		
			}else{
				myWeather.city = "EMPTY";
			}
			
			//yweather:condition text="Fair" temperature="60" date="Fri, 23 Mar 2012 8:49 pm EDT"
			NodeList conditionNodeList = srcDoc.getElementsByTagName("yweather:condition");
			if(conditionNodeList != null && conditionNodeList.getLength() > 0){
				Node conditionNode = conditionNodeList.item(0);
				NamedNodeMap conditionNamedNodeMap = conditionNode.getAttributes();
				myWeather.temperature = conditionNamedNodeMap.getNamedItem("temp").getNodeValue().toString();
				myWeather.date=conditionNamedNodeMap.getNamedItem("date").getNodeValue().toString();
				myWeather.code=conditionNamedNodeMap.getNamedItem("code").getNodeValue().toString();
				myWeather.text=conditionNamedNodeMap.getNamedItem("text").getNodeValue().toString();
				
				myWeather.temperature=tempunitchange(myWeather.temperature);//將menu要求的單位做轉換
				
				code_to_weather(myWeather.code);//12/15新增
				
				System.out.println("code"+myWeather.code);
				System.out.println("text"+myWeather.text);
			}else{
				myWeather.temperature="EMPTY";
				
			}

			//<yweather:atmosphere humidity;
			NodeList astNodeList = srcDoc.getElementsByTagName("yweather:atmosphere");
			if(astNodeList != null && astNodeList.getLength() > 0){
				Node astNode = astNodeList.item(0);
				NamedNodeMap astNamedNodeMap = astNode.getAttributes();
				myWeather.humidity = astNamedNodeMap.getNamedItem("humidity").getNodeValue().toString();
			
			}else{
				myWeather.humidity = "EMPTY";
				
			}			
			return myWeather; 
		}

	}

	public void code_to_weather(String code){//send CODE to menu 
		SharedPreferences settings = context.getSharedPreferences ("widget_data",1);
		int weather_code=0;
		index=Integer.parseInt(code);
		if(index>0&&index<18){
			weather_code=2;//雨天
		}else if(index>17&&index<31){
			weather_code=1;//陰天
		}else if(index==32){
			weather_code=0;//晴天
		}
		SharedPreferences.Editor PE = settings.edit();   
        PE.putInt("weather_code",weather_code);
        System.out.println("widget_weather:"+weather_code);
        PE.commit();
	}
	
	
	
}
