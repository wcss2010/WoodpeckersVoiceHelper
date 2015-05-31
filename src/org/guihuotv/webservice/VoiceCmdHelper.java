package org.guihuotv.webservice;

import java.util.List;

import android.annotation.TargetApi;
import android.os.Build;
import com.alibaba.fastjson.JSON;

/**
 * 用于向服务器查询语音指令以及添加语音指令
 * 
 * @author wcss
 * 
 */
public class VoiceCmdHelper {

	public static String serviceUrl = "http://cilisou.guihuotv.org/";

	/**
	 * 查询语音指令
	 * 
	 * @param say
	 * @param useLikeQuery
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public static List<NgnSay> getVoiceCmds(String say, boolean useLikeQuery) {
		String page = serviceUrl + "api/sys/getSay.jsp";
		String param = "say=" + say + "&mod=" + (useLikeQuery ? "1" : "0");
		String response = HttpRequest.sendPost(page, param);
		if (response != null && !response.isEmpty()) {
			ApiDataList data = JSON.parseObject(response, ApiDataList.class);
			if (data.getCode() > 0) {
				return data.getData();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 添加语音指令
	 * 
	 * @param says
	 * @return
	 */
	public static boolean addVoiceCmd(String say, String cmd, String content, boolean replaceAll) {
		if (say != null && cmd != null && content != null) {
			String page = serviceUrl + "api/sys/addSay.jsp";
			String param = "say=" + say + "&cmd=" + cmd + "&content=" + content + "&replace=" + (replaceAll ? "1" : "0");
			HttpRequest.sendPost(page, param);
			return true;
		} else {
			return false;
		}
	}

	public static void main(String[] args) {
		List<NgnSay> list = VoiceCmdHelper.getVoiceCmds("你好", false);
		if (list != null) {
			for (NgnSay ns : list) {
				System.out.println(ns.toString());
			}
		}

		List<NgnSay> list2 = VoiceCmdHelper.getVoiceCmds("你", true);
		if (list2 != null) {
			for (NgnSay ns : list2) {
				System.out.println(ns.toString());
			}
		}

		VoiceCmdHelper.addVoiceCmd("你好吗", "say", "哈哈，你好", false);
	}
}