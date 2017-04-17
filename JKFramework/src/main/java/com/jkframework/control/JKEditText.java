package com.jkframework.control;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.jkframework.R;
import com.jkframework.algorithm.JKAnalysis;
import com.jkframework.algorithm.JKConvert;
import com.jkframework.debug.JKLog;

import java.lang.reflect.Field;
import java.util.ArrayList;



public class JKEditText extends AppCompatEditText
{
	/**原始字符串*/
	private String tOriginal = "";
	/**分割字符*/
	private String tDelimiter = "";
	/**分割规则*/
	private String tDelimiterLength = "";
	/**是否在分割字符串上*/
	private boolean bDelimitePos = false;
    /**默认颜色*/
    private Drawable dwBackground;
    /**return键状态*/
    private int nImeOptions = 0;
    /**输入状态*/
    private int nInputType = 0;
	/**光标图案*/
	private int nCursorRes;
    /**上边距*/
    private float fPaddingTop = 0;
    /**下边距*/
    private float fPaddingBottom = 0;
    /**左边距*/
    private float fPaddingLeft = 0;
    /**右边距*/
    private float fPaddingRight = 0;
	/**分割的数字记录*/
	private ArrayList<Integer> a_nDelimiter = new ArrayList<>();
	
	public JKEditText(Context context) {
		super(context);
	}	

	public JKEditText(Context context, AttributeSet attrs) {
		super(context, attrs);

		InitAttri(context,attrs);
		Init();
	}	

	public JKEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		InitAttri(context,attrs);
		Init();
	}	

	private void InitAttri(Context context,AttributeSet attrs)
	{
		TypedArray taArray = context.obtainStyledAttributes(attrs, R.styleable.JKEditText);
		this.tDelimiter = taArray.getString(R.styleable.JKEditText_delimiter);
		this.tDelimiterLength = taArray.getString(R.styleable.JKEditText_delimiterLength);
		dwBackground = taArray.getDrawable(R.styleable.JKEditText_normalBackground);
		nImeOptions = taArray.getInt(R.styleable.JKEditText_imeOptions, EditorInfo.IME_ACTION_NEXT);
		nInputType = taArray.getInt(R.styleable.JKEditText_inputType, EditorInfo.TYPE_CLASS_TEXT);
		nCursorRes = taArray.getResourceId(R.styleable.JKEditText_textCursorDrawable, 0);
		fPaddingLeft = taArray.getDimension(R.styleable.JKEditText_paddingLeft, 0);
		fPaddingRight = taArray.getDimension(R.styleable.JKEditText_paddingRight, 0);
		fPaddingTop = taArray.getDimension(R.styleable.JKEditText_paddingTop, 0);
		fPaddingBottom = taArray.getDimension(R.styleable.JKEditText_paddingBottom, 0);
		taArray.recycle();
	}

	/**
	 * 初始化
	 */
    @TargetApi(16)
	private void Init()
	{
        if (dwBackground == null)
		{
			setBackgroundColor(0);
            setPadding((int)fPaddingLeft,(int)fPaddingTop,(int)fPaddingRight,(int)fPaddingBottom);
		}
        else {
            if (Build.VERSION.SDK_INT >= 16)
                setBackground(dwBackground);
            else
                setBackgroundDrawable(dwBackground);
			setPadding((int)fPaddingLeft,(int)fPaddingTop,(int)fPaddingRight,(int)fPaddingBottom);
        }
        setImeOptions(nImeOptions);
        setInputType(nInputType);
        try {
            Class<?> viewpager = TextView.class;
            Field scroller = viewpager.getDeclaredField("mCursorDrawableRes");
            scroller.setAccessible(true);

            scroller.set(this, nCursorRes);

        } catch (Exception e) {
            e.printStackTrace();
        }

		if (tDelimiter != null && tDelimiterLength != null) {
			ArrayList<String> a_tDelimiter = JKAnalysis.Split(tDelimiterLength, ",");
			for (int i = 0; i < a_tDelimiter.size(); ++i)    //记录分割数字
			{
				a_nDelimiter.add(JKConvert.toInt(a_tDelimiter.get(i)));
				if (i != 0) {
					a_nDelimiter.set(i, a_nDelimiter.get(i) + a_nDelimiter.get(i - 1));
				}
			}

			addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
					StringBuilder tNow = new StringBuilder();
					if (arg2 == 0 || arg3 == 0) {
						String tAdd = arg0.toString().substring(arg1, arg1 + arg3);
						int nRealPos = GetRealPos(arg1);
						tNow.append(tOriginal);
						tNow.insert(nRealPos, tAdd);
						tNow.replace(nRealPos - (bDelimitePos ? 1 : 0), nRealPos + arg2 - (bDelimitePos ? 1 : 0), "");
						String tText = Split(tNow.toString());
						if (!tText.equals(arg0.toString())) {
							setText(tText);
							setSelection(Math.min(tText.length(), GetRealSelection(arg1, arg3 != 0)));
						}
					}
				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
											  int arg3) {
					if (arg2 == 0 || arg3 == 0) {
						tOriginal = Analysis(arg0.toString());
						bDelimitePos = IsDelimiterPos(arg1 + arg2);
					}
				}

				@Override
				public void afterTextChanged(Editable arg0) {

				}
			});
		}
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		super.setText(text, type);
		if (text != null)
			setSelection(text.length());
	}
	
	@Override
	public void setSelection(int nPos)
	{
		super.setSelection(Math.min(nPos,GetMaxLength()));
	}
	
	/**
	 * 获得分割前的字符串
	 * @return
	 */
	public String GetRealText()
	{
		return Analysis(getText().toString());
	}
	
	/**
	 * 获取最大输入长度
	 * @return 最大输入长度
	 */
	public int GetMaxLength()  
	{
	    int length = Integer.MAX_VALUE;
	    try   
	    {  
	        InputFilter[] inputFilters = getFilters();  
	        for(InputFilter filter:inputFilters)  
	        {  
	            Class<?> c = filter.getClass();  
	            if(c.getName().equals("android.text.InputFilter$LengthFilter"))  
	            {  
	                Field[] f = c.getDeclaredFields();  
	                for(Field field:f)  
	                {  
	                    if(field.getName().equals("mMax"))  
	                    {  
	                        field.setAccessible(true);  
	                        length = (Integer)field.get(filter);  
	                    }  
	                }  
	            }  
	        }  
	    }  
	    catch (Exception e)   
	    {  
	    	JKLog.ErrorLog("无法获取输入框最大输入长度.原因为" + e.getMessage());
	        e.printStackTrace();  
	    }  
	    return length;  
	}  
	
	/**
	 * 按分割规则解析
	 * @param tText 分割后的字符串
	 * @return 分割前的字符串
	 */
	private String Analysis(String tText)
	{
		StringBuilder tBack = new StringBuilder(tText);
		for (int i=0; i<a_nDelimiter.size(); ++i)
		{
			if (tBack.length() >= a_nDelimiter.get(i) + tDelimiter.length())
			{
				if (tBack.substring(a_nDelimiter.get(i), a_nDelimiter.get(i) + tDelimiter.length()).equals(tDelimiter))
				{
					tBack.replace(a_nDelimiter.get(i), a_nDelimiter.get(i) + tDelimiter.length(),"");
				}
			}
		}
		return tBack.toString();
	}
	
	/**
	 * 分割字符串
	 * @param tText 分割前的字符串
	 * @return 按规则分割后的字符串
	 */
	private String Split(String tText)
	{
		StringBuilder tBack = new StringBuilder(tText);
		for (int i=a_nDelimiter.size() - 1; i>=0; --i)
		{
			if (tText.length() > a_nDelimiter.get(i))
				tBack.insert(a_nDelimiter.get(i), tDelimiter);
		}
		return tBack.toString();
	}
	
	/**
	 * 获取分割前真实位置
	 * @param nPos 分割后的位置
	 * @return 分割前的位置
	 */
	private int GetRealPos(int nPos)
	{
		for (int i=0; i<a_nDelimiter.size(); ++i)
		{
			for (int j=0; j<tDelimiter.length(); j++)
			{
				if (nPos + j >= a_nDelimiter.get(i) + tDelimiter.length())
				{
					nPos -= tDelimiter.length();
					nPos += j;
					break;
				}
			}
		}
		return nPos;
	}
	
	/**
	 * 获取真实的选择框位置
	 * @param nPos 当前选择框位置
	 * @param bAdd 操作规则是添加还是删除,true表示添加
	 * @return 真实位置
	 */
	private int GetRealSelection(int nPos,boolean bAdd)
	{
		int nItem;
		if (bAdd)
		{
			for (int i=0; i<a_nDelimiter.size(); ++i)
			{
				nItem = a_nDelimiter.get(i) + tDelimiter.length() * (i+1);
				for (int j=0; j<tDelimiter.length(); j++)
				{
					if (nPos == nItem - j - 1)
					{
						return nPos + j + tDelimiter.length();
					}
				}
			}
			return nPos + 1;
		}
		else 
		{
			for (int i=0; i<a_nDelimiter.size(); ++i)
			{
				nItem = a_nDelimiter.get(i) + tDelimiter.length() * (i+1);
				for (int j=0; j<tDelimiter.length(); j++)
				{
					if (nPos == nItem - j - 1)
					{
						return nPos + j - tDelimiter.length();
					}
				}
			}
			return nPos;
		}
	}
	
	/**
	 * 是否在分割位置上
	 * @param nPos 字符位置
	 * @return true表示在分割位置上
	 */
	private boolean IsDelimiterPos(int nPos)
	{
		int nItem;
		for (int i=0; i<a_nDelimiter.size(); ++i)
		{
			nItem = a_nDelimiter.get(i) + tDelimiter.length() * (i+1);
			for (int j=0; j<tDelimiter.length(); j++)
			{
				if (nPos == nItem - j)
				{
					return true;
				}
			}
		}
		return false;
	}
}