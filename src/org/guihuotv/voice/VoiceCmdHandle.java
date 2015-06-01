package org.guihuotv.voice;

import java.util.Date;
import java.util.List;

import org.guihuotv.speech.helper.IatHelper;
import org.guihuotv.speech.helper.TTSHelper;
import org.guihuotv.webservice.NgnSay;

public class VoiceCmdHandle {
	static java.text.SimpleDateFormat times = new java.text.SimpleDateFormat("HH点mm分ss秒");
	static java.text.SimpleDateFormat dates = new java.text.SimpleDateFormat("yyyy年MM月dd日");

	/**
	 * 处理语音指令
	 * 
	 * @param cmds
	 * @return
	 */
	public static String handleVoiceCmd(WoodpeckersActivity mainActivity, String say, List<NgnSay> cmds, IatHelper iat, TTSHelper tts) {
		String sayTxt = "";
		if (cmds != null && cmds.size() > 0) {
			if (cmds.get(0).getSyscmd().equals("say")) {
				// 只是回答问题
				sayTxt = cmds.get(0).getCmdcontent();
			} else if (cmds.get(0).getSyscmd().equals("saydate")) {
				// 只是回答问题
				sayTxt = "现在是" + dates.format(new Date());
			} else if (cmds.get(0).getSyscmd().equals("saytime")) {
				// 只是回答问题
				sayTxt = "现在是" + times.format(new Date());
			}
		}
		
		return sayTxt;
	}
}