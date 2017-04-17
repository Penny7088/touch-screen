package com.jkframework.control;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

import com.jkframework.activity.JKBaseActivity;
import com.jkframework.fragment.JKBaseFragment;

public class JKToolBar extends Toolbar {

	public JKToolBar(Context context) {
		super(context);
	}

	public JKToolBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public JKToolBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

    /**
     * 附加Activity上
     * @param CurrentActivity 当前Activity
     */
	public void Attach(JKBaseActivity CurrentActivity)
	{
		CurrentActivity.setSupportActionBar(this);
	}

    /**
     * 附加Fragment上
     * @param CurrentFragment 当前Fragment
     */
    public void Attach(JKBaseFragment CurrentFragment)
    {
        if (CurrentFragment.getActivity() != null)
        {
            JKBaseActivity CurrentActivity = (JKBaseActivity) CurrentFragment.getActivity();
            Attach(CurrentActivity);
        }
    }
}