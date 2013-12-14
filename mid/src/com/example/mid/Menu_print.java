package com.example.mid;



import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

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

import android.app.Activity;
import android.content.Context;


import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;


import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



public class Menu_print extends Activity implements LocationListener {
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
		Bundle bundle=this.getIntent().getExtras();
		chooice=bundle.getString("chooice");
		weather=bundle.getString("weather");
		time=bundle.getString("udpate_time");
		
		if(chooice.equals("gps")){//做天氣、GPS、城市互斥
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
			
		}else if(chooice.equals("city")){
			String city=bundle.getString("city");
			
			if(city.equals("台北")){
				woeid="2306179";
			}else if(city.equals("台中")){
				woeid="2306181";
			}else if(city.equals("高雄")){
				woeid="2306199";
			}
			new MyQueryYahooWeatherTask(woeid).execute();
			
		}else if(chooice.equals("weather")){
			
			weather_txt.setText(weather);
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
			getwoeid();
		}else{
			Toast.makeText(this, "無法定位座標", Toast.LENGTH_LONG).show();
			weather_txt.setText("請再重開一次頁面");
		}
	}
	
	public void getwoeid(){//取得woeid
		
		if (121.69>longitude&&longitude>121.28&&25.29>latitude&&latitude>24.89){
			text_city.setText("taipei");
			woeid="2306179";
		}else if(120.84>longitude&&longitude>120.49&&24.35>latitude&&latitude>24.00){
			text_city.setText("Taichung");
			woeid="2306181";
		}else if(120.85>longitude&&longitude>120.17&&23.44>latitude&&latitude>22.48){
			text_city.setText("Kaohsiung");
			woeid="2306199";
		}		
		//return woeid;
		new MyQueryYahooWeatherTask(woeid).execute();
	}
	
	private String tempunitchange(String temperature){//將得到的溫度轉換成menu要求的溫度單位
		String temper="";

		Bundle bundle=this.getIntent().getExtras();
		String temp_units=bundle.getString("tem");
		
		if(temp_units.equals("℃")){
			int temp=Integer.parseInt(temperature);
			temp=((temp-32)*5)/9;
			temper=String.valueOf(temp)+" ℃";
		}else{
			temper=temperature+" ℉";
		}
		
		return temper;
	}
	
	
	private class MyQueryYahooWeatherTask extends AsyncTask<Void, Void, Void>{

		String woeid;
		String weatherResult;
		String weatherString;

		MyQueryYahooWeatherTask(String w){
			woeid = w;
		}
		
		@Override
		protected void onPostExecute(Void result) {//print result
			weather_txt.setText(weatherResult);
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
				
				myWeather.temperature=tempunitchange(myWeather.temperature);//將menu要求的單位做轉換
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