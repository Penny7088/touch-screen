package com.jkframework.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.jkframework.algorithm.JKConvert;
import com.jkframework.algorithm.JKFile;
import com.jkframework.config.JKPreferences;





public class JKFileActivity extends JKBaseActivity
{
		
	/**文件选择*/
	private final int REQUEST_CHOICE_FILE = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Intent itIntent = new Intent(Intent.ACTION_GET_CONTENT);
		itIntent.addCategory(Intent.CATEGORY_OPENABLE);
		itIntent.setType("*/*");
		StartActivityForResult(itIntent, REQUEST_CHOICE_FILE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CHOICE_FILE) {  
			if (resultCode == RESULT_OK)
			{
				if (data != null)
	            {
					Uri uri = data.getData();
					if (uri != null)
					{
						String tPath = JKConvert.UriToPath(uri);
						if (tPath == null)
						{
							if (JKFile.mChoiceListener != null)
								JKFile.mChoiceListener.FinishChoice(null);
						}
						JKPreferences.SaveSharePersistent("JKFILE_CHOICEFILE",tPath);
						if (JKFile.mChoiceListener != null)
							JKFile.mChoiceListener.FinishChoice(tPath);
					}
					else {
						String tFilePath = data.getStringExtra("filePath");
						JKPreferences.SaveSharePersistent("JKFILE_CHOICEFILE",tFilePath);
						if (JKFile.mChoiceListener != null)
							JKFile.mChoiceListener.FinishChoice(tFilePath);
					}
	            }
			}
			else if (resultCode == RESULT_CANCELED){
				JKPreferences.SaveSharePersistent("JKFILE_CHOICEFILE","");
				if (JKFile.mChoiceListener != null)
					JKFile.mChoiceListener.FinishChoice(null);
			}
            finish();
		}
	}
}