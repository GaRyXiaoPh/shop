package cn.kt.mall.common.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonParser;

import cn.kt.mall.common.exception.ServerException;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JSON工具类 Created by jerry on 2018/1/8.
 */
public final class JSONUtil {

	private static final Logger logger = LoggerFactory.getLogger(JSONUtil.class);
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	static {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, false);
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		// 为 null 时不参与序列化，注意：只对VO起作用，Map List不起作用
		objectMapper.setSerializationInclusion(Include.NON_NULL);

		/** 遇到未知属性时是否抛出 JsonMappingException 异常，默认为 true */
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public static String toJSONString(Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			logger.error("toJSONString error", e);
			throw new ServerException("toJSONString error");
		}
	}

	public static <T extends Object> T parseToObject(String source, Class<T> valueType) {
		try {
			return objectMapper.readValue(source, valueType);
		} catch (IOException e) {
			logger.error("parseToObject error", e);
			throw new ServerException("parseToObject error");
		}
	}
}
