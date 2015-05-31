package org.guihuotv.speech.helper;

import org.guihuotv.speech.setting.TtsSettings;
import org.guihuotv.speech.util.ApkInstaller;
import org.guihuotv.voice.R;
import org.guihuotv.voicedemo.TtsDemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;

/**
 * 语音朗读
 * 
 * @author wcss
 * 
 */
public class TTSHelper {
	private static String TAG = TtsDemo.class.getSimpleName();
	// 语音合成对象
	private SpeechSynthesizer mTts;

	// 默认发音人
	private String voicer = "xiaoyan";

	private String[] cloudVoicersEntries;
	private String[] cloudVoicersValue;

	// 缓冲进度
	private int mPercentForBuffering = 0;
	// 播放进度
	private int mPercentForPlaying = 0;

	// 云端/本地单选按钮
	private RadioGroup mRadioGroup;
	// 引擎类型
	private String mEngineType = SpeechConstant.TYPE_CLOUD;
	// 语音+安装助手类
	ApkInstaller mInstaller;

	private Toast mToast;
	private SharedPreferences mSharedPreferences;
	private Activity thisForm = null;

	/**
	 * 初始化TTS
	 * 
	 * @param activitys
	 * @param isCloud
	 */
	public void init(Activity activitys, boolean isCloud) {
		this.thisForm = activitys;

		// 初始化合成对象
		mTts = SpeechSynthesizer.createSynthesizer(thisForm, mTtsInitListener);

		// 云端发音人名称列表
		cloudVoicersEntries = thisForm.getResources().getStringArray(R.array.voicer_cloud_entries);
		cloudVoicersValue = thisForm.getResources().getStringArray(R.array.voicer_cloud_values);

		mSharedPreferences = thisForm.getSharedPreferences(TtsSettings.PREFER_NAME, Context.MODE_PRIVATE);
		mToast = Toast.makeText(thisForm, "", Toast.LENGTH_SHORT);

		mInstaller = new ApkInstaller(thisForm);

		if (isCloud) {
			mEngineType = SpeechConstant.TYPE_CLOUD;
		} else {
			mEngineType = SpeechConstant.TYPE_LOCAL;

			isInstalledTTS();
		}
	}

	/**
	 * 检查是否需要安装本地语音服务
	 * 
	 * @return
	 */
	public boolean isInstalledTTS() {
		/**
		 * 选择本地合成 判断是否安装语音+,未安装则跳转到提示安装页面
		 */
		if (SpeechUtility.getUtility().checkServiceInstalled()) {
			return true;
		} else {
			mInstaller.install();
			return false;
		}
	}

	/**
	 * 打开设置界面
	 */
	public void openSettings() {
		if (SpeechConstant.TYPE_CLOUD.equals(mEngineType)) {
			Intent intent = new Intent(thisForm, TtsSettings.class);
			thisForm.startActivity(intent);
		} else {
			// 本地设置跳转到语音+中
			if (!SpeechUtility.getUtility().checkServiceInstalled()) {
				mInstaller.install();
			} else {
				SpeechUtility.getUtility().openEngineSettings(null);
			}
		}
	}

	/**
	 * 开始发言
	 * 
	 * @param text
	 */
	public void startSpeaking(String text) {
		// 设置参数
		setParam();
		int code = mTts.startSpeaking(text, mTtsListener);
		// /**
		// * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
		// * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
		// */
		// String path = Environment.getExternalStorageDirectory()+"/tts.pcm";
		// int code = mTts.synthesizeToUri(text, path, mTtsListener);

		if (code != ErrorCode.SUCCESS) {
			if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
				// 未安装则跳转到提示安装页面
				mInstaller.install();
			} else {
				showTip("语音合成失败,错误码: " + code);
			}
		}
	}

	/**
	 * 停止发言
	 */
	public void stopSpeaking() {
		mTts.stopSpeaking();
	}

	private int selectedNum = 0;

	/**
	 * 选择发言人
	 */
	public void selectSpeaker() {
		if (SpeechConstant.TYPE_CLOUD.equals(mEngineType)) {
			// 选择在线合成
			new AlertDialog.Builder(thisForm).setTitle("在线合成发音人选项").setSingleChoiceItems(cloudVoicersEntries, // 单选框有几项,各是什么名字
					selectedNum, // 默认的选项
					new DialogInterface.OnClickListener() { // 点击单选框后的处理
						public void onClick(DialogInterface dialog, int which) { // 点击了哪一项
							voicer = cloudVoicersValue[which];
							if ("catherine".equals(voicer) || "henry".equals(voicer) || "vimary".equals(voicer)) {
								// ((EditText)
								// findViewById(R.id.tts_text)).setText(R.string.text_tts_source_en);
							} else {
								// ((EditText)
								// findViewById(R.id.tts_text)).setText(R.string.text_tts_source);
							}
							selectedNum = which;
							dialog.dismiss();
						}
					}).show();
		} else {
			if (!SpeechUtility.getUtility().checkServiceInstalled()) {
				mInstaller.install();
			} else {
				SpeechUtility.getUtility().openEngineSettings(SpeechConstant.ENG_TTS);
			}
		}
	}

	/**
	 * 初始化监听。
	 */
	private InitListener mTtsInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			Log.d(TAG, "InitListener init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				showTip("初始化失败,错误码：" + code);
			} else {
				// 初始化成功，之后可以调用startSpeaking方法
				// 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
				// 正确的做法是将onCreate中的startSpeaking调用移至这里
			}
		}
	};

	/**
	 * 合成回调监听。
	 */
	private SynthesizerListener mTtsListener = new SynthesizerListener() {
		@Override
		public void onSpeakBegin() {
			showTip("开始播放");
		}

		@Override
		public void onSpeakPaused() {
			showTip("暂停播放");
		}

		@Override
		public void onSpeakResumed() {
			showTip("继续播放");
		}

		@Override
		public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
			// 合成进度
			mPercentForBuffering = percent;
			showTip(String.format(thisForm.getString(R.string.tts_toast_format), mPercentForBuffering, mPercentForPlaying));
		}

		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
			// 播放进度
			mPercentForPlaying = percent;
			showTip(String.format(thisForm.getString(R.string.tts_toast_format), mPercentForBuffering, mPercentForPlaying));
		}

		@Override
		public void onCompleted(SpeechError error) {
			if (error == null) {
				showTip("播放完成");
			} else if (error != null) {
				showTip(error.getPlainDescription(true));
			}
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

		}
	};

	/**
	 * 显示提示
	 * 
	 * @param str
	 */
	public void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
	}

	/**
	 * 参数设置
	 * 
	 * @param param
	 * @return
	 */
	private void setParam() {
		// 清空参数
		mTts.setParameter(SpeechConstant.PARAMS, null);
		// 根据合成引擎设置相应参数
		if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
			mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
			// 设置在线合成发音人
			mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
		} else {
			mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
			// 设置本地合成发音人 voicer为空，默认通过语音+界面指定发音人。
			mTts.setParameter(SpeechConstant.VOICE_NAME, "");
		}
		// 设置合成语速
		mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "50"));
		// 设置合成音调
		mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
		// 设置合成音量
		mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "50"));
		// 设置播放器音频流类型
		mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));

		// 设置播放合成音频打断音乐播放，默认为true
		mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

		// 设置合成音频保存路径，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		mTts.setParameter(SpeechConstant.PARAMS, "tts_audio_path=" + Environment.getExternalStorageDirectory() + "/test.pcm");
	}

	/**
	 * 释放资源
	 */
	public void destroy() {
		mTts.stopSpeaking();
		// 退出时释放连接
		mTts.destroy();
	}
}