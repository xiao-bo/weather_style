package com.example.mid;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * Android Live Wallpaper painting thread Archetype
 * <a href="http://my.oschina.net/arthor" target="_blank" rel="nofollow">@author</a> antoine vianey
 * GPL v3 : http://www.gnu.org/licenses/gpl-3.0.html
 */
public class LiveWallpaperPainting extends Thread {

	/** Reference to the View and the context */
	private SurfaceHolder surfaceHolder;
	private Context context;

	/** State */
	private boolean wait;
	private boolean run;

	/** Dimensions */
	private int width;
	private int height;

	/** Time tracking */
	private long previousTime;
	private long currentTime;

	public LiveWallpaperPainting(SurfaceHolder surfaceHolder,
			Context context) {
		// keep a reference of the context and the surface
		// the context is needed if you want to inflate
		// some resources from your livewallpaper .apk
		this.surfaceHolder = surfaceHolder;
		this.context = context;
		// don't animate until surface is created and displayed
		this.wait = true;
	}

	/**
	 * Pauses the live wallpaper animation
	 */
	public void pausePainting() {
		this.wait = true;
		synchronized(this) {
			this.notify();
		}
	}

	/**
	 * Resume the live wallpaper animation
	 */
	public void resumePainting() {
		this.wait = false;
		synchronized(this) {
			this.notify();
		}
	}

	/**
	 * Stop the live wallpaper animation
	 */
	public void stopPainting() {
		this.run = false;
		synchronized(this) {
			this.notify();
		}
	}

	@Override
		public void run() {
			this.run = true;
			Canvas c = null;
			while (run) {
				try {
					c = this.surfaceHolder.lockCanvas(null);
					synchronized (this.surfaceHolder) {
						currentTime = System.currentTimeMillis();
						updatePhysics();
						doDraw(c);
						previousTime = currentTime;
					}
				} finally {
					if (c != null) {
						this.surfaceHolder.unlockCanvasAndPost(c);
					}
				}
				// pause if no need to animate
				synchronized (this) {
					if (wait) {
						try {
							wait();
						} catch (Exception e) {}
					}
				}
			}
		}
	/**
	 * Invoke when the surface dimension change
	 */
	public void setSurfaceSize(int width, int height) {
		this.width = width;
		this.height = height;
		synchronized(this) {
			this.notify();
		}
	}

	/**
	 * Invoke while the screen is touched
	 */
	public void doTouchEvent(MotionEvent event) {
		// handle the event here
		// if there is something to animate
		// then wake up
		this.wait = false;
		synchronized(this) {
			notify();
		}
	}

	/**
	 * Do the actual drawing stuff
	 */
	private void doDraw(Canvas canvas) {}

	/**
	 * Update the animation, sprites or whatever.
	 * If there is nothing to animate set the wait
	 * attribute of the thread to true
	 */
	private void updatePhysics() {
		// if nothing was updated :
		// this.wait = true;
	}

}
