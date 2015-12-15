package com.zlt.icode;

import java.util.ArrayList;
import java.util.List;

import org.litepal.crud.DataSupport;

import com.zlt.icode.adapter.CommonAdapter;
import com.zlt.icode.adapter.ViewHolder;
import com.zlt.icode.common.DragListView;
import com.zlt.icode.common.Info;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;

public class SearchActivity extends BaseActivity implements OnClickListener {

	private DragListView mListView;
	private Button btnSearch;
	private EditText editText;
	private ImageView ivBack;
	private TextView noticeText;
	
	private List<Info> mLists = new ArrayList<>();

	private String text;

	private Dialog mDialog;
	private boolean change;

	private String password;

	private EditText passEditText;
	private CheckBox remenberBox;

	private boolean isKeep;
	private String keepPassword;

	private int pos;
	private CommonAdapter<Info> mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_layout);

		password = sharedPreferences.getString("password", null);
		initViews();
	}

	public void initViews() {
		btnSearch = (Button) findViewById(R.id.search_start);
		editText = (EditText) findViewById(R.id.search_edit);
		ivBack = (ImageView) findViewById(R.id.search_back);

		ivBack.setOnClickListener(this);
		btnSearch.setOnClickListener(this);
		
		noticeText = (TextView) findViewById(R.id.notice_text);
		
		mListView = (DragListView) findViewById(R.id.listview);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				pos = position;
				showCustomDialog();

			}
		});
	}

	public void showCustomDialog() {
		mDialog = new Dialog(this, R.style.CommonDialog);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setCancelable(false);
		View dialogView = LayoutInflater.from(this).inflate(
				R.layout.confirm_dialog, null);
		dialogView.findViewById(R.id.cancel).setOnClickListener(this);
		dialogView.findViewById(R.id.ok_btn).setOnClickListener(this);

		passEditText = (EditText) dialogView.findViewById(R.id.pass_edittext);
		remenberBox = (CheckBox) dialogView.findViewById(R.id.remenber_box);
		remenberBox.setChecked(isKeep);
		if (isKeep && keepPassword != null) {
			passEditText.setText(keepPassword);
		}
		remenberBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				isKeep = isChecked;
			}
		});
		mDialog.setContentView(dialogView);
		mDialog.show();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (change) {
			startSearch(text);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.search_back:
			finish();
			break;
		case R.id.search_start:
			text = editText.getText().toString();
			if (text == null || text.length() <= 0) {
				showToast("请输入查询内容");
				return;
			}
			startSearch(text);
			
			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
			.hideSoftInputFromWindow(this.getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			break;
		case R.id.cancel:
			mDialog.cancel();
			break;
		case R.id.ok_btn:
			if (passEditText.getText().toString().equals(password)) {
				keepPassword = password;
				mDialog.cancel();
				if (!change) {
					change = true;
				}
				Intent intent = new Intent();
				intent.setClass(SearchActivity.this, DetailActivity.class);
				intent.putExtra("type", QUERY);
				intent.putExtra("info", mLists.get(pos));
				startActivity(intent);
			} else {
				keepPassword = null;
				showToast("密码错误");
			}
			//
		default:
			break;
		}
	}

	private void startSearch(String str) {
		// TODO Auto-generated method stub
		if (str.equals("*#zlt#*")||str.equals("哈哈哈哈")) {
			mLists = DataSupport.where("id > ?", "0").find(Info.class);
		}else {
			mLists = DataSupport.where("title like ?", "%" + str + "%").find(
					Info.class);
		}
		
		if (mLists.size() <= 0) {
			mListView.setVisibility(View.GONE);
			noticeText.setVisibility(View.VISIBLE);
		}else {
			mListView.setVisibility(View.VISIBLE);
			noticeText.setVisibility(View.GONE);
		}
		mListView.setAdapter(mAdapter = new CommonAdapter<Info>(this, mLists,
				android.R.layout.simple_list_item_2) {

			@Override
			public void convert(ViewHolder viewHolder, Info item) {
				// TODO Auto-generated method stub
				viewHolder.setText(android.R.id.text1, item.getTitle()
						.toString());
				String extras = item.getExtras();
				viewHolder.setText(android.R.id.text2,
						extras.length() == 0 ? "[无相关信息]" : extras);
			}
		});
	};

}
