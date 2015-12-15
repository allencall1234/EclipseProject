package com.zlt.icode;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class AccountActivity extends BaseActivity implements OnClickListener {
 
	private EditText titleText;
	private EditText codeText;
	private EditText sCodeText;

	private String title, code, sCode, gesPassword;

	private LinearLayout gestureLayout;
	private ImageView gesCheckImageView;

	private boolean changeUserInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		changeUserInfo = getIntent().getBooleanExtra("changeUserInfo", false);
		
		setContentView(R.layout.account_info);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		titleText = (EditText) findViewById(R.id.title_text);
		codeText = (EditText) findViewById(R.id.code_text);
		sCodeText = (EditText) findViewById(R.id.scode_text);

		if (changeUserInfo) {
			titleText.setText(sharedPreferences.getString("userId", ""));
			codeText.setText(sharedPreferences.getString("password", ""));
			sCodeText.setText(sharedPreferences.getString("password", ""));
		}
		
		gesCheckImageView = (ImageView) findViewById(R.id.gesture_check);
		gesCheckImageView.setTag("0");
		gestureLayout = (LinearLayout) findViewById(R.id.gesture_password);
		gestureLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.gesture_password:
			startActivityForResult(new Intent(AccountActivity.this,
					GesPasswordActivity.class), 0);
			break;
		default:
			title = titleText.getText().toString();
			if (title == null || title.length() <= 0) {
				showToast("用户名不能为空白");
				return;
			}

			code = codeText.getText().toString();
			sCode = sCodeText.getText().toString();

			if (code == null || code.length() <= 0 || sCode == null || sCode.length() <= 0) {
				showToast("请设置密码");
				return;
			}

			if (!sCode.equals(code)) {
				showToast("密码不一致");
				return;
			}
			if (gesCheckImageView.getTag().toString().equals("1")) {
				saveInfo();
				showToast("创建完成");
				finish();
				if (changeUserInfo) {
					restartApplication();
				}
			} else {
				showToast("请设置手势密码");
			}
			break;
		}
	}

	private void restartApplication() {  
        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());  
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
        startActivity(intent);  
}  
	public void saveInfo() {
		Editor editor = sharedPreferences.edit();
		editor.putString("userId", title);
		editor.putString("password", code);
		editor.putString("gesPassword", gesPassword);
		editor.commit();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case 0:
			if (resultCode == 1) {
				gesPassword = data.getStringExtra("gesPassword");
				gesCheckImageView
						.setImageResource(R.drawable.btn_check_buttonless_on);
				gesCheckImageView.setTag("1");
			}
			break;

		default:
			break;
		}

	}
}
