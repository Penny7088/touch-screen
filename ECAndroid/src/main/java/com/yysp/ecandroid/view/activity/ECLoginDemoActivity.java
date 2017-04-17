package com.yysp.ecandroid.view.activity;

import android.os.Bundle;
import android.view.View;

import com.yysp.ecandroid.R;
import com.yysp.ecandroid.view.ECBaseActivity;
import com.jkframework.control.JKToolBar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


@EActivity(R.layout.ecandroid_logindemoactivity)
public class ECLoginDemoActivity extends ECBaseActivity {

	@ViewById(R.id.jktbToolBar)
	JKToolBar jktbToolBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	void InitData()
	{
//		jktbToolBar.setTitle("");
		jktbToolBar.Attach(this);
		jktbToolBar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
