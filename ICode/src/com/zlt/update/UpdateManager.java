package com.zlt.update;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import com.zlt.icode.R;

import android.R.interpolator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UpdateManager {

	private final static String DOWN_URL = "https://raw.githubusercontent.com/allencall1234/UpdateZone/master/version.xml";
	private Context mContext;
	private HashMap<String, String> mHashMap;
	private Dialog mDialog;
	private TextView titleTextView;
	private TextView process_Text;
	private TextView volume_Text;
	private ProgressBar mProgressBar;
	private LinearLayout loadingLayout;
	protected String mSavePath;
	public int progress;

	public Handler mHandler;

	public UpdateManager(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
	}
 
	/**
	 * 检测软件更新
	 * 
	 * @throws IOException
	 * @throws NotFoundException
	 */
	public void checkUpdate(final UpdateFailedListener onUpdateFailedListener)
			throws NotFoundException, IOException {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				try {
					try {
						if (isUpdate()) {
							// 显示提示对话框
							Looper.prepare();
							mHandler = new Handler() {
								public void handleMessage(Message msg) {
									// TODO Auto-generated method stub
									switch (msg.what) {
									case 0:
										mProgressBar.setProgress(progress);
										volume_Text
												.setText(getVolumeBySize(msg.arg2)
														+ "/"
														+ getVolumeBySize(msg.arg1));
										process_Text.setText(progress + "%");
										break;
									case 1:
										installApk();
										break;
									default:
										break;
									}
								}
							};
						showNoticeDialog();
//							mHandler.sendEmptyMessage(2);
							Looper.loop();
						} else {
							Looper.prepare();
							onUpdateFailedListener.onFailed();
							Looper.loop();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (NotFoundException e) {
					e.printStackTrace();
				}

			}
		}).start();
	}

	protected void showNoticeDialog() {
		mDialog = new Dialog(mContext, R.style.CommonDialog);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setCancelable(false);
		final View dialogView = LayoutInflater.from(mContext).inflate(
				R.layout.no_app_dialog, null);

		titleTextView = (TextView) dialogView.findViewById(R.id.no_app_title);
		process_Text = (TextView) dialogView.findViewById(R.id.loading_process);
		volume_Text = (TextView) dialogView.findViewById(R.id.loading_volume);
		mProgressBar = (ProgressBar) dialogView
				.findViewById(R.id.loading_progress);
		loadingLayout = (LinearLayout) dialogView
				.findViewById(R.id.loading_layout);
		
		String title = String.format("检测到新版本(版本号%1$s),是否更新?",mHashMap.get("versionName"));
		
		titleTextView.setText(title);
		
		dialogView.findViewById(R.id.no_app_ok).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// // 判断手机中是否已经下载了更新文件，如果下载了直接安装,没有下载就开启线程下载
						// File file = new File(mSavePath,
						// mHashMap.get("name").toString());
						// if (file.exists()) {
						// installApk();
						// mDialog.dismiss();
						// } else {
						v.setVisibility(View.GONE);
						((Button) dialogView.findViewById(R.id.no_app_cancel))
								.setText("隐藏下载");
						titleTextView.setText("正在下载更新版本...");
						mProgressBar.setVisibility(View.VISIBLE);
						loadingLayout.setVisibility(View.VISIBLE);
						new DownloadApkThread().start();// 开启下载线程
					}
					// }
				});
		dialogView.findViewById(R.id.no_app_cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mDialog.dismiss();
					}
				});

		mDialog.setContentView(dialogView);
		mDialog.show();
	}

	protected boolean isUpdate() throws IOException {
		// 获取当前软件版本
		int versionCode = getVersionCode(mContext);// 把version.xml放到网络上，然后获取文件信息
//		URL url = new URL("http://192.168.1.35:8080/version.xml");
		URL url = new URL(DOWN_URL);
		Log.i("zlt", "url = " + url);
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		InputStream inStream = urlConn.getInputStream();
		
		
//		InputStream inStream = new FileInputStream(file);
		// 解析XML文件。 由于XML文件比较小，因此使用DOM方式进行解析
		ParseXmlService service = new ParseXmlService();
		try {
			mHashMap = service.parseXml(inStream);
			Log.i("zlt", "version = " + mHashMap.get("version") + ",name = " + mHashMap.get("name"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != mHashMap) {
			int serviceCode = Integer.valueOf(mHashMap.get("version"));
			// 版本判断
			if (serviceCode > versionCode) {
				return true;
			}
		}
		return false;
	}

	private int getVersionCode(Context context) {
		int versionCode = 0;
		try {
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 下载文件线程
	 */
	class DownloadApkThread extends Thread {
		@Override
		public void run() {
			try {
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// 获得存储卡的路径
					String sdpath = Environment.getExternalStorageDirectory()
							+ "/";
					mSavePath = sdpath + "download";
//					URL url = new URL(mHashMap.get("url").toString());
					URL url = new URL(mHashMap.get("url").toString());
					// URL url = new
					// URL("http://192.168.1.59:8080/ogrp/apk/xjchat.apk");

					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					
					Log.i("zlt", "length = " + length);
					// 创建输入流
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// 判断文件目录是否存在
					if (!file.exists()) {
						file.mkdir();
					}
					File apkFile = new File(mSavePath, "Icode.apk");
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do {
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						Message message = new Message();
						message.what = 0;
						message.arg1 = length;
						message.arg2 = count;
						mHandler.sendMessage(message);
						if (numread <= 0) {
							// 下载完成
							Log.i("zlt", "Successful download Icode.apk form internet!");
							mHandler.sendEmptyMessage(1);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (true);// 点击取消就停止下载.
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 取消下载对话框显示
			mDialog.dismiss();
		}
	};

	/**
	 * 安装APK文件
	 */
	private void installApk() {
		File apkfile = new File(mSavePath, "Icode.apk");
		if (!apkfile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
	}

	private String getVolumeBySize(int size) {
		String volume;
		String unit = "b";
		if (size > 1000000) {
			volume = String.format("%.1f", (float) size / 1000000);
			unit = "M";
		} else if (size > 1000) {
			volume = String.valueOf(size / 1000);
			unit = "K";
		} else {
			volume = String.valueOf(size);
		}
		return volume + unit;
	}
}
