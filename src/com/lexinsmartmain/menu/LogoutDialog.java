package com.lexinsmartmain.menu;

import com.lexinsmart.preparative.PreparativeActivity;
import com.lexinsmart.utils.Constants;
import com.lexinsmart.utils.DatabaseTools;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

public class LogoutDialog extends MenuItemDialog {

	public LogoutDialog(Activity activity, Drawable icon, String title, String message) {
		super(activity, icon, title, message);

	}

	@Override
	protected Object[] initDefaultButtonListeners() {
		DialogInterface.OnClickListener positiveBtnListener = new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO: 退出登录，异步任务清除本地数据，同时播放动画，直至清除完毕，然后更新主界面
				// 跳转到App首次开启的Activity实例，用户名已填写，密码未填写，突出显示登录按钮，隐晦显示注册按钮、找回密码按钮
				Toast.makeText(mActivity, "Logging out...", Toast.LENGTH_SHORT).show();
				DatabaseTools.deleteLocalDBTable(mActivity, Constants.DB_USER);
				Intent intent = new Intent(mActivity, PreparativeActivity.class);
				intent.putExtra("reloginflag", true);
				mActivity.startActivity(intent);
				mActivity.finish();
			}
		};
		DialogInterface.OnClickListener negativeBtnListener = new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		};
		return new Object[] { "Confirm", null, "Cancel", positiveBtnListener, null, negativeBtnListener };
	}

	@Override
	protected Object initCustomContentViews() {
		return null;
	}

	@Override
	protected void updateCustomContentViews() {

	}

}