package com.example.mid;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class Animation extends Activity {
    /** Called when the activity is first created. */
	GifDecoderView view;

	@Override
   public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        InputStream stream = null;
        try {
            stream = getAssets().open("maple.gif");
        } catch (IOException e) {
            e.printStackTrace();
        }
        view = new GifDecoderView(this, stream);
        
        logo();
        
        
        
        		
    }
    public void logo(){
    	Thread t = new Thread() {
    		public void run() {
				try {
					setContentView(view);
					Thread.sleep(7250);
				} catch (InterruptedException e) {
				}
					finally{}
				
				Intent intent = new Intent();//if time out,jump to slides
	    		intent.setClass(Animation.this, Slides.class);
	    		startActivity(intent);
			}
		};
		t.start();
	}
}

