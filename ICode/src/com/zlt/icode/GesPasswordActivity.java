package com.zlt.icode;

import com.zlt.icode.common.GesView;
import com.zlt.icode.common.GesView.OnEndTouchListener;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class GesPasswordActivity extends BaseActivity {

	private GesView gesView;
	private TextView tipText;

	private String fPassword, sPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ges_layout);

		gesView = (GesView) findViewById(R.id.ges_view);
		tipText = (TextView) findViewById(R.id.tip_text);
		
		gesView.SetOnEndTouchListener(new OnEndTouchListener() {

			@Override
			public void onEndTouch(String password) {
				// TODO Auto-generated method stub
				if (sPassword == null) {
					if (fPassword == null) {
						fPassword = password;
						tipText.setText(getResources().getString(
								R.string.second_tips));
					} else {
						sPassword = password;
					}
				}

				if (sPassword != null) {
					if (sPassword.equals(fPassword)) {
						showToast("手势密码设置成功!");
						toFinish();
					} else {
						sPassword = null;
						fPassword = null;
						tipText.setText(getResources().getString(
								R.string.first_tips));
						showToast("手势密码设置失败,请重新再试!");
					}
				}
			}
		});
	}
	
	private void toFinish(){
		Intent intent = new Intent();
		intent.putExtra("gesPassword", sPassword);
		this.setResult(1,intent);
		this.finish();
	}
}
