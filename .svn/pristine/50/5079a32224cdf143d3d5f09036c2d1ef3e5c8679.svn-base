package com.lexinsmart.main;

import static com.lexinsmart.utils.Constants.ADDED_THING_DESCRIPTION;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lexinsmart.utils.Constants;
import com.lexinsmart.utils.DatabaseTools;
import com.lexinsmart.utils.HttpCommunications;
import com.lexinsmartmain.menu.AddDialog;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.widget.Toast;

public class MainTasks {

	private static final String TAG = "PreparativeTasks";

	private static final String BASE_URL_USER = "http://rest.open-smart.cn/api/users/";
	private static final String BASE_URL_THING = "http://rest.open-smart.cn/api/acls/";

	static Activity mActivity;
	static Context mContext;

	private MainTasks() {
	}

	static void setActivity(Activity activity) {
		mActivity = activity;
		mContext = mActivity.getBaseContext();
	}

	/**
	 * 在登录（获取令牌）之后，从服务器获取历史添加的THINGS
	 * 
	 * @author GeneralZRZ
	 * 
	 */
	static class SynchronizeThingsTask extends AsyncTask<String, Void, String> {
		String[][] things;

		/**
		 * 获取用户的其余信息，此处为id和email；获取历史添加的设备列表，存入本地数据库，通过Intent传给MainActivity
		 * 
		 * @param params
		 *            两个参数，依次为username、token。
		 */
		@Override
		protected String doInBackground(String... params) {

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
			System.out.println(responseThing);
			if (!HttpCommunications.getResponseKeyValue(responseThing,
					Constants.SUBSTR_MSG).equals("success")) {
				return "信息有误！";
			}

			String[][] jsonStringArray = analyticJson(responseThing);

			String[] responseThingSubStrs = responseThing
					.split(Constants.SUBSTR_UN);
			int thingLen = jsonStringArray.length;
			things = new String[thingLen][Constants.DB_THING_COLUMNS.length];
			System.out.println("PreparativeTasks.things.row: " + things.length
					+ ", line: " + (things.length == 0 ? 0 : things[0].length));

//			if (responseThing.contains("false")) { 														// TODO:
//				// 等秦飞给false加上双引号后改成getResponseKeyValue判断方式
//				return "同步设备列表成功！";				
//			}
			for (int i = 0; i < things.length; i++) {
				for (int j = 0; j < things[i].length; j++) { // 千万不要忘记给i加1！
					things[i][j] = jsonStringArray[i][j];

				}
			}
			for (int i = 0; i < things.length; i++) {
				DatabaseTools.updateTable(mContext, Constants.DB_THING,
						Constants.DB_THING_COLUMNS, things[i], false);
			}

			return "同步设备列表成功！";
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
			if (result.contains("成功")) {

				Message msg = MainActivity.mHandler.obtainMessage();
				msg.what = Constants.THING_ARRAY;
				Bundle data = new Bundle();
				data.putInt("things.length", things.length);
				for (int i = 0; i < things.length; i++) {
					data.putStringArray("things" + i, things[i]);
				}
				msg.setData(data);
				MainActivity.mHandler.sendMessage(msg);

			}
			Toast.makeText(mActivity, result, Toast.LENGTH_SHORT).show();
		}
	}

	public static class PostNewThingTask extends
			AsyncTask<String, Void, String> {
		String[] thing;

		@Override
		protected String doInBackground(String... params) {
			BasicNameValuePair pair6 = new BasicNameValuePair("username",
					params[0]);
			BasicNameValuePair pair7 = new BasicNameValuePair("token",
					params[1]);
			BasicNameValuePair[] pairsThing = new BasicNameValuePair[9];
			// for (int i = 0; i < pairsThing.length; i++) {
			// pairsThing[i] = new BasicNameValuePair(
			// Constants.DB_THING_COLUMNS[i],
			// AddDialog.mThingValues[i]);
			// }
			pairsThing[0] = new BasicNameValuePair(
					Constants.DB_THING_COLUMNS[3], AddDialog.mThingValues[0]);
			pairsThing[1] = new BasicNameValuePair(
					Constants.DB_THING_COLUMNS[4], AddDialog.mThingValues[1]);
			pairsThing[2] = new BasicNameValuePair(
					Constants.DB_THING_COLUMNS[2], AddDialog.mThingValues[2]);
			pairsThing[3] = new BasicNameValuePair(
					Constants.DB_THING_COLUMNS[6], AddDialog.mThingValues[3]);
			pairsThing[4] = new BasicNameValuePair(
					Constants.DB_THING_COLUMNS[5], AddDialog.mThingValues[4]);
			pairsThing[5] = new BasicNameValuePair(
					Constants.DB_THING_COLUMNS[7], AddDialog.mThingValues[5]);
			pairsThing[6] = new BasicNameValuePair(
					Constants.DB_THING_COLUMNS[8], AddDialog.mThingValues[6]);
			pairsThing[7] = new BasicNameValuePair(
					Constants.DB_THING_COLUMNS[10], AddDialog.mThingValues[7]);
			pairsThing[8] = new BasicNameValuePair(
					Constants.DB_THING_COLUMNS[9], AddDialog.mThingValues[8]);
			BasicNameValuePair pair8 = new BasicNameValuePair("rw", "1");
			String responseThing = HttpCommunications.getServerResponse(
					BASE_URL_THING + "insert/", new NameValuePair[] { pair6,
							pairsThing[0], pairsThing[1], pairsThing[2],
							pairsThing[3], pairsThing[4], pairsThing[5],
							pairsThing[6], pairsThing[7], pairsThing[8], pair8,
							pair7 });

			System.out
					.println("AddDialog.PostNewThing<doInBackground>.response of insert-thing : "
							+ responseThing);

			if (responseThing == null) {
				return "暂时无法访问网络！";
			}
			System.out.println(responseThing);
			if (!HttpCommunications.getResponseKeyValue(responseThing,
					Constants.SUBSTR_MSG).equals("success")) {
				return "信息有误！";
			}
			thing = new String[Constants.DB_THING_COLUMNS.length];
			if (responseThing.contains("false")) { // TODO:
				// 等秦飞给false加上双引号后改成getResponseKeyValue判断方式
				return "设备添加成功！";
			}
			for (int j = 0; j < thing.length; j++) {
				thing[j] = HttpCommunications.getResponseKeyValue(
						responseThing, Constants.SUBSTR_THINGS[j]);
			}
			DatabaseTools.updateTable(mContext, Constants.DB_THING,
					Constants.DB_THING_COLUMNS, thing, false);

			return "设备添加成功！";
		}

		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(mActivity, result, Toast.LENGTH_SHORT).show();

			if (result.contains("成功")) {
				// 将数据更新到ListView的新条目中
				Message msg = new Message();
				Bundle data = new Bundle();
				data.putStringArray(ADDED_THING_DESCRIPTION,
						AddDialog.mThingValues);
				msg.setData(data);
				MainActivity.mHandler.sendMessage(msg);
			}
			DatabaseTools.updateTable(mContext, Constants.DB_THING,
					Constants.DB_THING_COLUMNS, AddDialog.mThingValues, false);

		}
	}

	static class ModifyThingTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {

			return null;
		}

		@Override
		protected void onPostExecute(String result) {

		}
	}

	static class DeleteThingTask extends AsyncTask<String, Void, String> {
		String[] thing;

		@Override
		protected String doInBackground(String... params) {
			BasicNameValuePair pair1 = new BasicNameValuePair("id", params[0]);
			BasicNameValuePair pair2 = new BasicNameValuePair("username",
					params[1]);
			BasicNameValuePair pair3 = new BasicNameValuePair("topic",
					params[2]);
			BasicNameValuePair pair4 = new BasicNameValuePair("token",
					params[3]);

			String responseThing = HttpCommunications.getServerResponse(
					BASE_URL_THING + "remove/", new NameValuePair[] { pair1,
							pair2, pair3, pair4 });
			System.out.println("delete id" + params[0]);

			System.out.println("responseThing delete" + responseThing);
			if (responseThing.contains("success")) { // TODO:
				// 等秦飞给false加上双引号后改成getResponseKeyValue判断方式
				return "删除成功！";
			}

			else if (!responseThing.contains("success")) { // TODO:
				// 等秦飞给false加上双引号后改成getResponseKeyValue判断方式
				return "删除失败！";
			}
			return "你妹！";
		}

		@Override
		protected void onPostExecute(String result) {

			if (result.contains("成功")) {
				System.out
						.println("MainTasks.DeleteThingTask.onPostExecute()  >>>>>>>>  delete ok!");
			}
			if (result.contains("失败")) {
				System.out
						.println("MainTasks.DeleteThingTask.onPostExecute()  >>>>>>>>  delete 失败!");
			}
		}
	}

}
