package com.example.lucypan;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/** 
 * @author lushiju 2016-3-4 ����10:53:26 
 * ��˵�� 
 */
public class SurfaceViewTemplate extends SurfaceView implements Callback, Runnable {
	private SurfaceHolder mHolder;
	private Canvas mCanvas;
	/**
	 * ���ڻ��Ƶ��߳�
	 */
	private Thread t;
	/**
	 * �̵߳Ŀ��ƿ���
	 */
	private boolean isRunning = false;
	
	public SurfaceViewTemplate(Context context) {
		this(context, null);
	}


	public SurfaceViewTemplate(Context context, AttributeSet attrs) {
		super(context, attrs);
		//init holder  ���л��ƺ͹�����������
		mHolder = getHolder();
		mHolder.addCallback(this);
		//��ý���
		setFocusable(true);
		setFocusableInTouchMode(true);
		//���ó���
		setKeepScreenOn(true);
	}


	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}


	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		//�����߳�
		isRunning = true;
		t = new Thread(this);
		t.start();
	}


	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// �ر��߳�
		isRunning = false;
	}


	@Override
	public void run() {
		// ���ϵĽ��л���
		while(isRunning){
			draw();
		}
		
	}


	private void draw() {
		try {
			// ��ȡcanvas
			mCanvas = mHolder.lockCanvas();
			if (mCanvas!=null) {
				//draw something
				
			}
		} catch (Exception e) {
			
		}finally{
			if (mCanvas!=null) {
				//�ͷ�canvas
				mHolder.unlockCanvasAndPost(mCanvas);
			}
		}
	}

}
