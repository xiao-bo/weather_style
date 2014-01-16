package cs.android.weather_style;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class Slides extends Activity{
	//ImageView slide;
	Button btn_slide,btn_logo;
	public void onCreate(Bundle saveInstanceState){
		super.onCreate(saveInstanceState);
		setContentView(R.layout.slides_layout);
		btn_slide=(Button)findViewById(R.id.button1);
		btn_slide.setOnClickListener(toMenu);
		btn_logo=(Button)findViewById(R.id.button2);
		btn_logo.setOnClickListener(toLogo);
		
	}
	private OnClickListener toMenu = new OnClickListener(){
		
		public void onClick(View v){
			 Intent intent = new Intent();
	    	 intent.setClass(Slides.this, Main_menu.class);
	    	 startActivity(intent);
		}
	};
	private OnClickListener toLogo = new OnClickListener(){
		
		public void onClick(View v){
			 Intent intent = new Intent();
	    	 intent.setClass(Slides.this, Video.class);
	    	 startActivity(intent);
		}
	};
}
