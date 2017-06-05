package com.lexinsmart.preparative;

import static com.lexinsmart.MyApplication.gEMail;
import static com.lexinsmart.MyApplication.gId;
import static com.lexinsmart.MyApplication.gPassword;
import static com.lexinsmart.MyApplication.gSUCCESS;
import static com.lexinsmart.MyApplication.gToken;
import static com.lexinsmart.MyApplication.gUserName;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.lexinsmart.main.MainActivity;
import com.lexinsmart.utils.Constants;
import com.lexinsmart.utils.DatabaseTools;
import com.lexinsmart.utils.HttpCommunications;

public class PreparativeTasks {

	private static final String TAG = "PreparativeTasks";

	private static final String BASE_URL_USER = "http://rest.open-smart.cn/api/users/";
	private static final String BASE_URL_THING = "http://rest.open-smart.cn/api/acls/";

	static PreparativeActivity mActivity;
	static Context mContext;

	private PreparativeTasks() {
	}

	static void setActivity(PreparativeActivity activity) {
		mActivity = activity;
		mContext = mActivity.getBaseContext();
	}

	/**
	 * 
	 * @author GeneralZRZ
	 * 
	 */
	static class Try2LoginTask extends AsyncTask<String, Void, String[]> {
		@Override
		protected String[] doInBackground(String... params) {
			String[] unAndPw = DatabaseTools.queryUnAndPw(mContext,
					Constants.DB_USER, new String[] { "username", "password" });
			return unAndPw;
		}

		@Override
		protected void onPostExecute(String[] result) {
			if (result != null && !result[0].equals("")
					&& !result[1].equals("")) {
				gUserName = result[0];
				gPassword = result[1];
				new LoginTask().execute(gUserName, gPassword);
			} else {
				PreparativeActivity.setViewsFocusState(true);
			}
		}
	}

	/**
	 * 登录
	 * 
	 * @author generalzrz
	 * 
	 */
	static class LoginTask extends AsyncTask<String, Void, String> {
		/**
		 * @param params
		 *            两个参数，先后为：username，password
		 */
		@Override
		protected String doInBackground(String... params) {
			NameValuePair pair1 = new BasicNameValuePair("username", params[0]);
			NameValuePair pair2 = new BasicNameValuePair("password", params[1]);
			String response = HttpCommunications.getServerResponse(
					BASE_URL_USER + "get_token/", new NameValuePair[] { pair1,
							pair2 });
			if (response == null) {
				return "暂时无法访问网络！";
			}

			System.out.println(TAG + "-LoginTask-" + response);

			if (HttpCommunications.getResponseKeyValue(response,
					Constants.SUBSTR_MSG).equals("success")) {
				gToken = HttpCommunications.getResponseKeyValue(response,
						Constants.SUBSTR_TOKEN);
				DatabaseTools.updateTable(mContext, Constants.DB_USER,
						Constants.DB_USER_COLUMNS, new String[] { gId,
								gUserName, gPassword, gEMail, gToken }, true);
				return "登录成功！";
			} else {
				return "用户名或密码错误！\n或者您尚未注册！";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(mActivity, result, Toast.LENGTH_SHORT).show();
			if (!PreparativeActivity.mReLoginFlag) {
				if (result.contains("成功")) {
					new SynchronizeDataTask().execute(gUserName, gToken);
				} else {
					PreparativeActivity.setViewsFocusState(true);
				}
			}
		}
	}

	/**
	 * 注册
	 * <p>
	 * XXX: 注册需要电话号码或电子邮件作为联系方式，但此处暂时没有用到，因为服务器端不需要
	 * 
	 * @author generalzrz
	 * 
	 */
	static class CreateAccountTask extends AsyncTask<String, Void, String> {
		/**
		 * @param params
		 *            三个参数，先后为：username，password，email。
		 */
		@Override
		protected String doInBackground(String... params) {
			NameValuePair pair1 = new BasicNameValuePair("username", params[0]);
			NameValuePair pair2 = new BasicNameValuePair("password", params[1]);
			NameValuePair pair3 = new BasicNameValuePair("email", params[2]);
			String response = HttpCommunications.getServerResponse(
					BASE_URL_USER + "insert/", new NameValuePair[] { pair1,
							pair2, pair3 });
			if (response == null) {
				return "暂时无法访问网络！";
			}

			System.out.println(response);

			if (HttpCommunications.getResponseKeyValue(response,
					Constants.SUBSTR_MSG).equals("success")) {
				gId = HttpCommunications.getResponseKeyValue(response,
						Constants.SUBSTR_ID);
				DatabaseTools.updateTable(mContext, Constants.DB_USER,
						Constants.DB_USER_COLUMNS, new String[] { gId,
								gUserName, gPassword, gEMail, gToken }, true);
				return "账户创建成功！";
			} else {
				return "用户名不可用或者邮箱格式有误！";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(mActivity, result, Toast.LENGTH_SHORT).show();
			PreparativeActivity.setViewsFocusState(true);

			if (result.contains("成功")) {
				mActivity.login(PreparativeActivity.btnSubmit);
			}
		}
	}

	/**
	 * 修改密码
	 * <p>
	 * TODO: 暂未实现
	 * 
	 * @author GeneralZRZ
	 * 
	 */
	static class ChangePasswordTask extends AsyncTask<String, Void, String> {
		/**
		 * @param params
		 *            三个参数，username，原password，新password
		 */
		@Override
		protected String doInBackground(String... params) {

			return null;
		}

		@Override
		protected void onPostExecute(String result) {

		}
	}

	/**
	 * 找回密码
	 * <p>
	 * XXX: “找回密码”功能尚未在服务器端实现
	 * 
	 * @author generalzrz
	 * 
	 */
	static class RetrievePasswordTask extends AsyncTask<String, Void, String> {
		/**
		 * @param params
		 *            两个参数，先后为：username，contactinfo
		 */
		@Override
		protected String doInBackground(String... params) {
			return null;
		}

		@Override
		protected void onPostExecute(String result) {

		}
	}

	/**
	 * 在登录（获取令牌）之后，从服务器获取其他USERINFO以及历史添加的THINGS
	 * 
	 * @author GeneralZRZ
	 * 
	 */
	private static class SynchronizeDataTask extends
			AsyncTask<String, Void, String> {
		String[][] things;

		/**
		 * 获取用户的其余信息，此处为id和email；获取历史添加的设备列表，存入本地数据库，通过Intent传给MainActivity
		 * 
		 * @param params
		 *            两个参数，依次为username、token。
		 */
		@Override
		protected String doInBackground(String... params) {// 获取用户的其余信息，此处为id和email
			NameValuePair pair1 = new BasicNameValuePair("username", params[0]);
			NameValuePair pair2 = new BasicNameValuePair("token", params[1]);
			String responseUser = HttpCommunications.getServerResponse(
					BASE_URL_USER + "list/",
					new NameValuePair[] { pair1, pair2 });
			if (responseUser == null) {
				return "暂时无法访问网络！";
			}
			System.out.println(responseUser);
			if (!HttpCommunications.getResponseKeyValue(responseUser,
					Constants.SUBSTR_MSG).equals("success")) {
				return "您填写的信息TMD有误！";
			}
			gId = HttpCommunications.getResponseKeyValue(responseUser,
					Constants.SUBSTR_ID);
			gEMail = HttpCommunications.getResponseKeyValue(responseUser,
					Constants.SUBSTR_EM);
			DatabaseTools.updateTableColumns(mContext, Constants.DB_USER,
					new String[] { Constants.DB_USER_COLUMNS[0],
							Constants.DB_USER_COLUMNS[3] }, new String[] { gId,
							gEMail });
			// 先清空数据库的THINGS表格
			DatabaseTools.updateTable(mContext, Constants.DB_THING,
					Constants.DB_THING_COLUMNS, new String[] {}, true);
			// 获取历史添加的设备列表，存入本地数据库，通过Intent传给MainActivity
			NameValuePair pair6 = new BasicNameValuePair("username", params[0]);
			NameValuePair pair7 = new BasicNameValuePair("token", params[1]);

			String responseThing = HttpCommunications.getServerResponse(
					BASE_URL_THING + "list/", new NameValuePair[] { pair6,
							pair7 });
			if (responseThing == null) {
				return "暂时无法访问网络！";
			}

			if (!HttpCommunications.getResponseKeyValue(responseThing,
					Constants.SUBSTR_MSG).equals("success")) {
				return "您填写的信息有误！";
			}

			String[][] jsonStringArray = analyticJson(responseThing);
//
//			for (int i = 0; i < jsonStringArray.length; i++) {
//				for (int j = 0; j < jsonStringArray[0].length; j++) {
//
//				}
//			}

			// String[] responseThingSubStrs = responseThing
			// .split(Constants.SUBSTR_UN);
			//
			// for (int i = 0; i < responseThingSubStrs.length; i++) {
			// System.out.println("responseThingSubStrs:"
			// + responseThingSubStrs[i]);
			// }

			int thingLen = jsonStringArray.length;

			things = new String[thingLen][Constants.DB_THING_COLUMNS.length];

			System.out.println("PreparativeTasks.things.row: " + things.length
					+ ", line: " + (things.length == 0 ? 0 : things[0].length));
			System.out.println("responseThing.contains " +responseThing);

			if (responseThing.contains("false")) { // TODO:
													// 等秦飞给false加上双引号后改成getResponseKeyValue判断方式
				return gSUCCESS;
			}
			for (int i = 0; i < things.length; i++) {
				for (int j = 0; j < things[i].length; j++) { 
					things[i][j] = jsonStringArray[i][j];
					
				}
			}
			for (int i = 0; i < things.length; i++) {
				DatabaseTools.updateTable(mContext, Constants.DB_THING,
						Constants.DB_THING_COLUMNS, things[i], false);
			}

			return gSUCCESS;
		}

		private String[][] analyticJson(String responseThing) {
			// TODO Auto-generated method stub

			try {
				JSONObject jsonObject = new JSONObject(responseThing)
						.getJSONObject("data");
				JSONArray jsonArray = jsonObject.getJSONArray("items");

				int lenght = jsonArray.length();
				String[][] ss = new String[lenght][11];// 为了演示方便让其返回String[]

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jo = (JSONObject) jsonArray.opt(i);

					ss[i][0] = jo.getString("id");
					ss[i][1] = jo.getString("username");
					ss[i][2] = jo.getString("topic");
					ss[i][3] = jo.getString("thingname");
					ss[i][4] = jo.getString("type");
					ss[i][5] = jo.getString("max");
					ss[i][6] = jo.getString("min");
					ss[i][7] = jo.getString("step");
					ss[i][8] = jo.getString("unit");
					ss[i][9] = jo.getString("status");
					ss[i][10] = jo.getString("brief");
				}

				return ss;

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			if (result == gSUCCESS) {

				
				Intent intent = new Intent(mActivity, MainActivity.class);
				Bundle bundle = new Bundle();
				for (int i = 0; i < things.length; i++) {
					bundle.putStringArray("things" + i, things[i]);

				}
				intent.putExtra("things.length", things.length);
				
				intent.putExtra("things", bundle);
				mActivity.startActivity(intent);
				mActivity.finish();

				return;
			}
			Toast.makeText(mActivity,"result"+ result, Toast.LENGTH_SHORT).show();

			PreparativeActivity.setViewsFocusState(true);

		}
	}

}
