package com.example.mid;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.MediaController;
import android.widget.VideoView;

public class Video extends Activity implements OnCompletionListener,OnTouchListener{
	VideoView videoView ;
	MediaController mediaController;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animation);
		videoView =(VideoView)findViewById(R.id.videoView1);
		videoView.setOnTouchListener(this);
		videoView.setOnCompletionListener(this);
		
		mediaController= new MediaController(this);
		
        logo();
		
    }
	public void logo(){
		
	    mediaController.setAnchorView(videoView);            
	    videoView.setMediaController(mediaController);	            
	    videoView.requestFocus();
	    videoView.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.drawable.maple)); 
        videoView.start();
      
	}
	
	
	@Override
    public void onCompletion(MediaPlayer mp) {
		finish();
		Intent intent = new Intent();//if time out,jump to slides
		intent.setClass(Video.this, Slides.class);
		startActivity(intent);
        
    }
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	
}