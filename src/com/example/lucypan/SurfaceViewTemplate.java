package com.example.lucypan;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/** 
 * @author lushiju 2016-3-4 上午10:53:26 
 * 类说明 
 */
public class SurfaceViewTemplate extends SurfaceView implements Callback, Runnable {
	private SurfaceHolder mHolder;
	private Canvas mCanvas;
	/**
	 * 用于绘制的线程
	 */
	private Thread t;
	/**
	 * 线程的控制开关
	 */
	private boolean isRunning = false;
	
	public SurfaceViewTemplate(Context context) {
		this(context, null);
	}


	public SurfaceViewTemplate(Context context, AttributeSet attrs) {
		super(context, attrs);
		//init holder  进行绘制和管理声明周期
		mHolder = getHolder();
		mHolder.addCallback(this);
		//获得焦点
		setFocusable(true);
		setFocusableInTouchMode(true);
		//设置常量
		setKeepScreenOn(true);
	}


	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}


	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		//开启线程
		isRunning = true;
		t = new Thread(this);
		t.start();
	}


	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// 关闭线程
		isRunning = false;
	}


	@Override
	public void run() {
		// 不断的进行绘制
		while(isRunning){
			draw();
		}
		
	}


	private void draw() {
		try {
			// 获取canvas
			mCanvas = mHolder.lockCanvas();
			if (mCanvas!=null) {
				//draw something
				
			}
		} catch (Exception e) {
			
		}finally{
			if (mCanvas!=null) {
				//释放canvas
				mHolder.unlockCanvasAndPost(mCanvas);
			}
		}
	}

}
