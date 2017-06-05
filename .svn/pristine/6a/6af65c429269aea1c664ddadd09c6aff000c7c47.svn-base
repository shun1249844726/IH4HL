package com.lexinsmart.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.lexinsmart.R;
import com.lexinsmart.mqtt.MqttV3Service;
import com.lexinsmart.utils.Constants;

public class DetailsActivity extends Activity implements OnClickListener {

	private TextView tvDetails;
	private SeekBar sbRange;
	private LinearLayout girdView;
	private Button customerColor_btn;
	public int Position;
	private ColorPickerDialog dialog;
	int Qos = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);

		tvDetails = (TextView) findViewById(R.id.tv_details);
		sbRange = (SeekBar) findViewById(R.id.sb_Range);
		girdView = (LinearLayout) findViewById(R.id.grid_id);
		customerColor_btn = (Button) findViewById(R.id.button1);
		// TODO: 根据设备类型，加载布局文件
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		String type = bundle.getString("type");
		Position = bundle.getInt("position");
		tvDetails.setText(type);
		type = (type == null) ? "" : type;
		if (type.equals(Constants.THINGS_TYPES[0])) {
			sbRange.setVisibility(View.GONE);
		} else if (type.equals(Constants.THINGS_TYPES[1])) {
			girdView.setVisibility(View.GONE);
			sbRange.setVisibility(View.GONE);
			customerColor_btn.setVisibility(View.GONE);

		} else if (type.equals(Constants.THINGS_TYPES[2])) {
			girdView.setVisibility(View.GONE);
			sbRange.setVisibility(View.GONE);
			customerColor_btn.setVisibility(View.GONE);

		} else if (type.equals(Constants.THINGS_TYPES[3])) {
			girdView.setVisibility(View.VISIBLE);
			sbRange.setVisibility(View.VISIBLE);
			customerColor_btn.setVisibility(View.VISIBLE);
		}
		// TODO: 然后根据thingname，从本地数据库读取信息，在tvDetails上显示。
		String thingname = getIntent().getStringExtra("thingname");
		// 学习数据库的查询方法
		//

		sbRange.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				System.out.println("progress:" + progress);
				MqttV3Service.publishMsg("LIG:" + progress + ";", 1,
						Position - 1);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:

			Toast.makeText(DetailsActivity.this, "dianji", Toast.LENGTH_SHORT)
					.show();

			dialog = new ColorPickerDialog(DetailsActivity.this,
					customerColor_btn.getTextColors().getDefaultColor(),
					getResources().getString(R.string.btn_color_picker),
					new ColorPickerDialog.OnColorChangedListener() {

						@Override
						public void colorChanged(int color) {
							customerColor_btn.setTextColor(color);
							int testColor = customerColor_btn.getTextColors()
									.getDefaultColor();
							int blue = Color.blue(testColor);
							int red = Color.red(testColor);
							int green = Color.green(testColor);
							customerColor_btn.setBackgroundColor(testColor);
							MqttV3Service.publishMsg("RGB:" + red + "," + green
									+ "," + blue + ";", Qos, Position - 1);
							System.out.println("tvTextColor :" + testColor
									+ " ;r :" + red + ";  g : " + green
									+ " ;b : " + blue);
						}

					},Position-1);
			dialog.show();

			break;

		default:
			break;
		}
	}

}
