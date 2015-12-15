package com.zlt.icode;

import com.zlt.icode.common.GesView;
import com.zlt.icode.common.GesView.OnEndTouchListener;

import android.content.Intent;
import android.os.Bundle;

public class LoginCheckActivity extends BaseActivity {
	
	private String userId;
	
	private GesView gesView;
	private String gesPassword;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		userId = sharedPreferences.getString("userId", null);
		if (userId != null && userId.length() > 0) {
			setContentView(R.layout.ges_layout);
			gesPassword = sharedPreferences.getString("gesPassword", null);
			
			gesView = (GesView) findViewById(R.id.ges_view);
			gesView.SetOnEndTouchListener(new OnEndTouchListener() {
				
				@Override
				public void onEndTouch(String password) {
					// TODO Auto-generated method stub
					if (password.equals(gesPassword)) {
						showToast("登录成功!");
						startActivity(new Intent(LoginCheckActivity.this,LoginActivity.class));
						finish();
					}else {
						showToast("密码错误，请重新尝试!");
					}
				}
			});
		}else {
			startActivity(new Intent(LoginCheckActivity.this,LoginActivity.class));
			this.finish();
		}
		
		
		
	}
}
