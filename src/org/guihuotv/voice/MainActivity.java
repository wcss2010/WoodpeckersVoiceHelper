package org.guihuotv.voice;

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
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements IatListener {

	private Toast mToast;
	private WebView webView;
	private Button TTSSet;
	private Button voiceSet;
	private Button listen;
	private LinearLayout buttons;
	private IatHelper iat = new IatHelper();
	private TTSHelper tts = new TTSHelper();
	private Display display;

	@SuppressLint("ShowToast")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

		webView = (WebView) this.findViewById(R.id.webView);
		voiceSet = (Button) this.findViewById(R.id.voiceSet);
		TTSSet = (Button) this.findViewById(R.id.TTSSet);
		listen = (Button) this.findViewById(R.id.listen);
		buttons = (LinearLayout) this.findViewById(R.id.btns);

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

		display = getWindowManager().getDefaultDisplay();
		this.webView.setMinimumWidth(display.getWidth());
		this.webView.setMinimumHeight(display.getHeight() - this.buttons.getHeight());

		this.listen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				iat.startListener(display.getWidth() > 320);
			}
		});
		this.voiceSet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				iat.openSettings();
			}
		});
		this.TTSSet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tts.openSettings();
			}
		});
	}

	private void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
	}

	@SuppressLint("NewApi")
	@Override
	public void handleIatResult(IatHelper source, StringBuffer result) {
		Log.v("test", "识别出的内容：" + result);

		List<NgnSay> list = VoiceCmdHelper.getVoiceCmds(result.toString(), true);

		// TODO 打印查询到的内容
		if (list != null) {
			for (NgnSay ns : list) {
				Log.v("test", ns.toString());
			}
		}

		// TODO 响应语音指令
		String sayTxt = VoiceCmdHandle.handleVoiceCmd(list, iat, tts);
		if (sayTxt != null && !sayTxt.isEmpty()) {
			showTip(sayTxt);
		}
	}
}
