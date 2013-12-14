

package com.example.mid;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;


public class Animation extends Activity{
	private ProgressDialog pDialog;	
	ImageView logo;
	AnimationDrawable animtionDrawable;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation);
        logo=(ImageView)findViewById(R.id.imageView1);
        //downloadData();
	}
	public void onWindowFocusChanged(boolean hasFocus){
		super.onWindowFocusChanged(hasFocus);
		logo.setBackgroundResource(R.drawable.anima);
		animtionDrawable=(AnimationDrawable)logo.getBackground();
		animtionDrawable.start();
		Intent intent = new Intent();
   	 	intent.setClass(Animation.this, Main.class);
   	 	startActivity(intent);
	}
	/*public void downloadData() {
    	//pDialog = ProgressDialog.show(this, "Now Loading", "Please wait");
    	Thread t = new Thread() {
    		public void run() {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
					}
					finally{
						 pDialog.dismiss();
					}
					
				Intent intent = new Intent();
		    	 intent.setClass(Animation.this, Main.class);
		    	 startActivity(intent);
    		}
    	};
		t.start();
	}*/
}
