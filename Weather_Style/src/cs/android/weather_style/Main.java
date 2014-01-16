package cs.android.weather_style;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

public class Main extends Activity implements ViewFactory {
	Button bt_right,bt_left,bt_dialog;
	AlertDialog alertDialog;//dialog
	private ImageSwitcher switcher;//switch
	protected static final int MENU_BUTTON_1 = Menu.FIRST;//menu
	protected static final int MENU_BUTTON_2 = Menu.FIRST + 1;
	private static final Integer[] image_array = { R.drawable.summer,R.drawable.sunny,
		R.drawable.wind,R.drawable.rain, };
	private static int index = 0; 	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);   
        setContentView(R.layout.main);
        
        bt_right = (Button)findViewById(R.id.next); //layout component
        bt_left = (Button)findViewById(R.id.forward);
        bt_dialog=(Button)findViewById(R.id.dialog);
        
        bt_right.setOnClickListener(toright); //button function
        bt_left.setOnClickListener(toleft);
        bt_dialog.setOnClickListener(createdialog);
        
        
        switcher = (ImageSwitcher) findViewById(R.id.image);  //imageSwitcher
        switcher.setFactory(this);  
        switcher.setImageResource(image_array[index]);  

    }
    private OnClickListener createdialog = new OnClickListener() {  //create dialog
		public void onClick(View v) { 	    		
			//finish();
			alertDialog =getAlertDialog("訊息","你確定要選擇這個佈景主題?");
			alertDialog.show();
    	}
    };
    private AlertDialog getAlertDialog(String title,String message){  //dialog object
    	//create builder
    	
    	Builder builder = new AlertDialog.Builder(Main.this);
    	builder.setTitle(title);//dialog title
    	builder.setMessage(message);//Dialog content
    	
    	//positive button
    	builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
    	@Override
    	
    		public void onClick(DialogInterface dialog, int which) {
    	      	
    	      	Toast.makeText(Main.this, "你選擇了此佈景主題", Toast.LENGTH_SHORT).show();
    	      	setWall();
    		}
    	});
    	builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    	@Override
    		public void onClick(DialogInterface dialog, int which) {
    	         
    	         Toast.makeText(Main.this, "你選擇取消", Toast.LENGTH_SHORT).show();
    	    }
    	});
    	
    	return builder.create();
    }
    public void setWall() {  //change WallpaperManager
    	
    	WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        Resources res = getResources();
        Bitmap bitmap=BitmapFactory.decodeResource(res,image_array[index]); 
        try{
			wallpaperManager.setBitmap(bitmap);
		}catch (IOException e){
			e.printStackTrace();
		}
    }
    
    private OnClickListener toleft = new OnClickListener() { //to left  
    	public void onClick(View v) {
    		//finish();
    		 index--;  
             if (index < 0){  
                 index = image_array.length - 1;  
             }  
             switcher.setImageResource(image_array[index]);  
    	}
    };
    private OnClickListener toright = new OnClickListener() {//to right
    	public void onClick(View v) {
    		//finish();
    		 index++;  
             if (index >= image_array.length){  
                  index = 0;  
             }
             switcher.setImageResource(image_array[index]);  
    	}
    };
   
   
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {  //menu layout
		// TODO Auto-generated method stub
    	
    	 menu.add(0, MENU_BUTTON_1, 0, "設定").setIcon(android.R.drawable.ic_menu_preferences);
    	 menu.add(0, MENU_BUTTON_2, 1, "離開").setIcon(android.R.drawable.ic_menu_set_as);
    	 
         return super.onCreateOptionsMenu(menu);
	}
    public boolean onOptionsItemSelected(MenuItem item) { // menu function
        // TODO Auto-generated method stub
        switch(item.getItemId()) {
        case MENU_BUTTON_1:
        	Intent intent = new Intent();
    		intent.setClass(Main.this, Main_menu.class);
    		startActivity(intent);
            //to menu
            break;
        case MENU_BUTTON_2:
        	System.exit(0);
            //exit
            break;
        default:
        	break;
        }
 
        return super.onOptionsItemSelected(item);
    }
	@Override
	public View makeView() {
		// TODO Auto-generated method stub
		 return new ImageView(this);  
		
	}

	

	
} 