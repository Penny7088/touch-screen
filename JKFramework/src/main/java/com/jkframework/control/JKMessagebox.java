package com.jkframework.control;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.v7.app.AlertDialog;
import android.view.View;



public class JKMessagebox
{
	/**
	 * 消息提示框
	 * @param hContext 上下文指针
	 * @param tTitle 提示框标题
	 * @param tMessage 提示框内容
	 * @param tOK  提示框确认按钮文字
	 * @return 返回对话框对象
	 */
	public static AlertDialog Messagebox(Context hContext, String tTitle, String tMessage, String tOK)
	{
		AlertDialog dgAlert = new AlertDialog.Builder(hContext).
                setTitle(tTitle).
                setCancelable(false).
                setMessage(tMessage).
                setPositiveButton(tOK, new OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
					}
				}).create();
		try {
			dgAlert.show();
		}
		catch (Exception ignored)
		{

		}
		return dgAlert;
	}

	/**
	 * 消息提示框
	 * @param hContext 上下文指针
	 * @param tMessage 提示框内容
	 * @param tOK  提示框确认按钮文字
	 * @return 返回对话框对象
	 */
	public static AlertDialog Messagebox(Context hContext,String tMessage,String tOK)
	{
		AlertDialog dgAlert = new AlertDialog.Builder(hContext).
                setMessage(tMessage).
                setCancelable(false).
                setPositiveButton(tOK, new OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
					}
				}).create();
		try {
			dgAlert.show();
		}
		catch (Exception ignored)
		{

		}
		return dgAlert;
	}

	/**
	 * 消息提示框
	 * @param hContext 上下文指针
	 * @param tTitle 提示框标题
	 * @param tMessage 提示框内容
	 * @param tOK  提示框确认按钮文字
	 * @param l  确认后执行的命令
	 * @return 返回对话框对象
	 */
	public static AlertDialog Messagebox(Context hContext,String tTitle,String tMessage,String tOK,DialogInterface.OnClickListener l)
	{
		AlertDialog dgAlert = new AlertDialog.Builder(hContext).
                setTitle(tTitle).
                setCancelable(false).
                setMessage(tMessage).
                setPositiveButton(tOK, l).create();
		try {
			dgAlert.show();
		}
		catch (Exception ignored)
		{

		}
		return dgAlert;
	}

	/**
	 * 消息提示框
	 * @param hContext 上下文指针
	 * @param tTitle 消息提示框标题
	 * @param tMessage 提示框内容
	 * @param tOK  提示框确认按钮文字
	 * @param l  确认后执行的命令
	 * @param tCancel 提示框取消的文字
	 * @return 返回对话框对象
	 */
	public static AlertDialog Messagebox(Context hContext,String tTitle,String tMessage,String tOK,DialogInterface.OnClickListener l,String tCancel)
	{
		AlertDialog dgAlert = new AlertDialog.Builder(hContext).
				setTitle(tTitle).
                setMessage(tMessage).
                setCancelable(true).
                setPositiveButton(tOK, l).
                setNegativeButton(tCancel, new OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        dgAlert.setCanceledOnTouchOutside(true);
		try {
			dgAlert.show();
		}
		catch (Exception ignored)
		{

		}
		return dgAlert;
	}

	/**
	 * 消息提示框
	 * @param hContext 上下文指针
	 * @param tMessage 提示框内容
	 * @param tOK  提示框确认按钮文字
	 * @param l  确认后执行的命令
	 * @param tCancel 提示框取消的文字
	 * @return 返回对话框对象
	 */
	public static AlertDialog Messagebox(Context hContext,String tMessage,String tOK,DialogInterface.OnClickListener l,String tCancel)
	{
		AlertDialog dgAlert = new AlertDialog.Builder(hContext).
                setMessage(tMessage).
                setCancelable(true).
                setPositiveButton(tOK, l).
                setNegativeButton(tCancel, new OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

					}
				}).create();
        dgAlert.setCanceledOnTouchOutside(true);
		try {
			dgAlert.show();
		}
		catch (Exception ignored)
		{

		}
		return dgAlert;
	}

	/**
	 * 消息提示框
	 * @param hContext 上下文指针
	 * @param tTitle 消息提示框标题
	 * @param tMessage 提示框内容
	 * @param tOK  提示框确认按钮文字
	 * @param l  确认后执行的命令
	 * @param tCancel 提示框取消的文字
	 * @param l2  取消后执行的命令
	 * @return 返回对话框对象
	 */
	public static AlertDialog Messagebox(Context hContext,String tTitle,String tMessage,String tOK,DialogInterface.OnClickListener l,String tCancel,DialogInterface.OnClickListener l2)
	{
		AlertDialog dgAlert = new AlertDialog.Builder(hContext).
				setTitle(tTitle).
                setMessage(tMessage).
                setCancelable(false).
                setPositiveButton(tOK, l).
                setNegativeButton(tCancel,l2).create();
		try {
			dgAlert.show();
		}
		catch (Exception ignored)
		{

		}
		return dgAlert;
	}

	/**
	 * 消息提示框
	 * @param hContext 上下文指针
	 * @param tTitle 提示框标题
	 * @param tMessage 提示框内容
	 * @param tOK  提示框确认按钮文字
	 * @param l  确认后执行的命令
	 * @param tMiddle 提示框中间按钮文字
	 * @param l2 中间执行的命令
	 * @param tCancel 提示框取消的文字
	 * @return 返回对话框对象
	 */
	public static AlertDialog Messagebox(Context hContext,String tTitle,String tMessage,String tOK,DialogInterface.OnClickListener l,String tMiddle,DialogInterface.OnClickListener l2,String tCancel)
	{
		AlertDialog dgAlert = new AlertDialog.Builder(hContext).
				setTitle(tTitle).
                setMessage(tMessage).
                setCancelable(true).
                setPositiveButton(tOK, l).
                setNeutralButton(tMiddle, l2).
                setNegativeButton(tCancel, new OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

					}
				}).create();
        dgAlert.setCanceledOnTouchOutside(true);
		try {
			dgAlert.show();
		}
		catch (Exception ignored)
		{

		}
		return dgAlert;
	}

	/**
	 * 消息提示框
	 * @param hContext 上下文指针
	 * @param tMessage 提示框内容
	 * @param tOK  提示框确认按钮文字
	 * @param l  确认后执行的命令
	 * @return  返回对话框对象
	 */
	public static AlertDialog Messagebox(Context hContext,String tMessage,String tOK,DialogInterface.OnClickListener l)
	{
		AlertDialog dgAlert = new AlertDialog.Builder(hContext).
                setMessage(tMessage).
                setCancelable(false).
                setPositiveButton(tOK, l).create();
		try {
			dgAlert.show();
		}
		catch (Exception ignored)
		{

		}
		return dgAlert;
	}

	/**
	 * 消息输入框
	 * @param hContext 上下文指针
	 * @param tTitle 提示框标题
	 * @param jketText 自定义输入内容
	 * @param tOK  提示框确认按钮文字
	 * @param l  确认后执行的命令
	 * @return 返回对话框对象
	 */
	public static AlertDialog Inputbox(Context hContext,String tTitle,JKEditText jketText,String tOK,DialogInterface.OnClickListener l)
	{
		AlertDialog dgAlert = new AlertDialog.Builder(hContext).
                setMessage(tTitle).
                setView(jketText).
                setCancelable(true).
                setPositiveButton(tOK, l).create();
        dgAlert.setCanceledOnTouchOutside(true);
		try {
			dgAlert.show();
		}
		catch (Exception ignored)
		{

		}
		return dgAlert;
	}

	/**
	 * 消息提示框
	 * @param hContext 上下文指针
	 * @param tTitle 提示框标题
	 * @param vView 对话框界面对象
	 * @param tOK  提示框确认按钮文字
	 * @param l  确认后执行的命令
	 * @param tCancel 提示框取消的文字
	 * @return 返回对话框对象
	 */
	public static AlertDialog Viewbox(Context hContext,String tTitle,View vView,String tOK, DialogInterface.OnClickListener l,String tCancel)
	{
		AlertDialog dgAlert = new AlertDialog.Builder(hContext).
				setTitle(tTitle).
                setCancelable(true).
                setView(vView).
				setPositiveButton(tOK, l).
				setNegativeButton(tCancel, new OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        dgAlert.setCanceledOnTouchOutside(true);
		try {
			dgAlert.show();
		}
		catch (Exception ignored)
		{

		}
		return dgAlert;
	}

	/**
	 * 消息提示框
	 * @param hContext 上下文指针
	 * @param tTitle 提示框标题
	 * @param vView 对话框界面对象
	 * @return 返回对话框对象
	 */
	public static AlertDialog Viewbox(Context hContext,String tTitle,View vView)
	{
		AlertDialog dgAlert = new AlertDialog.Builder(hContext).
				setTitle(tTitle).
				setCancelable(true).
				setView(vView).create();
        dgAlert.setCanceledOnTouchOutside(true);
		try {
			dgAlert.show();
		}
		catch (Exception ignored)
		{

		}
		return dgAlert;
	}

	/**
	 * 消息选择框
	 * @param hContext 上下文指针
	 * @param tTitle 选择框标题
	 * @param a_tItems 选择框内容数组
	 * @param l 选择框监听事件
	 * @return 返回对话框对象
	 */
	public static AlertDialog Selectbox(Context hContext,String tTitle,String[] a_tItems,OnClickListener l)
	{
		AlertDialog dgAlert = new AlertDialog.Builder(hContext).
				setTitle(tTitle).
                setCancelable(true).
                setItems(a_tItems, l).create();
        dgAlert.setCanceledOnTouchOutside(true);
		try {
			dgAlert.show();
		}
		catch (Exception ignored)
		{

		}
		return dgAlert;
	}

    /**
     * 消息选择框
     * @param hContext 上下文指针
     * @param a_tItems 选择框内容数组
     * @param l 选择框监听事件
     * @return 返回对话框对象
     */
    public static AlertDialog Selectbox(Context hContext,String[] a_tItems,OnClickListener l)
    {
		AlertDialog dgAlert = new AlertDialog.Builder(hContext).
                setCancelable(true).
                setItems(a_tItems, l).create();
        dgAlert.setCanceledOnTouchOutside(true);
		try {
			dgAlert.show();
		}
		catch (Exception ignored)
		{

		}
        return dgAlert;
    }
}