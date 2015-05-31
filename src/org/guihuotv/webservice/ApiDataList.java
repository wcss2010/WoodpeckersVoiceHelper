package org.guihuotv.webservice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 用来表达API结构
 * 
 * @author axeon
 * 
 */
public class ApiDataList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7660900358326558677L;

	/**
	 * 返回代码
	 */
	private int code;

	/**
	 * 返回的消息
	 */
	private String msg = "";

	/**
	 * 返回数据
	 */
	private ArrayList<NgnSay> data = new ArrayList<NgnSay>();

	/**
	 * 所有数据大小。
	 */
	private int dataAllSize;

	/**
	 * 数据开始位置
	 */
	private int dataStartPos;

	/**
	 * 数据结果集大小。
	 */
	private int dataResultNum;

	public ApiDataList() {
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg
	 *            the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the data
	 */
	public ArrayList<NgnSay> getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(ArrayList<NgnSay> data) {
		if (data != null) {
			this.data = data;
		} else {
			this.data = new ArrayList<NgnSay>();
		}
	}

	/**
	 * @return the dataAllSize
	 */
	public int getDataAllSize() {
		return dataAllSize;
	}

	/**
	 * @param dataAllSize
	 *            the dataAllSize to set
	 */
	public void setDataAllSize(int dataAllSize) {
		this.dataAllSize = dataAllSize;
	}

	/**
	 * @return the dataStartPos
	 */
	public int getDataStartPos() {
		return dataStartPos;
	}

	/**
	 * @param dataStartPos
	 *            the dataStartPos to set
	 */
	public void setDataStartPos(int dataStartPos) {
		this.dataStartPos = dataStartPos;
	}

	/**
	 * @return the dataResultNum
	 */
	public int getDataResultNum() {
		return dataResultNum;
	}

	/**
	 * @param dataResultNum
	 *            the dataResultNum to set
	 */
	public void setDataResultNum(int dataResultNum) {
		this.dataResultNum = dataResultNum;
	}

	/**
	 * 
	 * @param data
	 */
	public void addAll(NgnSay[] datas) {
		if (this.data != null) {
			for (NgnSay t : datas) {
				this.data.add(t);
			}
		}
	}

	/**
	 * 
	 * @param data
	 */
	public void addAll(Collection<NgnSay> datas) {
		if (this.data != null) {
			this.data.addAll(datas);
		}
	}
}