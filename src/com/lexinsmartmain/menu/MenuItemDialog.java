package com.lexinsmartmain.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.view.View;

public abstract class MenuItemDialog {

	protected static Activity mActivity = null;
	protected static Context mContext = null;

	protected AlertDialog.Builder mBuilder = null;

	protected AlertDialog mDialog = null;

	public MenuItemDialog(Activity activity, Drawable icon, String title, String message) {
		mActivity = activity;
		mContext = mActivity.getBaseContext();
		mBuilder = new AlertDialog.Builder(mActivity).setIcon(icon).setTitle(title);
		if (message != null) {
			mBuilder.setMessage(message);
		}
		setUI();
		mDialog = mBuilder.create();
		updateCustomContentViews();
	}

	// public MenuItemDialog(Activity activity, int icon, int title, int
	// message) {
	//
	// setUI();
	// mDialog = new
	// AlertDialog.Builder(activity).setIcon(icon).setTitle(title).setMessage(message).create();
	// }

	/**
	 * 在AlertDialog.create()前执行该方法
	 * <p>
	 * 复写该方法，实现：返回对话框的三个按钮所要显示的字符串 & 三个按钮各自对应的触控监听器实例
	 * 
	 * @return 前三位为String类型（对应对话框按钮的字符串），后三位为OnClickListener类型（对应对话框按钮的触控监听器实例）
	 */
	protected abstract Object[] initDefaultButtonListeners();

	/**
	 * 在AlertDialog.create()前执行该方法
	 * <p>
	 * 复写该方法，实现：返回对话框所要加载的xml布局对应的View实例
	 * 
	 * @return 第一为View型（对应布局文件的View实例）
	 */
	protected abstract Object initCustomContentViews();

	/**
	 * 在AlertDialog.create()后执行该方法
	 * <p>
	 * 复写该方法，实现：为该布局内部视图添加响应，使之能够对用户操作的作出相应的更新
	 */
	protected abstract void updateCustomContentViews();

	private void setUI() {
		Object[] dialogBtns = initDefaultButtonListeners();
		if (dialogBtns != null) {
			if (dialogBtns[0] != null) {
				mBuilder.setPositiveButton((String) dialogBtns[0], (OnClickListener) dialogBtns[0 + 3]);
			}
			if (dialogBtns[0] != null) {
				mBuilder.setNeutralButton((String) dialogBtns[1], (OnClickListener) dialogBtns[1 + 3]);
			}
			if (dialogBtns[0] != null) {
				mBuilder.setNegativeButton((String) dialogBtns[2], (OnClickListener) dialogBtns[2 + 3]);
			}
		}

		Object dialogViews = initCustomContentViews();
		if (dialogViews != null) {
			mBuilder.setView((View) dialogViews);
		}
	}

	public void setCancelable(boolean flag) {
		mDialog.setCancelable(flag);
	}

	public void show() {
		mDialog.show();

	}

	public void hide() {
		mDialog.hide();
	}

	public void dismiss() {
		mDialog.dismiss();
	}

}
