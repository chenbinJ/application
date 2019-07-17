package com.springboot.util;

import lombok.extern.slf4j.Slf4j;

import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class HttpClientUtils {




    public static String getJsonData(JSONObject jsonParam, String urls) {
        StringBuffer sb = new StringBuffer();
        try {
            // 创建url资源
            URL url = new URL(urls);
            // 建立http连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置允许输出
            conn.setDoOutput(true);
            // 设置允许输入
            conn.setDoInput(true);
            // 设置不用缓存
            conn.setUseCaches(false);
            // 设置传递方式
            conn.setRequestMethod("POST");
            // 设置维持长连接
            conn.setRequestProperty("Connection", "Keep-Alive");
            // 设置文件字符集:
            conn.setRequestProperty("Charset", "UTF-8");
            // 转换为字节数组
            byte[] data = (jsonParam.toString()).getBytes();
            // 设置文件长度
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            // 设置文件类型:
            conn.setRequestProperty("contentType", "application/json");
            // 开始连接请求
            conn.connect();
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // 写入请求的字符串
            out.write((jsonParam.toString()).getBytes());
            out.flush();
            out.close();

            System.out.println(conn.getResponseCode());

            // 请求返回的状态
            if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                System.out.println("连接成功");
                // 请求返回的数据
                InputStream in1 = conn.getInputStream();
                try {
                    String readLine = new String();
                    BufferedReader responseReader = new BufferedReader(new InputStreamReader(in1, "UTF-8"));
                    while ((readLine = responseReader.readLine()) != null) {
                        sb.append(readLine).append("\n");
                    }
                    responseReader.close();
                    System.out.println(sb.toString());

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } else {
                System.out.println("error++");
            }
        } catch (Exception e) {
            e.getStackTrace();
            log.error(e.getMessage());
        }
        System.out.println("返回结果"+sb.toString());
        return sb.toString();
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }






}
