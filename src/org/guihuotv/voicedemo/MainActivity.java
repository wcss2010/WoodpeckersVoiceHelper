package org.guihuotv.voicedemo;

import java.util.List;

import org.guihuotv.voice.R;
import org.guihuotv.webservice.NgnSay;
import org.guihuotv.webservice.VoiceCmdHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private Toast mToast;

	@SuppressLint("ShowToast")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		SimpleAdapter listitemAdapter = new SimpleAdapter();
		((ListView) findViewById(R.id.listview_main)).setAdapter(listitemAdapter);
		
		List<NgnSay> list = VoiceCmdHelper.getVoiceCmds("你好", false);
		if (list != null) {
			for (NgnSay ns : list) {
				System.out.println(ns.toString());
			}
		}

		List<NgnSay> list2 = VoiceCmdHelper.getVoiceCmds("天气", true);
		if (list2 != null) {
			for (NgnSay ns : list2) {
				System.out.println(ns.toString());
			}
		}

		boolean result = VoiceCmdHelper.addVoiceCmd("你好吗", "say", "哈哈，你好", false);
		if (result)
		{
			System.out.println("添加成功！！！");
		}
	}

	@Override
	public void onClick(View view) {
		int tag = Integer.parseInt(view.getTag().toString());
		Intent intent = null;
		switch (tag) {
		case 0:
			// 语音转写
			intent = new Intent(this, IatDemo.class);
			break;
		case 1:
			// 语音合成
			intent = new Intent(this, TtsDemo.class);
			break;
		}

		if (intent != null) {
			startActivity(intent);
		}
	}

	// Menu 列表
	String items[] = { "立刻体验语音听写", "立刻体验语音合成" };

	private class SimpleAdapter extends BaseAdapter {
		public View getView(int position, View convertView, ViewGroup parent) {
			if (null == convertView) {
				LayoutInflater factory = LayoutInflater.from(MainActivity.this);
				View mView = factory.inflate(R.layout.list_items, null);
				convertView = mView;
			}

			Button btn = (Button) convertView.findViewById(R.id.btn);
			btn.setOnClickListener(MainActivity.this);
			btn.setTag(position);
			btn.setText(items[position]);

			return convertView;
		}

		@Override
		public int getCount() {
			return items.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}

	private void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
	}
}
