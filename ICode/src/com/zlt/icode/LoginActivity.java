package com.zlt.icode;

import java.io.IOException;

import com.zlt.update.UpdateFailedListener;
import com.zlt.update.UpdateManager;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LoginActivity extends BaseActivity implements OnClickListener{

	private String userId;
	private String code;
	
	private TextView mainText;
	
	private LinearLayout accountLayout,noAccountLayout;
	 
	private Dialog mDialog;
	private EditText passEditText;
	private CheckBox remenberBox;
	
	private MenuItem mMenuItem;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mainText = (TextView) findViewById(R.id.main_text);
		accountLayout = (LinearLayout) findViewById(R.id.account_layout);
		noAccountLayout = (LinearLayout) findViewById(R.id.no_account_layout);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("zlt", "Login OnResume");
		
		userId = sharedPreferences.getString("userId", null);
		
		if (userId != null) {
			accountLayout.setVisibility(View.VISIBLE);
			noAccountLayout.setVisibility(View.GONE);
			mainText.setText("Deal " + userId + ",Welcome to back!");
			if (mMenuItem != null) {
				mMenuItem.setVisible(true);
			}
		}
		
		
	}
	
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.create_count:
			startActivity(new Intent(LoginActivity.this, AccountActivity.class));
			break;
		case R.id.query_info:
			startActivity(new Intent(LoginActivity.this, SearchActivity.class));
			break;
		case R.id.create_info:
			Intent intent = new Intent(LoginActivity.this,DetailActivity.class);
			intent.putExtra("type", CREATE);
			startActivity(intent);
			break;
		case R.id.cancel:
			mDialog.dismiss();
			break;
		case R.id.ok_btn:
			
			String password = sharedPreferences.getString("password", null);
			
			if (passEditText.getText().toString().equals(password)) {
				mDialog.dismiss();
				Intent intent2 = new Intent(LoginActivity.this,AccountActivity.class);
				intent2.putExtra("changeUserInfo", true);
				startActivity(intent2);
			}else {
				showToast("密码错误");
			}
			break;
		default:
			finish();
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		Log.i("zlt", "Login OnCreateOptionsMenu");
		
		getMenuInflater().inflate(R.menu.main, menu);
		mMenuItem = menu.getItem(0);
		if (userId == null) {
			mMenuItem.setVisible(false);
		}
		
		String versionStr = null;
		try {
			versionStr = String.format("版本更新(当前%1$s)", getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		menu.getItem(1).setTitle(versionStr);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			showCustomDialog();
			return true;
		}else if (id == R.id.check_updates) {
			try {
				new UpdateManager(this).checkUpdate(new UpdateFailedListener() {
					
					@Override
					public void onFailed() {
						// TODO Auto-generated method stub
						showToast("当前已经是最新版本!");
					}
				});
			} catch (NotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showCustomDialog() {
		// TODO Auto-generated method stub
		mDialog = new Dialog(this, R.style.CommonDialog);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setCancelable(false);
		
		View dialogView = LayoutInflater.from(this).inflate(
				R.layout.confirm_dialog, null);
		dialogView.findViewById(R.id.cancel).setOnClickListener(this);
		dialogView.findViewById(R.id.ok_btn).setOnClickListener(this);
		
		passEditText = (EditText) dialogView.findViewById(R.id.pass_edittext);
		remenberBox = (CheckBox) dialogView.findViewById(R.id.remenber_box);
		remenberBox.setVisibility(View.GONE);
		mDialog.setContentView(dialogView);
		mDialog.show();
	}
	
}
