package org.guihuotv.speech.helper;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.guihuotv.speech.setting.IatSettings;
import org.guihuotv.speech.util.ApkInstaller;
import org.guihuotv.speech.util.FucUtil;
import org.guihuotv.speech.util.JsonParser;
import org.guihuotv.voice.R;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

/**
 * 语音识别
 * 
 * @author wcss
 * 
 */
public class IatHelper {
	private static String TAG = IatHelper.class.getSimpleName();

	// 语音听写对象
	private SpeechRecognizer mIat;
	// 语音听写UI
	private RecognizerDialog mIatDialog;
	// 用HashMap存储听写结果
	public HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

	private Toast mToast;
	private SharedPreferences mSharedPreferences;
	// 引擎类型
	private String mEngineType = SpeechConstant.TYPE_CLOUD;
	// 语音+安装助手类
	ApkInstaller mInstaller;

	private Activity thisForm = null;

	public IatListener listener = null;

	public IatListener getListener() {
		return listener;
	}

	public void setListener(IatListener listener) {
		this.listener = listener;
	}

	/**
	 * 初始化TTS
	 * 
	 * @param activitys
	 * @param isCloud
	 */
	public void init(Activity activitys, boolean isCloud) {
		this.thisForm = activitys;

		// 初始化识别无UI识别对象
		// 使用SpeechRecognizer对象，可根据回调消息自定义界面；
		mIat = SpeechRecognizer.createRecognizer(thisForm, mInitListener);

		// 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
		// 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
		mIatDialog = new RecognizerDialog(thisForm, mInitListener);

		mSharedPreferences = thisForm.getSharedPreferences(IatSettings.PREFER_NAME, Activity.MODE_PRIVATE);
		mToast = Toast.makeText(thisForm, "", Toast.LENGTH_SHORT);

		mInstaller = new ApkInstaller(thisForm);

		if (isCloud) {
			mEngineType = SpeechConstant.TYPE_CLOUD;
		} else {
			mEngineType = SpeechConstant.TYPE_LOCAL;

			/**
			 * 选择本地听写 判断是否安装语音+,未安装则跳转到提示安装页面
			 */
			if (!SpeechUtility.getUtility().checkServiceInstalled()) {
				mInstaller.install();
			} else {
				String result = FucUtil.checkLocalResource();
				if (!TextUtils.isEmpty(result)) {
					showTip(result);
				}
			}
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
		Intent intents = new Intent(thisForm, IatSettings.class);
		thisForm.startActivity(intents);
	}

	private int ret = 0; // 函数调用返回值

	/**
	 * 开始听写
	 * 
	 * @param isShowDialog
	 */
	public void startListener(boolean isShowDialog) {

		mIatResults.clear();
		// 设置参数
		setParam();

		if (isShowDialog) {
			// 显示听写对话框
			mIatDialog.setListener(recognizerDialogListener);
			mIatDialog.show();
			showTip(thisForm.getString(R.string.text_begin));
		} else {
			// 不显示听写对话框
			ret = mIat.startListening(recognizerListener);
			if (ret != ErrorCode.SUCCESS) {
				showTip("听写失败,错误码：" + ret);
			} else {
				showTip(thisForm.getString(R.string.text_begin));
			}
		}
	}

	/**
	 * 停止听写
	 */
	public void stopListener() {
		mIat.cancel();
		showTip("取消听写");
	}

	/**
	 * 初始化监听器。
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d(TAG, "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				showTip("初始化失败，错误码：" + code);
			}
		}
	};

	/**
	 * 听写监听器。
	 */
	private RecognizerListener recognizerListener = new RecognizerListener() {

		@Override
		public void onBeginOfSpeech() {
			showTip("开始说话");
		}

		@Override
		public void onError(SpeechError error) {
			// Tips：
			// 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
			// 如果使用本地功能（语音+）需要提示用户开启语音+的录音权限。
			showTip(error.getPlainDescription(true));
		}

		@Override
		public void onEndOfSpeech() {
			showTip("结束说话");
		}

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			Log.d(TAG, results.getResultString());
			printResult(results);

			if (isLast) {
				// TODO 最后的结果
			}
		}

		@Override
		public void onVolumeChanged(int volume) {
			showTip("当前正在说话，音量大小：" + volume);
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
		}
	};

	private void printResult(RecognizerResult results) {
		String text = JsonParser.parseIatResult(results.getResultString());

		String sn = null;
		// 读取json结果中的sn字段
		try {
			JSONObject resultJson = new JSONObject(results.getResultString());
			sn = resultJson.optString("sn");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		mIatResults.put(sn, text);

		StringBuffer resultBuffer = new StringBuffer();
		for (String key : mIatResults.keySet()) {
			resultBuffer.append(mIatResults.get(key));
		}

		// 投递识别结果
		if (this.getListener() != null) {
			this.getListener().handleIatResult(this, resultBuffer);
		}
	}

	/**
	 * 听写UI监听器
	 */
	private RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener() {
		public void onResult(RecognizerResult results, boolean isLast) {
			printResult(results);
		}

		/**
		 * 识别回调错误.
		 */
		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}

	};

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
		mIat.setParameter(SpeechConstant.PARAMS, null);

		// 设置听写引擎
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
		// 设置返回结果格式
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

		String lag = mSharedPreferences.getString("iat_language_preference", "mandarin");
		if (lag.equals("en_us")) {
			// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
		} else {
			// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			// 设置语言区域
			mIat.setParameter(SpeechConstant.ACCENT, lag);
		}

		// 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
		mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));

		// 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
		mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));

		// 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
		mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));

		// 设置音频保存路径，保存音频格式仅为pcm，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/iflytek/wavaudio.pcm");

		// 设置听写结果是否结果动态修正，为“1”则在听写过程中动态递增地返回结果，否则只在听写结束之后返回最终结果
		// 注：该参数暂时只对在线听写有效
		mIat.setParameter(SpeechConstant.ASR_DWA, mSharedPreferences.getString("iat_dwa_preference", "0"));
	}

	/**
	 * 释放资源
	 */
	public void destroy() {
		// 退出时释放连接
		mIat.cancel();
		mIat.destroy();
	}
}