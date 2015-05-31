package org.guihuotv.webservice;

import java.lang.reflect.Type;
import java.util.Arrays;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyPreFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * json工具类，本质上是对fastjson的一个小封装。
 * 
 * @author axeon
 * 
 */
public class JsonUtils {

	static {
		int features = 0;
		features |= SerializerFeature.QuoteFieldNames.getMask();
		features |= SerializerFeature.SkipTransientField.getMask();
		features |= SerializerFeature.WriteEnumUsingToString.getMask();
		features |= SerializerFeature.SortField.getMask();
		features |= SerializerFeature.DisableCircularReferenceDetect.getMask();
		JSON.DEFAULT_GENERATE_FEATURE = features;

		features |= Feature.AutoCloseSource.getMask();
		features |= Feature.InternFieldNames.getMask();
		features |= Feature.UseBigDecimal.getMask();
		features |= Feature.AllowUnQuotedFieldNames.getMask();
		features |= Feature.AllowSingleQuotes.getMask();
		features |= Feature.AllowArbitraryCommas.getMask();
		features |= Feature.SortFeidFastMatch.getMask();
		features |= Feature.IgnoreNotMatch.getMask();
		JSON.DEFAULT_PARSER_FEATURE = features;
	}

	/**
	 * 用于名称的例外排除。
	 */
	private static PropertyPreFilter pojoExcludeFilter = new PropertyPreFilter() {

		private String[] excludes = new String[] { "CURRENT_TABLE_NAME", "LIST_COL_UPDATED", "MAP_EXT_PROPERTY" };

		@Override
		public boolean apply(JSONSerializer serializer, Object source, String name) {
			if (Arrays.binarySearch(excludes, name) > -1) {
				return false;
			} else {
				return true;
			}
		}
	};

	/**
	 * 默认的输出jsonString的方法。 可以根据需要来修改。
	 * 
	 * @param object
	 * @return
	 */
	public static String toJSONString(Object object) {
		return JSON.toJSONString(object);
	}

	/**
	 * 默认的输出jsonString的方法。 可以根据需要来修改。
	 * 
	 * @param object
	 * @return
	 */
	public static String toJSONString(Object object, PropertyPreFilter filter) {
		return JSON.toJSONString(object, filter);
	}

	/**
	 * 将json解析成对象输出。
	 * 
	 * @param input
	 * @param clazz
	 * @return
	 */
	public static final <T> T parseObject(String input, Type clazz) {
		return JSON.parseObject(input, clazz);

	}

	public static class ApiData {

		private int retcode = 0;

		private String message = null;

		private Object data = null;

		/**
		 * @return the retcode
		 */
		public int getRetcode() {
			return retcode;
		}

		/**
		 * @param retcode
		 *            the retcode to set
		 */
		public void setRetcode(int retcode) {
			this.retcode = retcode;
		}

		/**
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}

		/**
		 * @param message
		 *            the message to set
		 */
		public void setMessage(String message) {
			this.message = message;
		}

		/**
		 * @return the data
		 */
		public Object getData() {
			return data;
		}

		/**
		 * @param data
		 *            the data to set
		 */
		public void setData(Object data) {
			this.data = data;
		}

	}

	public static void main(String[] args) {

		// NgnUser user = new NgnUser();
		// user.setCreateDate(new java.util.Date());
		// ApiData data = new ApiData();
		// data.data = user;
		// String json = toJSONString(data, pojoExcludeFilter);
		// System.out.println(json);
		// data = JSON.parseObject(json, ApiData.class);
		// System.out.println(((NgnUser) data.data).getCreateDate());
	}
}