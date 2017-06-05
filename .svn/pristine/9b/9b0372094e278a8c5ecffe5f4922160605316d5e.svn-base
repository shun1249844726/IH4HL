package com.lexinsmartmain.menu;

import java.lang.reflect.Field;

import android.content.DialogInterface;

public class DialogUtils {

	public static void setDialogDismissable(DialogInterface dialog, boolean flag) {
		try {
			Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, flag); // 将mShowing变量设为false，表示对话框已关闭
			dialog.dismiss();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
