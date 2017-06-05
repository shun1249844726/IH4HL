package com.lexinsmart.preparative;

import static com.lexinsmart.MyApplication.gCHANGE;
import static com.lexinsmart.MyApplication.gCREATE;
import static com.lexinsmart.MyApplication.gEMail;
import static com.lexinsmart.MyApplication.gLOGIN;
import static com.lexinsmart.MyApplication.gPassword;
import static com.lexinsmart.MyApplication.gPasswordNew;
import static com.lexinsmart.MyApplication.gRETRIEVE;
import static com.lexinsmart.MyApplication.gUserName;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lexinsmart.MyApplication;
import com.lexinsmart.R;

public class PreparativeActivity extends Activity {

	static ImageButton ibBack;
	static EditText etUn, etPw, etPwRe, etPwNew, etPwNewRe, etEmail;
	static Button btnSubmit;
	static TextView tvCreate, tvChange, tvRetrieve;
	static ProgressBar pbExec;
	static LinearLayout llContainer;
	static RelativeLayout rlContainer;

	static boolean mReLoginFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preparative);

		mReLoginFlag = getIntent().getBooleanExtra("relogingflag", false);

		ibBack = (ImageButton) findViewById(R.id.ib_back);
		etUn = (EditText) findViewById(R.id.et_un);
		etPw = (EditText) findViewById(R.id.et_pw);
		etPwRe = (EditText) findViewById(R.id.et_pw_re);
		etPwNew = (EditText) findViewById(R.id.et_pw_new);
		etPwNewRe = (EditText) findViewById(R.id.et_pw_new_re);
		etEmail = (EditText) findViewById(R.id.et_email);
		btnSubmit = (Button) findViewById(R.id.btn);
		tvCreate = (TextView) findViewById(R.id.tv_create);
		tvChange = (TextView) findViewById(R.id.tv_change);
		tvRetrieve = (TextView) findViewById(R.id.tv_retrieve);
		pbExec = (ProgressBar) findViewById(R.id.pb_executing);
		llContainer = (LinearLayout) findViewById(R.id.center_container);
		rlContainer = (RelativeLayout) findViewById(R.id.bottom_container);

		gLOGIN = getResources().getString(R.string.btn_login);
		gCREATE = getResources().getString(R.string.btn_create);
		gCHANGE = getResources().getString(R.string.btn_change);
		gRETRIEVE = getResources().getString(R.string.btn_retrieve);

		PreparativeTasks.setActivity(this);

		try2Login();
	}

	private void try2Login() {
		setViewsFocusState(false);
		new PreparativeTasks.Try2LoginTask().execute();
	}

	/**
	 * 当用户点击返回按钮时，先退回到Login界面
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (btnSubmit.getText() != MyApplication.gLOGIN) {
				login(btnSubmit);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	public void submitInfo(View v) {
		setViewsFocusState(false);

		String un = etUn.getText().toString();
		String pw = etPw.getText().toString();
		String pwRe = etPwRe.getText().toString();
		String pwNew = etPwNew.getText().toString();
		String pwNewRe = etPwNewRe.getText().toString();
		String email = etEmail.getText().toString();

		String tag = ((Button) v).getText().toString().trim();
		if (tag == gLOGIN) {
			if (un.equals("") || pw.equals("")) {
				Toast.makeText(this, "信息不完整！", Toast.LENGTH_SHORT).show();
				setViewsFocusState(true);
			} else {
				gUserName = un;
				gPassword = pw;
				new PreparativeTasks.LoginTask().execute(gUserName, gPassword);
			}
			return;
		}
		if (tag == gCREATE) { // XXX: 服务器“注册”功能暂时不需电话号码或邮箱地址
			if (un.equals("") || pw.equals("") || pwRe.equals("") || email.equals("")) {
				Toast.makeText(this, "信息不完整！", Toast.LENGTH_SHORT).show();
				setViewsFocusState(true);
			} else if (!pw.equals(pwRe)) {
				Toast.makeText(this, "两个密码不一致", Toast.LENGTH_SHORT).show();
				setViewsFocusState(true);
			} else if (!checkEMailValidability(email)) {
				Toast.makeText(this, "邮箱格式有误！", Toast.LENGTH_SHORT).show();
				setViewsFocusState(true);
			} else {
				gUserName = un;
				gPassword = pw;
				gEMail = email;
				new PreparativeTasks.CreateAccountTask().execute(gUserName, gPassword, gEMail);
			}
			return;
		}
		if (tag == gCHANGE) {
			if (un.equals("") || pw.equals("") || pwNew.equals("") || pwNewRe.equals("")) {
				Toast.makeText(this, "信息不完整！", Toast.LENGTH_SHORT).show();
				setViewsFocusState(true);
			} else if (!pwNew.equals(pwNewRe)) {
				Toast.makeText(this, "两个新密码不一致！", Toast.LENGTH_SHORT).show();
				setViewsFocusState(true);
			} else if (pwNew.equals(pw)) {
				Toast.makeText(this, "新密码与旧密码相同！", Toast.LENGTH_SHORT).show();
				setViewsFocusState(true);
			} else {
				gUserName = un;
				gPassword = pw;
				gPasswordNew = pwNew;
				new PreparativeTasks.ChangePasswordTask().execute(gUserName, gPassword, gPasswordNew);
			}
		}
		if (tag == gRETRIEVE) { // XXX: 服务器暂无“找回密码”功能
			if (un.equals("") || email.equals("")) {
				Toast.makeText(this, "信息不完整！", Toast.LENGTH_SHORT).show();
				setViewsFocusState(true);
			} else if (!checkEMailValidability(email)) {
				Toast.makeText(this, "邮箱格式有误！", Toast.LENGTH_SHORT).show();
				setViewsFocusState(true);
			} else {
				gUserName = un;
				gEMail = email;
				new PreparativeTasks.RetrievePasswordTask().execute(gUserName, gEMail);
			}
			return;
		}
	}

	/**
	 * 更新登录界面
	 * 
	 * @param v
	 */
	public void login(View v) {
		ibBack.setVisibility(View.GONE);
		etUn.setVisibility(View.VISIBLE);
		etPw.setVisibility(View.VISIBLE);
		etPwRe.setVisibility(View.GONE);
		etPwNew.setVisibility(View.GONE);
		etPwNewRe.setVisibility(View.GONE);
		etEmail.setVisibility(View.GONE);
		pbExec.setVisibility(View.GONE);
		btnSubmit.setVisibility(View.VISIBLE);
		rlContainer.setVisibility(View.VISIBLE);

		btnSubmit.setText(R.string.btn_login);

	}

	/**
	 * 更新创建账户界面
	 * 
	 * @param v
	 */
	public void createAccount(View v) {
		ibBack.setVisibility(View.VISIBLE);
		etUn.setVisibility(View.VISIBLE);
		etPw.setVisibility(View.VISIBLE);
		etPwRe.setVisibility(View.VISIBLE);
		etPwNew.setVisibility(View.GONE);
		etPwNewRe.setVisibility(View.GONE);
		etEmail.setVisibility(View.VISIBLE);
		btnSubmit.setVisibility(View.VISIBLE);
		pbExec.setVisibility(View.GONE);
		rlContainer.setVisibility(View.GONE);

		btnSubmit.setText(R.string.btn_create);
	}

	/**
	 * 更新修改密码界面
	 * 
	 * @param v
	 */
	public void changePassword(View v) {
		ibBack.setVisibility(View.VISIBLE);
		etUn.setVisibility(View.VISIBLE);
		etPw.setVisibility(View.VISIBLE);
		etPwRe.setVisibility(View.GONE);
		etPwNew.setVisibility(View.VISIBLE);
		etPwNewRe.setVisibility(View.VISIBLE);
		etEmail.setVisibility(View.GONE);
		btnSubmit.setVisibility(View.VISIBLE);
		pbExec.setVisibility(View.GONE);
		rlContainer.setVisibility(View.GONE);

		btnSubmit.setText(R.string.btn_change);
	}

	/**
	 * 更新找回密码界面
	 * 
	 * @param v
	 */
	public void retrievePassword(View v) {
		ibBack.setVisibility(View.VISIBLE);
		etUn.setVisibility(View.VISIBLE);
		etPw.setVisibility(View.GONE);
		etPwRe.setVisibility(View.GONE);
		etPwNew.setVisibility(View.GONE);
		etPwNewRe.setVisibility(View.GONE);
		etEmail.setVisibility(View.VISIBLE);
		btnSubmit.setVisibility(View.VISIBLE);
		pbExec.setVisibility(View.GONE);
		rlContainer.setVisibility(View.GONE);

		btnSubmit.setText(R.string.btn_retrieve);
	}

	public static void setViewsFocusState(boolean flag) {
		ibBack.setEnabled(flag);
		etUn.setEnabled(flag);
		etPw.setEnabled(flag);
		etPwRe.setEnabled(flag);
		etPwNew.setEnabled(flag);
		etPwNewRe.setEnabled(flag);
		etEmail.setEnabled(flag);
		btnSubmit.setEnabled(flag);
		tvCreate.setEnabled(flag);
		tvChange.setEnabled(flag);
		tvRetrieve.setEnabled(flag);
		pbExec.setVisibility(flag ? View.GONE : View.VISIBLE);
	}

	private static boolean checkEMailValidability(String emailStr) {
		Pattern pattern = Pattern.compile(
				"^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher matcher = pattern.matcher(emailStr);
		return matcher.matches();
	}

}
