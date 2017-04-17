package com.jkframework.algorithm;

import android.media.MediaRecorder;

import java.io.IOException;


public class JKRecorder {

	/**录音对象*/
	private static JKRecorder jkrRecorder = null;
	/**录音媒体对象*/
	private MediaRecorder mrRecorder = null;
	/**录音保存地址*/
	private String tSavePath = "";
    /**录音持续时间*/
    private int nDuration = 0;
    /**录音开始时间*/
    private long lStartTime = 0;

	/**
	 * 获取录音对象
	 * @return  录音对象
	 */
	public static JKRecorder GetInstance()
	{
		if (jkrRecorder == null)
		{
			jkrRecorder = new JKRecorder();
		}
		return jkrRecorder;
	}

	/**
	 * 开始录音
	 */
	public void StartRecord()
	{
		mrRecorder = new MediaRecorder();
		mrRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mrRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
		tSavePath = JKFile.GetPublicCachePath() + "/JKCache/JKRecorder/" + JKRandom.MakeGUID() + ".amr";
		JKFile.CreateDir(tSavePath);
		mrRecorder.setOutputFile(tSavePath);
		mrRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		try {
			mrRecorder.prepare();
		} catch (IOException e) {
			mrRecorder = null;
			return;
		}
		mrRecorder.start();
        lStartTime = JKDate.GetExactCalendarTime();
	}

	/**
	 * 停止录音
	 * @return 成功返回录音地址,失败返回null
	 */
	public String StopRecord()
	{
		if(mrRecorder == null)
			return  null;
		try {
            nDuration = (int) (JKDate.GetExactCalendarTime() - lStartTime);
			mrRecorder.stop();
			mrRecorder.release();
		}
		catch (Exception e)
		{
			mrRecorder = null;
			return null;
		}
		mrRecorder = null;
		return tSavePath;
	}

    /**
     * 获取录音时长
     * @return 录音时长毫秒数
     */
    public int GetDuration()
    {
        return nDuration;
    }
}