package com.lexinsmart.main;

import static com.lexinsmart.utils.Constants.MEMU_ITEM_DRAWABLES;
import static com.lexinsmart.utils.Constants.THINGS_ICONS;
import static com.lexinsmart.utils.Constants.THINGS_TYPES;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.capricorn.ArcMenu;
import com.espressif.iot.esptouch.demo_activity.EsptouchDemoActivity;
import com.handmark.pull2refresh.lib.PullToRefreshBase;
import com.handmark.pull2refresh.lib.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pull2refresh.lib.PullToRefreshListView;
import com.lexinsmart.MyApplication;
import com.lexinsmart.R;
import com.lexinsmart.mqtt.MqttV3Service;
import com.lexinsmart.utils.Constants;
import com.lexinsmartmain.menu.AboutDialog;
import com.lexinsmartmain.menu.AddDialog;
import com.lexinsmartmain.menu.LogoutDialog;
import com.lexinsmartmain.menu.MenuItemDialog;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity {

	private static Activity mActivity;
	private String strResult = "";
	private static int startFlag = 0;
	int receiveFlag = 0;
	static int Qos = 1;
	ListView lvDevices;
	ProgressBar pbExecuting;
	private static PullToRefreshListView ptrDevices;
	private static MyAdapter adapter;
	private static ArrayList<HashMap<String, Object>> listItem;

	private static ArrayList<String> topicList;
	static Handler mHandler = new AddedThingHandler();
	static ViewHolder mViews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mActivity = this;
		// pbExecuting = (ProgressBar) findViewById(R.id.pb_executing_main);

		MainTasks.setActivity(mActivity);

		Intent intent = getIntent();
		String[][] things = new String[intent.getIntExtra("things.length", 0)][Constants.DB_THING_COLUMNS.length];
		for (int i = 0; i < things.length; i++) {
			things[i] = intent.getBundleExtra("things").getStringArray(
					"things" + i);
		}

		for(int i=0;i<things.length;i++)
		{
			System.out.println("things" + i +"   "+things[i][0]);

		}
		int[] thingIcons = new int[things.length];
		String[] thingNames = new String[things.length];
		String[] thingId = new String[things.length];
		// String[] thingBriefs = new String[things.length];
		String[] thingTopic = new String[things.length];
		topicList = new ArrayList<String>();

		for (int i = 0; i < things.length; i++) {
			for (int j = 0; j < THINGS_TYPES.length; j++) {
				if ((things[i][4]).toLowerCase(Locale.ENGLISH).equals(
						(THINGS_TYPES[j]).toLowerCase())) {
					thingIcons[i] = THINGS_ICONS[j];
				}
			}
			thingNames[i] = things[i][3];
			// thingBriefs[i] = things[i][7];
			thingTopic[i] = things[i][2];
			thingId[i] = things[i][0];
			topicList.add(thingTopic[i]);
		}
		listItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < things.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("icon", thingIcons[i]);
			map.put("name", thingNames[i]);
			// map.put("brief", thingBriefs[i]);
			map.put("topic", thingTopic[i]);
			map.put("id", thingId[i]);

			listItem.add(map);
		}
		adapter = new MyAdapter(this);

		ptrDevices = (PullToRefreshListView) findViewById(R.id.main_ptr_things);
		ptrDevices.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			String label = "";

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				label = DateUtils.formatDateTime(getApplicationContext(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label); // 显示上次更新时间
				new MainTasks.SynchronizeThingsTask().execute(
						MyApplication.gUserName, MyApplication.gToken);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				new AsyncTask<Void, Void, String>() {
					@Override
					protected String doInBackground(Void... params) {
						return null;
					}

					@Override
					protected void onPostExecute(String s) {
						ptrDevices.onRefreshComplete();
					}
				}.execute();
			}
		});
		lvDevices = ptrDevices.getRefreshableView();
		lvDevices.setAdapter(adapter);
		lvDevices.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				final int deletePosition = position;
				final String itemTopic = (String) listItem.get(position - 1)
						.get("topic");
				final String itemId = (String) listItem.get(position - 1).get(
						"id");

				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.this).setTitle("删除设备")
						.setMessage("是否删除设备");
				builder.setPositiveButton("yes",
						new android.content.DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								int size = listItem.size();
								if (size > 0) {
						//			listItem.remove(deletePosition);
									adapter.notifyDataSetChanged();
									new MainTasks.DeleteThingTask().execute(
											itemId, MyApplication.gUserName,
											itemTopic, MyApplication.gToken);
								}

							}

						});
				builder.setNegativeButton("no",
						new android.content.DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}

						});
				builder.create().show();
				return true;
			}
		});
		lvDevices.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 获取此时被点击条目的type和thingname值，通过intent传给DetailsActivity
				// switch
				Intent intent = new Intent();
				Bundle bundle = new Bundle();

				switch ((Integer) listItem.get(position - 1).get("icon")) {
				case R.drawable.type_switch:
					bundle.putString("type", "Switch");
					break;

				case R.drawable.type_range:
					bundle.putString("type", "Slider");
					break;

				case R.drawable.type_rgb:
					bundle.putString("type", "RGB");
					break;

				case R.drawable.type_json:
					bundle.putString("type", "ReadOnly");
					break;

				default:
					break;
				}
				bundle.putInt("position", position);
				intent.putExtras(bundle);
				intent.setClass(MainActivity.this, DetailsActivity.class);
				startActivity(intent);
			}
		});

		ArcMenu amMenu = (ArcMenu) findViewById(R.id.amMenu);
		initArcMenu(amMenu, MEMU_ITEM_DRAWABLES);

		new Thread(new MqttProcThread()).start();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem menuItem3 = menu.add(1001, 100, 1, "菜单三");
		menuItem3.setTitle("配置设备");
		menuItem3.setShortcut('c', 'c');
		MenuItem menuItem4 = menu.add(1001, 101, 2, "菜单四");
		menuItem4.setTitle("退出程序");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case 100:
			Intent intent_3 = new Intent(MainActivity.this,
					EsptouchDemoActivity.class);
			item.setIntent(intent_3);
			break;
		case 101:
			onKeydown_back();
			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);

	}

	public void onKeydown_back() {
		AlertDialog.Builder build = new AlertDialog.Builder(this);
		build.setTitle("系统提示").setMessage("确定要退出吗？");
		build.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
				System.exit(0);
				android.os.Process.killProcess(android.os.Process.myPid());
				Toast.makeText(MainActivity.this, "Bye-bye,honey!", 0).show();
			}
		});
		build.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		}).show();
	}

	public class MqttProcThread implements Runnable {
		int randomid = (int) Math.floor(10000 + Math.random() * 90000);

		public void run() {
			Message msg = new Message();

			boolean ret = MqttV3Service.connectionMqttServer(myHandler,
					MyApplication.ADDRESS, MyApplication.PORT, "lexin"
							+ randomid, topicList, MyApplication.gPassword); // XXX

			if (ret) {
				msg.what = 1;
			} else {
				msg.what = 0;
			}
			msg.obj = strResult;
			myHandler.sendMessage(msg);
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				Toast.makeText(MainActivity.this, "连接成功！", Toast.LENGTH_SHORT)
						.show();

			} else if (msg.what == 0) {
				Toast.makeText(MainActivity.this, "连接失败！", Toast.LENGTH_SHORT)
						.show();

			} else if (msg.what == 2) {
				String strContent = "";
				Time time = new Time("GMT+8");
				strContent += msg.getData().getString("content");

				if (strContent.equals("on")) {
					receiveFlag = 1;
				} else if (strContent.equals("off")) {
					receiveFlag = 1;
				}
				time.setToNow();
				int hour = time.hour + 8;
				int minute = time.minute;
				System.out.print(strContent + " -- [" + hour + ":" + minute
						+ ":" + time.second + "]\n");
			} else if (msg.what == 3) {
				if (MqttV3Service.closeMqtt()) {
					Toast.makeText(MainActivity.this, "已经断开连接！",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	};

	private void initArcMenu(ArcMenu menu, int[] itemDrawables) {
		final int itemCount = itemDrawables.length;
		for (int i = 0; i < itemCount; i++) {
			ImageView item = new ImageView(this);
			item.setImageResource(itemDrawables[i]);
			final int position = i;
			menu.addItem(item, new OnClickListener() {

				@Override
				public void onClick(View v) {

					switch (position) { // TODO: 重构成DialogFactory类
					case 0:
						// Add
						AddDialog addDialog = new AddDialog(mActivity,
								mActivity.getResources().getDrawable(
										R.drawable.ic_launcher), "添加设备", null);
						addDialog.setCancelable(false);
						addDialog.show();
						break;
					case 1:
						// Search，从本地数据库搜索数据

						break;
					case 2:
						// Logout
						MenuItemDialog logoutDialog = new LogoutDialog(
								mActivity, mActivity.getResources()
										.getDrawable(R.drawable.ic_launcher),
								"Logout",
								"Are you sure to log out ? Your data stored to local will be wiped out. ");
						logoutDialog.show();
						break;
					case 3:
						// About
						MenuItemDialog aboutDialog = new AboutDialog(mActivity,
								mActivity.getResources().getDrawable(
										R.drawable.ic_launcher), "About",
								"About this application...adj;faoidjfefqwnefakdsnvaadfja;lsdfjasdfjnma;lskfja");
						aboutDialog.show();
						break;
					}

				}
			});
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}
		String newTopic = data.getStringExtra("QRresult");
		System.out.println("MainActivity<onActivityResult>.newTopic ---> "
				+ newTopic);
		System.out.println("MainActivity<onActivityResult>.requestCode == "
				+ requestCode);
		System.out.println("MainActivity<onActivityResult>.resultCode == "
				+ resultCode);
		if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK
				&& newTopic != null) {
			AddDialog.mEtTopic.setText(newTopic);
		}
	}

	private static final class ViewHolder {
		public ImageView ivType;
		public TextView tvTn;
		public TextView tvTopic;
		public ToggleButton tbSwitch;
		public SeekBar sbRange;
		public TextView tvValue;
		public ToggleButton tbRgb;
	}

	private static class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return listItem.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			mViews = null;
			if (convertView == null) {
				mViews = new ViewHolder();

				convertView = mInflater.inflate(R.layout.main_list_item, null);
				mViews.ivType = (ImageView) convertView
						.findViewById(R.id.main_thinglist_item_img);
				mViews.tvTn = (TextView) convertView
						.findViewById(R.id.item_tv_name);
				mViews.tvTopic = (TextView) convertView
						.findViewById(R.id.item_tv_topic);
				mViews.tbSwitch = (ToggleButton) convertView
						.findViewById(R.id.item_tb_switch);
				mViews.sbRange = (SeekBar) convertView
						.findViewById(R.id.item_sb_range);
				mViews.tvValue = (TextView) convertView
						.findViewById(R.id.item_tv_value);
				mViews.tbRgb = (ToggleButton) convertView
						.findViewById(R.id.item_btn_rgb);
				convertView.setTag(mViews);
			} else {
				mViews = (ViewHolder) convertView.getTag();
			}

			mViews.ivType.setBackgroundResource((Integer) listItem
					.get(position).get("icon"));
			String itemTopic = (String) listItem.get(position).get("topic");

			mViews.tvTn.setText((String) listItem.get(position).get("name"));
			mViews.tvTopic.setText(itemTopic);
			mViews.tbRgb.setVisibility(View.INVISIBLE);
			mViews.sbRange.setVisibility(View.INVISIBLE);
			mViews.tbSwitch.setVisibility(View.INVISIBLE);
			mViews.tvValue.setVisibility(View.INVISIBLE);
			switch ((Integer) listItem.get(position).get("icon")) {
			case R.drawable.type_rgb:
				mViews.tbRgb.setVisibility(View.VISIBLE);
				mViews.tbRgb
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								// TODO Auto-generated method stub
								if (isChecked) {
									MqttV3Service.publishMsg(
											"LIG:" + 254 + ";", Qos, position);
								} else if (!isChecked) {
									MqttV3Service.publishMsg("LIG:" + 0 + ";",
											Qos, position);
								}

							}
						});
				break;
			case R.drawable.type_range:
				mViews.sbRange.setVisibility(View.VISIBLE);
				mViews.sbRange
						.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
							@Override
							public void onProgressChanged(SeekBar seekBar,
									int progress, boolean fromUser) {
								// TODO: 向MQTT服务发送用户控制数据 & 本视图（当服务返回数据时用于更新该视图）
								// TODO: 此方法应该在DetailsActivity中复用
								MqttV3Service.publishMsg("LIG:" + progress
										+ ";", Qos, position);
							}

							@Override
							public void onStartTrackingTouch(SeekBar seekBar) {
							}

							@Override
							public void onStopTrackingTouch(SeekBar seekBar) {
							}
						});
				break;
			case R.drawable.type_switch:
				mViews.tbSwitch.setVisibility(View.VISIBLE);
				mViews.tbSwitch
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								// TODO: 向MQTT服务发送用户控制数据 & 本视图（当服务返回数据时用于更新该视图）
								// TODO: 此方法应该在DetailsActivity中复用
								MyApplication.TOPIC = (String) listItem.get(
										position).get("topic");
								System.out.println("topicList:" + topicList);

								if (isChecked) {
									MqttV3Service.publishMsg("LIG:" + 2 + ";",
											Qos, position);
								} else {
									MqttV3Service.publishMsg("LIG:" + 0 + ";",
											Qos, position);

								}

							}
						});
				break;
			case R.drawable.type_json:
				mViews.tvValue.setVisibility(View.VISIBLE);
				break;
			}
			return convertView;
		}

	}

	private static class AddedThingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.ONE_THING:
				System.out.println("Constants.ONE_THING");

				String[] addedThingValues = msg.getData().getStringArray(
						Constants.ADDED_THING_DESCRIPTION);
				for(int i =0;i<addedThingValues.length;i++)
				{
					System.out.println("addedThingValues"+addedThingValues[i]);

				}
				HashMap<String, Object> map1 = new HashMap<String, Object>();
				// 传入ListView新增条目的icon
				for (int i = 0; i < THINGS_TYPES.length; i++) {
					if ((addedThingValues[1]).toLowerCase(Locale.ENGLISH)
							.equals((THINGS_TYPES[i])
									.toLowerCase(Locale.ENGLISH))) {
						map1.put("icon", THINGS_ICONS[i]);
					}
				}
				// 传入ListView新增条目的name
				map1.put("name", addedThingValues[0]);
				// 传入ListView新增条目的brief
	//			map1.put("brief", "");
				listItem.add(map1);
				adapter.notifyDataSetChanged();
				ptrDevices.onRefreshComplete();
				break;
			case Constants.THING_ARRAY:
				System.out.println("Constants.THING_ARRAY");
				Bundle data = msg.getData();
				String[][] things = new String[data.getInt("things.length")][Constants.DB_THING_COLUMNS.length];
				for (int i = 0; i < things.length; i++) {
					things[i] = data.getStringArray("things" + i);
				}

				int[] thingIcons = new int[things.length];
				String[] thingNames = new String[things.length];
				String[] thingBriefs = new String[things.length];
				String[] thingTopic = new String[things.length];
				String[] thingId = new String [things.length];
				for (int i = 0; i < things.length; i++) {
					for (int j = 0; j < THINGS_TYPES.length; j++) {
						if ((things[i][4]).toLowerCase(Locale.ENGLISH).equals(
								(THINGS_TYPES[j]).toLowerCase())) {
							thingIcons[i] = THINGS_ICONS[j];
						}
					}
					thingNames[i] = things[i][3];
					// thingBriefs[i] = things[i][7];
					thingTopic[i] = things[i][2];
					thingId[i] = things[i][0];
				}
				listItem.clear();
				for (int i = 0; i < things.length; i++) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("icon", thingIcons[i]);
					map.put("name", thingNames[i]);
					map.put("topic", thingTopic[i]);
					map.put("id", thingId[i]);

					listItem.add(map);
				}
				adapter.notifyDataSetChanged();
				ptrDevices.onRefreshComplete();
				break;
			}

		}
	}

}
