package org.guihuotv.voice;

import java.util.List;

import org.guihuotv.speech.helper.IatHelper;
import org.guihuotv.speech.helper.TTSHelper;
import org.guihuotv.webservice.NgnSay;

import android.webkit.WebView;

public class VoiceCmdHandle {

	/**
	 * 处理语音指令
	 * 
	 * @param cmds
	 * @return
	 */
	public static String handleVoiceCmd(List<NgnSay> cmds, IatHelper iat, TTSHelper tts, WebView web) {
		String showTxt = "";
		String sayTxt = "主公，我太笨了，不知道该怎么回答你！";
		if (cmds != null && cmds.size() > 0) {
			if (cmds.get(0).getSyscmd().equals("say")) {
				// 只是回答问题
				sayTxt = cmds.get(0).getCmdcontent();
			}
		}

		tts.startSpeaking(sayTxt);
		return showTxt;
	}
}