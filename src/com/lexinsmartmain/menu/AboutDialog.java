package com.lexinsmartmain.menu;

import android.app.Activity;
import android.graphics.drawable.Drawable;

public class AboutDialog extends MenuItemDialog {

	public AboutDialog(Activity activity, Drawable icon, String title, String message) {
		super(activity, icon, title, message);

	}

	@Override
	protected Object[] initDefaultButtonListeners() {

		return null;
	}

	@Override
	protected Object initCustomContentViews() {

		return null;
	}

	@Override
	protected void updateCustomContentViews() {
		
	}
	
}
