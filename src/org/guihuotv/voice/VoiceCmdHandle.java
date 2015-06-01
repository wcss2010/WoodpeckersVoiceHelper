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
	public static String handleVoiceCmd(WoodpeckersActivity mainActivity, String say, List<NgnSay> cmdList, IatHelper iat, TTSHelper tts) {
		String sayTxt = "";
		if (cmdList != null && cmdList.size() > 0) {
			// 比较靠普的是最后一个结果
			NgnSay useCmd = cmdList.get(cmdList.size() - 1);

			if (useCmd.getSyscmd().equals("say")) {
				// 只是回答问题
				sayTxt = useCmd.getCmdcontent();
			} else if (useCmd.getSyscmd().equals("saydate")) {
				// 只是回答问题
				sayTxt = "现在是" + dates.format(new Date());
			} else if (useCmd.getSyscmd().equals("saytime")) {
				// 只是回答问题
				sayTxt = "现在是" + times.format(new Date());
			}
		}

		return sayTxt;
	}
}