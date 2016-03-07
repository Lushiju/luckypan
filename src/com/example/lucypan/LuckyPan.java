package com.example.lucypan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/** 
 * @author lushiju 2016-3-4 ����10:53:26 
 * ��˵��  ����ת��
 */
public class LuckyPan extends SurfaceView implements Callback, Runnable {
	private SurfaceHolder mHolder;
	private Canvas mCanvas;
	/**
	 * ���ڻ��Ƶ��߳�
	 */
	private Thread t;
	/**
	 * �̵߳Ŀ��ƿ���
	 */
	private boolean isRunning;
	//�����齱����
	private String[] mStrs = new String[]{"�������","iPad","��ϲ����","iPhone","��װһ��","��ϲ����"};
	//�齱��ͼƬ
	private int[] mImgs = new int[]{R.drawable.p13,R.drawable.p13,R.drawable.p13,R.drawable.p13,R.drawable.p13,R.drawable.p13};
	//��ͼƬ��Ӧ��Bitmap����
	private Bitmap[] mImgsBitmap;
	//����ͼ
    private Bitmap mBgBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.p11);
	
	
	///�̿����ɫ
	private int[] mColors = new int[]{0xFFFFC300,0xFFF17E01,0xFFFFC300,0xFFF17E01,0xFFFFC300,0xFFF17E01};
	//���̿��Ӧ������
	private int mItemCount = 6;
	//�����̿�ķ�Χ
	private RectF mRange = new RectF();
	//�̿��ֱ��
	private int mRadius;
	//�����̿�Ļ���
	private Paint mArcPaint;
	//�����ı��Ļ���
	private Paint mTextPaint;
	
	//�̿�������ٶ�
	private double mSpeed ;
	/**
	 * volatile��֤�̼߳�Ŀɼ���
	 * ��ʾת�̵�һ���Ƕ�
	 */
	private volatile float mStartAngle = 0;
	//�ж��Ƿ�����ֹͣ��ť
	private boolean isShouldEnd;
	//ת�̵�����λ��
	private int mCenter;
	//paddingֱ��һpaddingLeft Ϊ׼
	private int mPadding;
	//���ֵĴ�С
	private float mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,20,getResources().getDisplayMetrics());
	
	public LuckyPan(Context context) {
		this(context, null);
	}


	public LuckyPan(Context context, AttributeSet attrs) {
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	// TODO Auto-generated method stub
    	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    	//Luckypan��widthȡ���ߵ���Сֵ   
    	int width = Math.min(getMeasuredWidth(),getMeasuredHeight());
    	mPadding = getPaddingLeft();
    	//ֱ��
    	mRadius = width-mPadding*2;
    	//���ĵ�
    	mCenter = width/2;
    	setMeasuredDimension(width, width);
    	
    }
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}


	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		//��ʼ�������̿�Ļ���
		mArcPaint = new Paint();
	    mArcPaint.setAntiAlias(true);//�����
		mArcPaint.setDither(true);
		
		mTextPaint = new Paint();
		mTextPaint.setColor(0xffffffff);
		mTextPaint.setTextSize(mTextSize);
		
		//��ʼ�������̿�ķ�Χ
		mRange = new RectF(mPadding,mPadding,mPadding+mRadius,mPadding+mRadius);
		
		//��ʼ��ͼƬ
		mImgsBitmap = new Bitmap[mItemCount];
		for (int i = 0; i <mItemCount; i++) {
			mImgsBitmap[i] = BitmapFactory.decodeResource(getResources(),mImgs[i]);
		}
		
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
			long start = System.currentTimeMillis();
			draw();
			long end = System.currentTimeMillis();
			if (end-start<50) {
				try {
					Thread.sleep(50-(end-start));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}


	private void draw() {
		try {
			// ��ȡcanvas
			mCanvas = mHolder.lockCanvas();
			if (mCanvas!=null) {
				//���Ʊ���
				drawBg();
				//�����̿�
				float tmpAngle = mStartAngle;
				float sweepAngle = 360/mItemCount;
				for (int i = 0; i < mItemCount; i++) {
					mArcPaint.setColor(mColors[i]);
					//�����̿�
					mCanvas.drawArc(mRange, tmpAngle, sweepAngle,true,mArcPaint);
				    //�����ı�
					drawText(tmpAngle,sweepAngle,mStrs[i]);
					//����icon
					drawIcon(tmpAngle,mImgsBitmap[i]);
					tmpAngle +=sweepAngle;
				}
				mStartAngle +=mSpeed;
				//��������ֹͣ��ť
				if (isShouldEnd) {
					mSpeed -=1;
				}
				if (mSpeed<=0) {
					mSpeed = 0;
					isShouldEnd = false;
				}
			}
		} catch (Exception e) {
			
		}finally{
			if (mCanvas!=null) {
				//�ͷ�canvas
				mHolder.unlockCanvasAndPost(mCanvas);
			}
		}
	}
	/**
	 * ���������ת
	 */
	public void luckyStart(int index){
		//����ÿһ��ĽǶ�
		float angle = 360/mItemCount;
		//����ÿһ����н���Χ����ǰindex��
		//1->150-210
		//0->210-270
		float from = 270-(index+1)*angle;
		float end = from+angle;
		
		//����ͣ������Ҫ��ת�ľ���
		float targetFrom = 4*360+from;
		float targetEnd = 4*360+end;
		/**
		 * <pre>
		 * v1->0
		 * ��ÿ��-1
		 * 
		 * (v1+0)*(v1+1)/2=targetfrom �Ȳ��������
		 * v1*v1+v1-2*targetfrom = 0;
		 * v1=(-1+Math.sqrt(1+4*2*targetfrom))/2
		 * </pre>
		 */
		
		float v1 = (float) ((-1+Math.sqrt(1+8*targetFrom))/2);
		float v2 = (float) ((-1+Math.sqrt(1+8*targetEnd))/2);
		mSpeed = v1+Math.random()*(v2-v1);
//		mSpeed = v1;
//		mSpeed = 50;
		isShouldEnd = false;
	}
	
	/**
	 * ���ֹͣ��ת
	 */
	public void luckyEnd(){
		mStartAngle = 0;
		isShouldEnd = true;
	}
	/**
	 * ת���Ƿ�����ת
	 * @return
	 */
	public boolean isStart(){
		return mSpeed!=0;
	}
	public boolean isShouldEnd(){
		return isShouldEnd;
	}
	
	/**
	 * ����icon
	 * @param tmpAngle
	 * @param bitmap
	 */
			
	private void drawIcon(float tmpAngle, Bitmap bitmap) {
		//����ͼƬ�Ŀ��Ϊֱ����1/8
		int imgWidth = mRadius/8;
		//
		float angle = (float) ((tmpAngle+360/mItemCount/2)*Math.PI/180);
		//ͼƬ���ĵ�λ��
		int x = (int) (mCenter+mRadius/2/2*Math.cos(angle));
		int y = (int) (mCenter+mRadius/2/2*Math.sin(angle));
		
		//ȷ��ͼƬ��λ��
		Rect rect = new Rect(x-imgWidth/2,y-imgWidth/2,x+imgWidth/2,y+imgWidth/2);
		mCanvas.drawBitmap(bitmap, null,rect,null);
	}


	/**
	 * ����ÿ���̿���ı�
	 * @param tmpAngle
	 * @param sweepAngle
	 * @param string
	 */
    private void drawText(float tmpAngle, float sweepAngle, String string) {
		// TODO Auto-generated method stub
		Path path = new Path();
		path.addArc(mRange, tmpAngle, sweepAngle);
		
		//����ˮƽƫ���������־���
	   float textWidth = mTextPaint.measureText(string);
	   
	     int hOffset = (int) (mRadius*Math.PI/mItemCount/2-textWidth/2);
		int vOffset = mRadius/2/6;//��ֱƫ����
		mCanvas.drawTextOnPath(string, path, hOffset, vOffset, mTextPaint);
	}


	/**
     * ���Ʊ���
     */

	private void drawBg() {
		// TODO Auto-generated method stub
		mCanvas.drawColor(0xFFFFFFFF);
		mCanvas.drawBitmap(mBgBitmap,null,new RectF(mPadding/2,mPadding/2,getMeasuredWidth()-mPadding/2,getMeasuredHeight()-mPadding/2),null);
		
	}

}
