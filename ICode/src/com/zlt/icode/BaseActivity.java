package com.zlt.icode;

import org.litepal.tablemanager.Connector;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class BaseActivity extends Activity {
	public static final int CREATE = 0;
	public static final int QUERY = 1;
	
	private Toast mToast = null;
	public SharedPreferences sharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		SQLiteDatabase db = Connector.getDatabase();
	}
	
	
	public void showToast(CharSequence s){
		if (mToast == null) {
			mToast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
		}
		mToast.setText(s);
		mToast.show();
	}
}
