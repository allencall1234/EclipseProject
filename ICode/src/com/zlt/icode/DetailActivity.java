package com.zlt.icode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.litepal.crud.DataSupport;

import com.zlt.icode.common.Info;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class DetailActivity extends BaseActivity implements OnClickListener{
	private int type = -1;
	
	
	private EditText titleText,accountText,passwordText,createTimeText,lastTimeText,queryTimesText,extrasText;
	
	private String title,account,password,extras;
	private LinearLayout createTimeLayout,lastQueryTimeLayout,queryTimesLayout;
	
	private Button delButton,addButton;
	
	private Info mInfo;
	private boolean isSave;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_deal);
		
		type = getIntent().getIntExtra("type", -1);
		if (type == CREATE) {
			mInfo = new Info();
		}else {
			mInfo = (Info) getIntent().getSerializableExtra("info");
			mInfo.setQueryTimes(mInfo.getQueryTimes() + 1);
			mInfo.update(mInfo.getId());
		}
		
		initViews();
	}
	
	public void initViews(){
		titleText = (EditText) findViewById(R.id.info_title);
		accountText = (EditText) findViewById(R.id.info_account);
		passwordText = (EditText) findViewById(R.id.info_password);
		createTimeText = (EditText) findViewById(R.id.info_create_time);
		lastTimeText = (EditText) findViewById(R.id.info_last_time);
		queryTimesText = (EditText) findViewById(R.id.info_query_times);
		extrasText = (EditText) findViewById(R.id.info_extras);
		
		createTimeLayout = (LinearLayout) findViewById(R.id.info_create_time_layout);
		lastQueryTimeLayout = (LinearLayout) findViewById(R.id.info_last_time_layout);
		queryTimesLayout = (LinearLayout) findViewById(R.id.info_query_times_layout);
		
		delButton = (Button) findViewById(R.id.del);
		addButton = (Button) findViewById(R.id.add);

		if (type == CREATE) {
			createTimeLayout.setVisibility(View.GONE);
			lastQueryTimeLayout.setVisibility(View.GONE);
			queryTimesLayout.setVisibility(View.GONE);
			
			delButton.setVisibility(View.GONE);
		}
		
		delButton.setOnClickListener(this);
		addButton.setOnClickListener(this);
				
		if (type == QUERY) {
			titleText.setText(mInfo.getTitle().toString());
			accountText.setText(mInfo.getAccount().toString());
			passwordText.setText(mInfo.getPassword().toString());
			extrasText.setText(mInfo.getExtras().toString());
			lastTimeText.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.UK).format(mInfo.getLastTime()));
			createTimeText.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.UK).format(mInfo.getCreateTime()));
			
			queryTimesText.setText(mInfo.getQueryTimes() + "");
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.del:
			DataSupport.delete(Info.class, mInfo.getId());
			showToast("删除成功!");
			finish();
			break;
		case R.id.add:
			
			title = titleText.getText().toString();
			password = passwordText.getText().toString();
			account = accountText.getText().toString();
			extras = extrasText.getText().toString();
			
			
			if (type == CREATE) {
				createInfo();
			}else {
				mInfo.setTitle(title);
				mInfo.setAccount(account);
				mInfo.setPassword(password);
				mInfo.setLastTime(new Date());
				mInfo.setExtras(extras);
				mInfo.setQueryTimes(mInfo.getQueryTimes());
				mInfo.update(mInfo.getId());
				isSave = true;
			}
			showToast("保存成功!");
			finish();
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		if (type == QUERY  &&  !isSave) {
			mInfo.setLastTime(new Date());
			mInfo.update(mInfo.getId());
		}
		super.onStop();
	}

	private void createInfo() {
		// TODO Auto-generated method stub
		if (title == null || title.length() <= 0) {
			showToast("关键字不能为空");
			return;
		}
		if (password == null || password.length() <= 0) {
			showToast("密码不能为空");
			return;
		}
		
		mInfo.setTitle(title);
		mInfo.setAccount(account);
		mInfo.setPassword(password);
		mInfo.setCreateTime(new Date());
		mInfo.setLastTime(new Date());
		mInfo.setExtras(extras);
		mInfo.save();
	};
}
