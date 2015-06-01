package org.guihuotv.voice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.guihuotv.speech.helper.IatHelper;
import org.guihuotv.speech.helper.IatListener;
import org.guihuotv.speech.helper.TTSHelper;
import org.guihuotv.webservice.NgnSay;
import org.guihuotv.webservice.VoiceCmdHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class WoodpeckersActivity extends Activity implements OnClickListener, IatListener {

	private Button mBtnListen;// 听写btn
	private Button mBtnBack;// 退出btn
	private Button mBtnTTS;// 朗读设置btn
	private Button mBtnIAT;// 听写设置btn
	private EditText mEditTextContent;
	private ListView mListView;
	private ChatMsgViewAdapter mAdapter;// 消息视图的Adapter
	private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();// 消息对象数组
	private String userName = "主公";
	private String sysName = "您的秘书";
	private Toast mToast;
	private IatHelper iat = new IatHelper();
	private TTSHelper tts = new TTSHelper();
	private DisplayMetrics display;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_woodpeckers);
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null || !networkInfo.isAvailable()) {
			// 当前有可用网络
			iat.init(this, true);
			tts.init(this, true);
		} else {
			// 当前无可用网络
			iat.init(this, false);
			tts.init(this, false);
		}
		iat.setListener(this);

		display = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(display);

		initView();// 初始化view

		initData();// 初始化数据
		mListView.setSelection(mAdapter.getCount() - 1);
	}

	/**
	 * 初始化view
	 */
	public void initView() {
		mListView = (ListView) findViewById(R.id.listviews);
		mBtnListen = (Button) findViewById(R.id.btn_listen);
		mBtnListen.setOnClickListener(this);
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
		mBtnTTS = (Button) findViewById(R.id.btn_tts);
		mBtnTTS.setOnClickListener(this);
		mBtnIAT = (Button) findViewById(R.id.btn_voice);
		mBtnIAT.setOnClickListener(this);
	}

	/**
	 * 模拟加载消息历史，实际开发可以从数据库中读出
	 */
	public void initData() {
		mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
		mListView.setAdapter(mAdapter);
	}

	/**
	 * 发送消息时，获取当前事件
	 * 
	 * @return 当前时间
	 */
	private String getDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return format.format(new Date());
	}

	/**
	 * 增加一条消息显示
	 * 
	 * @param isUser
	 *            是否为用户输入的内容
	 * @param name
	 *            显示名称
	 * @param msg
	 *            消息内容
	 */
	public void addMsg(boolean isUser, String name, String msg) {
		if (name != null && msg != null && name.length() > 0 && msg.length() > 0) {
			ChatMsgEntity entity = new ChatMsgEntity();
			entity.setName(name);
			entity.setDate(getDate());
			entity.setMessage(msg);
			entity.setMsgType(isUser);
			mDataArrays.add(entity);
			mAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变
			mEditTextContent.setText("");// 清空编辑框数据
			mListView.setSelection(mListView.getCount() - 1);// 发送一条消息时，ListView显示选择最后一项
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_listen:// 听写点击事件
			iat.startListener(display.widthPixels > 320);
			break;
		case R.id.btn_tts:// 朗读设置点击事件
			tts.openSettings();
			break;
		case R.id.btn_voice:// 听写设置点击事件
			iat.openSettings();
			break;
		case R.id.btn_back:// 返回按钮点击事件
			finish();// 结束,实际开发中，可以返回主界面
			break;
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void handleIatResult(IatHelper source, StringBuffer result) {
		String results = result.toString();
		if (result != null) {
			results = result.toString().replace("。", "");
		}
		Log.v("test", "识别出的内容：" + results);
		addMsg(true, this.userName, results);

		final String resultStr = results;
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				final List<NgnSay> list = VoiceCmdHelper.getVoiceCmds(resultStr, true);

				// TODO 打印查询到的内容
				if (list != null) {
					for (NgnSay ns : list) {
						Log.v("test", ns.toString());
					}
				} else {
					Log.v("test", "没有查询到任务内容！");
				}

				WoodpeckersActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO 响应语音指令
						String sayTxt = VoiceCmdHandle.handleVoiceCmd(resultStr, list, iat, tts);
						if (sayTxt != null && !sayTxt.isEmpty()) {
							addMsg(false, WoodpeckersActivity.this.sysName, sayTxt);
						}
					}
				});
			}
		}).start();
	}
}