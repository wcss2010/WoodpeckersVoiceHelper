package org.guihuotv.webservice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Title: Base table NgnSay的pojo类 Description: 本文件使用XCodeFactory自动生成。
 * 本类为ValueObject类，通过数据库表映像。
 */
public class NgnSay implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private long id = 0l;

	/**
	 * 
	 */
	private String usersay = "";

	/**
	 * 
	 */
	private String syscmd = "";

	/**
	 * 
	 */
	private String cmdcontent = "";

	/**
	 * 
	 */
	private String cmdp1 = "";

	/**
	 * 
	 */
	private String cmdp2 = "";

	/**
	 * 
	 */
	private String cmdp3 = "";

	/**
	 * 
	 */
	private String cmdp4 = "";

	/**
	 * 
	 */
	private String cmdp5 = "";

	/**
	 * 
	 */
	private java.util.Date createtime = null;

	/**
	 * 
	 */
	private int status = 0;

	/**
	 * 获取数值
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * 获取数值
	 */
	public String getUsersay() {
		return this.usersay;
	}

	/**
	 * 获取数值
	 */
	public String getSyscmd() {
		return this.syscmd;
	}

	/**
	 * 获取数值
	 */
	public String getCmdcontent() {
		return this.cmdcontent;
	}

	/**
	 * 获取数值
	 */
	public String getCmdp1() {
		return this.cmdp1;
	}

	/**
	 * 获取数值
	 */
	public String getCmdp2() {
		return this.cmdp2;
	}

	/**
	 * 获取数值
	 */
	public String getCmdp3() {
		return this.cmdp3;
	}

	/**
	 * 获取数值
	 */
	public String getCmdp4() {
		return this.cmdp4;
	}

	/**
	 * 获取数值
	 */
	public String getCmdp5() {
		return this.cmdp5;
	}

	/**
	 * 获取数值
	 */
	public java.util.Date getCreatetime() {
		return this.createtime;
	}

	/**
	 * 获取数值
	 */
	public int getStatus() {
		return this.status;
	}

	/**
	 * 设置数值
	 */
	public void setId(long id) {
		if (this.id != id) {
			this.id = id;
		}
	}

	/**
	 * 设置数值
	 */
	public void setUsersay(String usersay) {
		if ((!String.valueOf(this.usersay).equals(String.valueOf(usersay)))) {
			this.usersay = usersay;
		}
	}

	/**
	 * 设置数值
	 */
	public void setSyscmd(String syscmd) {
		if ((!String.valueOf(this.syscmd).equals(String.valueOf(syscmd)))) {
			this.syscmd = syscmd;
		}
	}

	/**
	 * 设置数值
	 */
	public void setCmdcontent(String cmdcontent) {
		if ((!String.valueOf(this.cmdcontent).equals(String.valueOf(cmdcontent)))) {
			this.cmdcontent = cmdcontent;
		}
	}

	/**
	 * 设置数值
	 */
	public void setCmdp1(String cmdp1) {
		if ((!String.valueOf(this.cmdp1).equals(String.valueOf(cmdp1)))) {
			this.cmdp1 = cmdp1;
		}
	}

	/**
	 * 设置数值
	 */
	public void setCmdp2(String cmdp2) {
		if ((!String.valueOf(this.cmdp2).equals(String.valueOf(cmdp2)))) {
			this.cmdp2 = cmdp2;
		}
	}

	/**
	 * 设置数值
	 */
	public void setCmdp3(String cmdp3) {
		if ((!String.valueOf(this.cmdp3).equals(String.valueOf(cmdp3)))) {
			this.cmdp3 = cmdp3;
		}
	}

	/**
	 * 设置数值
	 */
	public void setCmdp4(String cmdp4) {
		if ((!String.valueOf(this.cmdp4).equals(String.valueOf(cmdp4)))) {
			this.cmdp4 = cmdp4;
		}
	}

	/**
	 * 设置数值
	 */
	public void setCmdp5(String cmdp5) {
		if ((!String.valueOf(this.cmdp5).equals(String.valueOf(cmdp5)))) {
			this.cmdp5 = cmdp5;
		}
	}

	/**
	 * 设置数值
	 */
	public void setCreatetime(java.util.Date createtime) {
		if ((this.createtime != null && createtime != null && !this.createtime.equals(createtime)) || (this.createtime != createtime)) {
			this.createtime = createtime;
		}
	}

	/**
	 * 设置数值
	 */
	public void setStatus(int status) {
		if (this.status != status) {
			this.status = status;
		}
	}

	/**
	 * 构造器
	 */
	public NgnSay(long id, String usersay, String syscmd, String cmdcontent, String cmdp1, String cmdp2, String cmdp3, String cmdp4, String cmdp5, java.util.Date createtime, int status) {

		this.id = id;
		this.usersay = usersay;
		this.syscmd = syscmd;
		this.cmdcontent = cmdcontent;
		this.cmdp1 = cmdp1;
		this.cmdp2 = cmdp2;
		this.cmdp3 = cmdp3;
		this.cmdp4 = cmdp4;
		this.cmdp5 = cmdp5;
		this.createtime = createtime;
		this.status = status;

	}

	/**
	 * 构造器
	 */
	public NgnSay() {
	}

	/**
	 * 重载toString方法
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder("表NgnSay，主键\"" + this.id + "\"的全部信息为:\r\n");
		sb.append("字段id数值为:" + id + "\r\n");
		sb.append("字段usersay数值为:" + usersay + "\r\n");
		sb.append("字段syscmd数值为:" + syscmd + "\r\n");
		sb.append("字段cmdcontent数值为:" + cmdcontent + "\r\n");
		sb.append("字段cmdp1数值为:" + cmdp1 + "\r\n");
		sb.append("字段cmdp2数值为:" + cmdp2 + "\r\n");
		sb.append("字段cmdp3数值为:" + cmdp3 + "\r\n");
		sb.append("字段cmdp4数值为:" + cmdp4 + "\r\n");
		sb.append("字段cmdp5数值为:" + cmdp5 + "\r\n");
		sb.append("字段createtime数值为:" + createtime + "\r\n");
		sb.append("字段status数值为:" + status + "\r\n");

		return sb.toString();
	}

	/**
	 * 克隆自身
	 * 
	 * @return<a name=""></a>
	 */
	public Object clone() {
		Object o = null;
		try {
			o = super.clone();
		} catch (CloneNotSupportedException e) {
		}
		return o;
	}
}