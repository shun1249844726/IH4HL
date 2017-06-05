package com.lexinsmartmain.menu;

import static com.lexinsmart.utils.Constants.SPINNER_TYPES;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.Toast;

import com.lexinsmart.MyApplication;
import com.lexinsmart.R;
import com.lexinsmart.main.MainTasks;
import com.lexinsmart.utils.Constants;
import com.zbar.lib.CaptureActivity;

public class AddDialog extends MenuItemDialog {

	// private static boolean mDialogDismissableFlag = false;

	private View mAddDialogView;
	private String mThingName = "", mType = "", mTopic = "", mMin = "", mMax = "", mStep = "", mUnit = "", mBrief = "",
			mStatus = "";
	private TableRow mRowTopic, mRowMinValue, mRowMaxValue, mRowStep, mRowUnit/* , mRowJsonKey */;
	private EditText mEtName, mEtMinValue, mEtMaxValue, mEtStep,
			mEtUnit/* , mEtJsonKey */;
	public static EditText mEtTopic;
	private Button btnCapture;
	private Spinner mSpinnerTypes;

	public static String[] mThingValues;
	private ThingTypesUpdater mTypesUpdater;

	public AddDialog(Activity activity, Drawable icon, String title, String message) {
		super(activity, icon, title, message);
	}

	@Override
	protected Object[] initDefaultButtonListeners() {
		DialogInterface.OnClickListener positiveBtnListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO: 将信息提交到服务器，并使当前界面失焦，直至服务器返回信息。
				// 若返回成功信号，则dismiss；反之，则使当前界面重获焦点、由用户决定是否继续提交或退出
				// 目前，本代码块中的剩余部分仅作演示
				mThingValues = mTypesUpdater.getThingValues();
				if (mThingValues != null) {
					// 将数据提交到服务器
					// 向服务器订阅本Topic
					new MainTasks.PostNewThingTask().execute(MyApplication.gUserName, MyApplication.gToken);
					Toast.makeText(mActivity, "更新中...", Toast.LENGTH_SHORT).show();
					DialogUtils.setDialogDismissable(dialog, true);
				} else {
					Toast.makeText(mActivity, "信息不完整！", Toast.LENGTH_SHORT).show();
					DialogUtils.setDialogDismissable(dialog, false);
				}
			}
		};
		DialogInterface.OnClickListener negativeBtnListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DialogUtils.setDialogDismissable(dialog, true);
			}
		};
		return new Object[] { "确定", null, "取消", positiveBtnListener, null, negativeBtnListener };
	}

	@Override
	protected Object initCustomContentViews() {
		LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
		mAddDialogView = layoutInflater.inflate(R.layout.add,
				(ViewGroup) mActivity.findViewById(R.layout.activity_main));
		// 必须采用“addDialog.findViewById(R.id.x)”这种形式，前面的View实例不可略！
		mSpinnerTypes = (Spinner) mAddDialogView.findViewById(R.id.add_sp_type);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity, R.layout.add_typesspinner_item,
				SPINNER_TYPES);
		mSpinnerTypes.setAdapter(adapter);
		mTypesUpdater = new ThingTypesUpdater(mAddDialogView);
		mSpinnerTypes.setOnItemSelectedListener(mTypesUpdater);
		return mAddDialogView;
	}

	@Override
	protected void updateCustomContentViews() {
		// 修改AlertDialog宽度，“LayoutParams.MATCH_PARENT”会比默认宽度宽一点
		// XXX: 为啥不管用？
		WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mDialog.getWindow().setAttributes(params);
	}

	private class ThingTypesUpdater implements AdapterView.OnItemSelectedListener {
		View parentView;

		public ThingTypesUpdater(View viewGroup) {
			parentView = viewGroup;
			mRowTopic = (TableRow) parentView.findViewById(R.id.add_row_topic);
			mRowMinValue = (TableRow) parentView.findViewById(R.id.add_row_min_value);
			mRowMaxValue = (TableRow) parentView.findViewById(R.id.add_row_max_value);
			mRowStep = (TableRow) parentView.findViewById(R.id.add_row_step);
			mRowUnit = (TableRow) parentView.findViewById(R.id.add_row_unit);
			// mRowJsonKey = (TableRow)
			// parentView.findViewById(R.id.add_row_json_key);
			mEtName = (EditText) parentView.findViewById(R.id.add_et_name);
			mEtTopic = (EditText) parentView.findViewById(R.id.add_et_topic);
			mEtMinValue = (EditText) parentView.findViewById(R.id.add_et_min_value);
			mEtMaxValue = (EditText) parentView.findViewById(R.id.add_et_max_value);
			mEtStep = (EditText) parentView.findViewById(R.id.add_et_step);
			mEtUnit = (EditText) parentView.findViewById(R.id.add_et_unit);
			btnCapture = (Button) parentView.findViewById(R.id.add_ib_capture);
			btnCapture.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) { // XXX: 严重问题！！！！！
					Intent intentQR = new Intent(mContext, CaptureActivity.class);
					mActivity.startActivityForResult(intentQR, Constants.REQUEST_CODE);
				}
			});
			// mEtJsonKey = (EditText)
			// parentView.findViewById(R.id.add_et_json_key);
		}

		/** 根据用户选择，动态更新后续描述项目 */
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

			mType = ((Spinner) parent).getSelectedItem().toString();
			if (mType.equals(Constants.SPINNER_TYPES[1])) {
				mType = Constants.THINGS_TYPES[0];
			} else if (mType.equals(Constants.SPINNER_TYPES[2])) {
				mType = Constants.THINGS_TYPES[1];
			} else if (mType.equals(Constants.SPINNER_TYPES[3])) {
				mType = Constants.THINGS_TYPES[2];
			} else if (mType.equals(Constants.SPINNER_TYPES[4])) {
				mType = Constants.THINGS_TYPES[3];
			}

			switch (position) {
			case 0:
				mRowTopic.setVisibility(View.GONE);
				mRowMinValue.setVisibility(View.GONE);
				mRowMaxValue.setVisibility(View.GONE);
				mRowStep.setVisibility(View.GONE);
				mRowUnit.setVisibility(View.GONE);
				// mRowJsonKey.setVisibility(View.GONE);
				break;
			case 1:
			case 4:
				mRowTopic.setVisibility(View.VISIBLE);
				mRowMinValue.setVisibility(View.GONE);
				mRowMaxValue.setVisibility(View.GONE);
				mRowStep.setVisibility(View.GONE);
				mRowUnit.setVisibility(View.GONE);
				// mRowJsonKey.setVisibility(View.GONE);
				break;
			case 2:
				mRowTopic.setVisibility(View.VISIBLE);
				mRowMinValue.setVisibility(View.VISIBLE);
				mRowMaxValue.setVisibility(View.VISIBLE);
				mRowStep.setVisibility(View.VISIBLE);
				mRowUnit.setVisibility(View.GONE);
				// mRowJsonKey.setVisibility(View.GONE);
				break;
			case 3:
				mRowTopic.setVisibility(View.VISIBLE);
				mRowMinValue.setVisibility(View.GONE);
				mRowMaxValue.setVisibility(View.GONE);
				mRowStep.setVisibility(View.GONE);
				mRowUnit.setVisibility(View.VISIBLE);
				// mRowJsonKey.setVisibility(View.VISIBLE);
				break;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}

		public String[] getThingValues() {
			mThingName = mEtName.getText().toString();
			mTopic = mEtTopic.getText().toString();
			mMin = mEtMinValue.getText().toString();
			mMax = mEtMaxValue.getText().toString();
			mStep = mEtStep.getText().toString();
			mUnit = mEtUnit.getText().toString();

			String[] description = new String[] { mThingName, mType, mTopic, mMin, mMax, mStep, mUnit, mBrief,
					mStatus };
			if ((!description[0].equals("")) && (!description[2].equals(""))) { // TODO:
																				// 把下面三个if语句改成一个if语句
				if (description[1].equals(Constants.THINGS_TYPES[0])
						|| description[1].equals(Constants.THINGS_TYPES[3])) {
					return description;
				}
				if (description[1].equals(Constants.THINGS_TYPES[1]) && !description[3].equals("")
						&& !description[4].equals("") && !description[5].equals("")) {
					return description;
				}
				if (description[1].equals(Constants.THINGS_TYPES[2]) && !description[6].equals("")) {
					return description;
				}
			}
			return null;
		}
	}

}