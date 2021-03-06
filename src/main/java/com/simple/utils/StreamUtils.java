package com.simple.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 将stream 转换 String的工具类
 */
public final class StreamUtils {

	private StreamUtils() {
	}

    /**
     * stream to string
     * @param inputStream 输入流
     * @return 返回string
     * @throws IOException io异常
     */
	public static String streamToString(InputStream inputStream)
			throws IOException {
		StringBuilder sBuilder = new StringBuilder();
		String line ;
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		while ((line = bufferedReader.readLine()) != null) {
			sBuilder.append(line).append("\n");
		}
		return sBuilder.toString();
	}

    /**
     * 关闭Closeable对象
     * @param closeable 要关闭的对象
     */
	public static void closeSilently(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
