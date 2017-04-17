package com.yysp.ecandroid.view;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.yysp.ecandroid.BuildConfig;
import com.yysp.ecandroid.config.ECConfig;
import com.jkframework.activity.JKBaseActivity;
import com.jkframework.control.JKEditText;
import com.jkframework.control.JKMessagebox;
import com.jkframework.debug.JKException;

import java.util.Locale;

public abstract class ECBaseActivity extends JKBaseActivity {
	
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null)
		{
		}

//        JKSystem.SetStatusBarColor(0xFF292D53);
//        JKSystem.SetNavigationBarColor(0xFF292D53);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

    @Override
    public void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case Menu.FIRST: {
				final JKEditText etText = new JKEditText(this);
				etText.setSingleLine();
				JKMessagebox.Inputbox(this, "消息平台调试", etText, "确定",
						new DialogInterface.OnClickListener() {
	
							@Override
							public void onClick(DialogInterface dialog, int which) {
	
								String tFunction = etText.getText().toString()
										.toLowerCase(Locale.getDefault());
								JKException.DoingReflect(ECBaseActivity.this, ECConfig.MESSAGE_CENTER, tFunction);
							}
						});
				break;
			}
			case Menu.FIRST + 1: {
				throw  new NullPointerException();
			}
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (BuildConfig.DEBUG) {
			menu.add(Menu.NONE, Menu.FIRST, Menu.FIRST, "消息平台调试");
			menu.add(Menu.NONE, Menu.FIRST + 1, Menu.FIRST + 1, "崩溃测试按钮");
		}
		return true;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}

	
	@Override
	protected void onResume() {
		super.onResume();
	}

}
