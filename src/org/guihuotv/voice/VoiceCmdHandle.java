package org.guihuotv.voice;

import java.util.List;

import org.guihuotv.speech.helper.IatHelper;
import org.guihuotv.speech.helper.TTSHelper;
import org.guihuotv.webservice.NgnSay;

public class VoiceCmdHandle {

	/**
	 * 处理语音指令
	 * 
	 * @param cmds
	 * @return
	 */
	public static String handleVoiceCmd(List<NgnSay> cmds, IatHelper iat, TTSHelper tts) {
		String showTxt = "主人，我太笨了，不知道该怎么回答你！";
		if (cmds != null && cmds.size() > 0) {

		} else {
			tts.startSpeaking(showTxt);
		}

		return showTxt;
	}
}