package com.jkframework.control;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.jkframework.R;
import com.jkframework.algorithm.JKConvert;

public class JKSideBar extends View{

	/**画笔控件*/
	private Paint ptPaint = new Paint();  
	/**索引内容*/
	private String[] a_tIndex = null;
	/**未选择时的颜色*/
	private int nNormalColor = 0;
	/**选择后的颜色*/
	private int nSelectColor = 0;
	/**默认颜色*/
	private int nBackground = 0;
	/**选择后的背景颜色*/
	private int nSelectBackground = 0;
	/**当前选择*/
	private int nChoose = -1;
	/**左边距*/
	private int nPaddingLeft = 0;
	/**上边距*/
	private int nPaddingTop = 0;
	/**右边距*/
    private int nPaddingRight = 0;
    /**下边距*/
    private int nPaddingBottom = 0;
	/**文字大小*/
	private float fTextSize = 0;
	/**文字大小*/
	private boolean bHasSelectBackground = false;
	/**索引对象*/
    private SectionIndexer siIndexter = null;
    /**列表对象*/
    private ListView lvListView;
    /**提示索引字母*/
    private TextView tvTips;
    /**弹出框*/
    private PopupWindow pwTips = null;
    
    
    public JKSideBar(Context context) {  
        super(context);  
    }  
    
    public JKSideBar(Context context, AttributeSet attrs) {  
    	super(context, attrs);
    	if (isInEditMode())
            return;
    	TypedArray taArray = context.obtainStyledAttributes(attrs,R.styleable.JKSideBar);
        int nPadding = taArray.getDimensionPixelSize(R.styleable.JKSideBar_android_padding, 0);
        nPaddingLeft = taArray.getDimensionPixelSize(R.styleable.JKSideBar_android_paddingLeft, nPadding);
        nPaddingTop = taArray.getDimensionPixelSize(R.styleable.JKSideBar_android_paddingTop, nPadding);
        nPaddingRight = taArray.getDimensionPixelSize(R.styleable.JKSideBar_android_paddingRight, nPadding);
        nPaddingBottom = taArray.getDimensionPixelSize(R.styleable.JKSideBar_android_paddingBottom, nPadding);
        nBackground = taArray.getColor(R.styleable.JKSideBar_android_background, 0x00000000);
        if (taArray.getValue(R.styleable.JKSideBar_textSize, new TypedValue()))
        {
        	bHasSelectBackground = true;
        	nSelectBackground = taArray.getColor(R.styleable.JKSideBar_selectBackground, 0x00000000);
        }
		this.nNormalColor = taArray.getColor(R.styleable.JKSideBar_normalColor,0xFFFFFFFF);
		this.nSelectColor = taArray.getColor(R.styleable.JKSideBar_selectColor,nNormalColor);
		this.fTextSize = taArray.getDimension(R.styleable.JKSideBar_textSize,JKConvert.SpToPx(9));
		taArray.recycle();
		
    	Init();
    }  
    
    public JKSideBar(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        Init();
    }  
    
    /**
     * 设置列表控件对象
     * @param lvTmp 列表控件对象
     */
    public void SetListView(ListView lvTmp) {  
    	lvListView = lvTmp;
    	siIndexter = (SectionIndexer) lvTmp.getAdapter();
        a_tIndex = (String[]) siIndexter.getSections();
    }  
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getLayoutParams().height == LayoutParams.WRAP_CONTENT)
        {
            Paint paint = new Paint();
            paint.setTextSize(fTextSize);
            Paint.FontMetrics fm = paint.getFontMetrics();
            heightMeasureSpec = (int) (Math.ceil(fm.descent - fm.ascent) * a_tIndex.length);
        }
    	if (getLayoutParams().width >= 0 || getLayoutParams().width == LayoutParams.MATCH_PARENT)
    	{
    		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    	}
    	else if (getLayoutParams().width == LayoutParams.WRAP_CONTENT){
    		int nMaxWidth = 0;
            for (String anA_tIndex : a_tIndex) {
                nMaxWidth = (int) Math.max(ptPaint.measureText(anA_tIndex), nMaxWidth);
            }
        	setMeasuredDimension(nMaxWidth + nPaddingLeft + nPaddingRight, heightMeasureSpec);
    	}
    }
 
    @Override
    protected void onDraw(Canvas canvas) {  
        int nHeight = getHeight();
        int nWidth = getWidth();        
        //计算得出每一个字体大概的高度
        int nSingleHeight = (nHeight - nPaddingTop - nPaddingBottom) / a_tIndex.length;
        for (int i = 0; i < a_tIndex.length; i++) {
            //得到字体的X坐标
            float xPos = (nWidth - ptPaint.measureText(a_tIndex[i]) - nPaddingLeft - nPaddingRight) / 2 + nPaddingLeft;
            //得到字体的Y坐标
            float yPos = (nSingleHeight * (i+1)) - (nSingleHeight - ptPaint.getTextSize())/2 + nPaddingTop;
            if (i == nChoose) {
            	ptPaint.setColor(nSelectColor);
            }
            else {
            	ptPaint.setColor(nNormalColor);    	
            }
            //将字体绘制到面板上
            canvas.drawText(a_tIndex[i], xPos, yPos, ptPaint);
        }
        super.onDraw(canvas);  
    }  
    
    /**
     * 点击事件
     */
    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        //得到点击的状态
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = nChoose;
        //得到当前的值
        final int nTmpIndex = (int) ((y - nPaddingTop) / (getHeight()-nPaddingTop-nPaddingBottom) * a_tIndex.length);
        
        //根据点击的状态不同做出不同的处理
        switch (action) {
        //按下已经开始
        case MotionEvent.ACTION_DOWN:
        	if (bHasSelectBackground)
        		setBackgroundColor(nSelectBackground);
            if (oldChoose != nTmpIndex) {
                if (nTmpIndex >= 0 && nTmpIndex < a_tIndex.length) {
                    nChoose = nTmpIndex;
                    UpdatePosition();
                }
            }
            if (pwTips != null && (nChoose >= 0 && nChoose < a_tIndex.length)) {
            	tvTips.setText(a_tIndex[nChoose]);
            	if (!pwTips.isShowing())
            		pwTips.showAtLocation(lvListView, Gravity.CENTER, 0, 0);
            	else 
            		pwTips.update();
			}
            break;
            //松开为完成点击
        case MotionEvent.ACTION_MOVE:
            if (oldChoose != nTmpIndex) {
                if (nTmpIndex >= 0 && nTmpIndex < a_tIndex.length) {
                	nChoose = nTmpIndex;
                	UpdatePosition();
                }
            }
            if (pwTips != null && (nChoose >= 0 && nChoose < a_tIndex.length)) {
            	tvTips.setText(a_tIndex[nChoose]);
            	if (!pwTips.isShowing())
            		pwTips.showAtLocation(lvListView, Gravity.CENTER, 0, 0);
            	else 
            		pwTips.update();
			}
            break;
        //完成松开  还原数据 并刷新界面
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
        	setBackgroundColor(nBackground);
        	nChoose = -1;
            invalidate();
            if (pwTips != null)
            	pwTips.dismiss();
            break;
        }
        return true;
    }
 
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        return super.onTouchEvent(event);
    }
    
    /**
	 * 初始化提示框对象(需要自定义重写此函数)
	 * @return 对话框对象
	 */
	protected TextView InitTextView()
	{
		return (TextView) LayoutInflater.from(getContext()).inflate(
				R.layout.jkframework_jksidebartips, null);
	}
    
    /**
     * 初始化控件
     */
    private void Init()
    {
    	tvTips = InitTextView();
    	pwTips = new PopupWindow(tvTips,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
    	pwTips.setContentView(tvTips);
    	ptPaint.setAntiAlias(true);
    	ptPaint.setTextSize(fTextSize);
    	ptPaint.setColor(nNormalColor);
    	setBackgroundColor(nBackground);
    }
    
    /**
     * 更新位置
     */
    private void UpdatePosition()
    {
    	lvListView.setSelection(siIndexter.getPositionForSection(nChoose));
        //刷新界面
        invalidate();
    }
}
