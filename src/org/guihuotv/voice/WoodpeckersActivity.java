package org.guihuotv.voice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class WoodpeckersActivity extends Activity implements OnClickListener {

	private Button mBtnSend;// 发送btn
	private Button mBtnBack;// 返回btn
	private EditText mEditTextContent;
	private ListView mListView;
	private ChatMsgViewAdapter mAdapter;// 消息视图的Adapter
	private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();// 消息对象数组
	private String userName = "主公";
	private String sysName = "您的秘书";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_woodpeckers);

		initView();// 初始化view

		initData();// 初始化数据
		mListView.setSelection(mAdapter.getCount() - 1);
	}

	/**
	 * 初始化view
	 */
	public void initView() {
		mListView = (ListView) findViewById(R.id.listviews);
		mBtnSend = (Button) findViewById(R.id.btn_listen);
		mBtnSend.setOnClickListener(this);
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
//		mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
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
		case R.id.btn_listen:// 发送按钮点击事件
//			send();
			break;
		case R.id.btn_back:// 返回按钮点击事件
			finish();// 结束,实际开发中，可以返回主界面
			break;
		}
	}
}