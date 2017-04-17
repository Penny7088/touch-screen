package com.jkframework.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;

import com.jkframework.R;
import com.jkframework.config.JKSystem;
import com.jkframework.control.JKTextView;
import com.jkframework.control.JKToolBar;
import com.jkframework.debug.JKException;

import java.util.Locale;




public class JKExceptionActivity extends JKBaseActivity
{	

	/**Toolbar*/
	private JKToolBar jktbToolBar;
	/**崩溃信息控件*/
	private JKTextView jktvExceptionInfo;
	/**反射输入框*/
	private EditText etExceptionEdit;
	/**调试按钮*/
	private Button cbExceptionDebug;
	/**退出按钮*/
	private Button cbExceptionExit;
	/**消息反馈*/
	private String tReportText;
	/**反馈类名*/
	private String tReflectClass;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.jkframework_exceptionactivity);
		tReportText = getIntent().getStringExtra("ReportText");
		tReflectClass = getIntent().getStringExtra("Class");

		InitView();
		InitListener();
		InitData();
	}
	
	@Override
	public void onBackPressed()
	{
		JKException.ExitProgram();
		System.exit(0);
	}
	
	/**
	 * 初始化界面
	 */
	private void InitView()
	{
		jktvExceptionInfo = (JKTextView) findViewById(R.id.jktvExceptionInfo);
		etExceptionEdit = (EditText) findViewById(R.id.etExceptionEdit);
		cbExceptionDebug = (Button) findViewById(R.id.cbExceptionDebug);
		cbExceptionExit = (Button) findViewById(R.id.cbExceptionExit);
		jktbToolBar = (JKToolBar) findViewById(R.id.jktbToolBar);
	}
	
	/**
	 * 初始化监听事件
	 */
	protected void InitListener()
	{
		etExceptionEdit.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER
						&& event.getAction() == KeyEvent.ACTION_UP) {
					JKException.DoingReflect(JKExceptionActivity.this,tReflectClass,etExceptionEdit.getText().toString().toLowerCase(Locale.getDefault()));
					return true;
				}
				return false;
			}
		});
		cbExceptionDebug.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				JKException.DoingReflect(JKExceptionActivity.this,tReflectClass,etExceptionEdit.getText().toString().toLowerCase(Locale.getDefault()));
			}
		});		
		cbExceptionExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				JKException.ExitProgram();
				System.exit(0);
			}
		});
	}
	
	/**
	 * 初始化数据
	 */
	private void InitData()
	{
		jktbToolBar.setSubtitle("程序崩溃");
		jktbToolBar.Attach(this);
		jktbToolBar.setLogo(JKSystem.GetApplicationIcon());
        jktvExceptionInfo.setText(tReportText);
	}
}