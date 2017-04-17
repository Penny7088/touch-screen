package com.jkframework.control;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;

import com.jkframework.R;
import com.jkframework.algorithm.JKFile;
import com.jkframework.callback.JKDownloadLinstener;
import com.jkframework.config.JKSystem;
import com.jkframework.net.JKHttpDownload;

public class JKTextView extends AppCompatTextView {

	/** 显示最大行数 */
	private int nMaxLines = -1;
	/**0为不显示省略号,1为start,2为middle,3为end*/
	private int nEllipsize;
	/**文字风格*/
	private int nTextStyle = 0;
	/**是否进行特殊处理*/
	private boolean bEllipsize = false;
	/**完整的文本*/
	private CharSequence tFullText = null;
	/**html文本*/
	private String tHtml = null;
	/**文本间距*/
	private float fLineSpacingMultiplier = 1.0f;
	/**文本水平padding*/
	private float fLineAdditionalVerticalPadding = 0.0f;

	public JKTextView(Context context) {
		super(context);
	}

	public JKTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray taArray = context.obtainStyledAttributes(attrs, R.styleable.JKTextView);
		this.nTextStyle = taArray.getInt(R.styleable.JKTextView_textStyle, 0);
        this.nMaxLines = taArray.getInt(R.styleable.JKTextView_maxLines, -1);
		taArray.recycle();

		Init();
	}

	public JKTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray taArray = context.obtainStyledAttributes(attrs, R.styleable.JKTextView);
		this.nTextStyle = taArray.getInt(R.styleable.JKTextView_textStyle, 0);
        this.nMaxLines = taArray.getInt(R.styleable.JKTextView_maxLines, -1);
		taArray.recycle();

		Init();
	}

	/**
	 * 初始化属性
	 */
	private void Init()
	{
		if ((nTextStyle & 4) != 0)
			getPaint().setUnderlineText(true);
		nTextStyle = nTextStyle- (nTextStyle & 4);
		setTypeface(getTypeface(), nTextStyle);
		if (nMaxLines >= 0)
        	setMaxLines(nMaxLines);

		getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (!bEllipsize)
					ResetText();
			}
		});
    }

	@Override
	public void onDraw(@NonNull Canvas canvas)
	{
		super.onDraw(canvas);
	}

	@Override
	public void setLineSpacing(float add, float mult) {
		super.setLineSpacing(add, mult);
		this.fLineAdditionalVerticalPadding = add;
		this.fLineSpacingMultiplier = mult;
	}
	
	@Override
	public void setMaxLines(int maxLines) {
		super.setMaxLines(maxLines);
		this.nMaxLines = maxLines;
		if (getLineCount() > nMaxLines) {
			ResetText();
		}
	}

    @Override
	public void setSingleLine()
	{
		setMaxLines(1);
	}
	
	@Override
	protected void onTextChanged(CharSequence text, int start, int before,
			int after) {
		super.onTextChanged(text, start, before, after);
		if (!bEllipsize) {
			tFullText = text;
		}
	}

	/**
	 * 返回实际文本
	 * @return 实际文本
     */
	public String GetRealText()
	{
		return getText().toString();
	}

	/**
	 * 设置特殊处理的文本
	 * @param tText 特殊处理后的文本
	 */
	public void SetEllipsizeText(String tText)
	{
        bEllipsize = true;
		super.setText(tText);
        bEllipsize = true;
	}

	@Override
	public CharSequence getText()
	{
		if (tFullText != null)
			return tFullText;
		else
            return super.getText();
	}

    @Override
    public void setText(CharSequence text, BufferType type)
    {
        super.setText(text, type);
        bEllipsize = false;
    }

	@Override
	public void setEllipsize(TextUtils.TruncateAt where) {
		if (where == TextUtils.TruncateAt.START)
		{
			nEllipsize = 1;
		}
		else if (where == TextUtils.TruncateAt.MIDDLE)
		{
			nEllipsize = 2;
		}
		else if (where == TextUtils.TruncateAt.END)
		{
			nEllipsize = 3;
		}
		else 
			nEllipsize = 0;
    }

	/**
	 * 设置富文本
	 * @param tHtml html文本
	 */
	public void SetHtmlText(String tHtml)
	{
		this.tHtml = tHtml;
        super.setText(Html.fromHtml(tHtml, GetImage(), null));
        super.setMovementMethod(LinkMovementMethod.getInstance());
	}

	/**
	 * 重新计算省略号位置
	 */
	private void ResetText() {
        String tText = super.getText().toString();
		if (getLineCount() > nMaxLines)		//需要画...
		{
			switch (nEllipsize) {
                case 1:  //开头画...
                {
                    String tWorkText1 = tText.substring(Math.min(tText.length(), getLayout().getLineStart(getLineCount() - nMaxLines)));
                    Layout lLayout1 = CreateWorkingLayout("..." + tWorkText1);
                    while (lLayout1.getLineCount() > nMaxLines) {
                        if (tWorkText1.length() == 0)
                            break;
                        tWorkText1 = tWorkText1.substring(1);
                        lLayout1 = CreateWorkingLayout("..." + tWorkText1);
                    }
                    SetEllipsizeText("..." + tWorkText1);
                    break;
                }
                case 2:  //中间画...
                {
                    String tStartWorkText2 = tText.substring(Math.min(tText.length(), getLayout().getLineStart(getLineCount() - (nMaxLines + 1) / 2)));
                    String tEndWorkText2 = tText.substring(0, Math.min(tText.length(), getLayout().getLineEnd((nMaxLines + 1) / 2)));
                    Layout lLayout2 = CreateWorkingLayout(tEndWorkText2 + "..." + tStartWorkText2);
                    while (lLayout2.getLineCount() > nMaxLines) {
                        boolean bStart = tEndWorkText2.length() >= tStartWorkText2.length();
                        if (bStart)
                        {
                            if (tEndWorkText2.length() == 0)
                                break;
                            tEndWorkText2 = tEndWorkText2.substring(0, tEndWorkText2.length() - 1);
                        }
                        else {
                            if (tStartWorkText2.length() == 0)
                                break;
                            tStartWorkText2 = tStartWorkText2.substring(1);
                        }
                        lLayout2 = CreateWorkingLayout(tEndWorkText2 + "..." + tStartWorkText2);
                    }
                    SetEllipsizeText(tEndWorkText2 + "..." + tStartWorkText2);
                    break;
                }
                case 3:    //末尾画...
                {
                    String tWorkText3 = tText.substring(0, Math.min(tText.length(), getLayout().getLineEnd(nMaxLines)));
                    Layout lLayout3 = CreateWorkingLayout(tWorkText3 + "...");
                    while (lLayout3.getLineCount() > nMaxLines) {
                        if (tWorkText3.length() == 0)
                            break;
                        tWorkText3 = tWorkText3.substring(0, tWorkText3.length() - 1);
                        lLayout3 = CreateWorkingLayout(tWorkText3 + "...");
                    }
                    SetEllipsizeText(tWorkText3 + "...");
                    break;
                }
            }
		}
	}
	
	/**
	 * 创建计算高度的Layout
	 * @param workingText 需要放入计算的文字
	 * @return 计算后的Layout
	 */
	private Layout CreateWorkingLayout(String workingText) {
		return new StaticLayout(workingText, getPaint(), getWidth()
				- getPaddingLeft() - getPaddingRight(), Alignment.ALIGN_NORMAL,
				fLineSpacingMultiplier, fLineAdditionalVerticalPadding, false);
	}
	
	/**
	 * Textview图文混排图片解析
	 * @return 解析对象
	 */
	private ImageGetter GetImage() {
        return new ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                Drawable drawable=null;
                if (source.startsWith("http"))	//网络图片
                {
                    String tImagePath = JKFile.GetPublicCachePath() + "/JKCache/JKTextView" + source.substring(source.lastIndexOf("/"));
                    if (JKFile.IsExists(tImagePath))
                    {
                        drawable = Drawable.createFromPath(tImagePath);
                        if (drawable == null)
                            return null;
                        int nWidth = (int)(drawable.getIntrinsicWidth() * JKSystem.GetDensity());
                        int nHeight = (int)(drawable.getIntrinsicHeight() * JKSystem.GetDensity());
                        float fScale = 1;
                        int nTarget = getMeasuredWidth();
                        if (nWidth > nTarget && nTarget!=0)
                        {
                            fScale = nTarget / (float)nWidth;
                        }
                        drawable.setBounds(0, 0, (int)(nWidth*fScale),(int)(nHeight*fScale));
                    }
                    else {
                        JKHttpDownload jkhsDown = new JKHttpDownload();
                        jkhsDown.InitType(source);
                        jkhsDown.DownLoad(new JKDownloadLinstener() {

                            @Override
                            public void ReceiveStatus(int nCode) {
                                if (nCode == 1)
                                {
                                    SetHtmlText(tHtml);
                                }
                            }

                            @Override
                            public void ReceiveProgress(int nCurrentSize, int nTotalSize) {

                            }
                        },tImagePath,false);
                    }
                }
                return drawable;
            }
        };
	}
}