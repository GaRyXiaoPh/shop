package cn.kt.mall.common.certification;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

//用户请使用UTF-8作为源码文件的保存格式，避免出现乱码问题
public class CertificationApi {
    /**
     * HTTP的Post请求方式
     * @param strUrl 访问地址
     * @param param 参数字符串
     * */
    public static String doPost(String strUrl, String param) {
        String returnStr = null; // 返回结果定义
        URL url = null;
        HttpURLConnection httpURLConnection = null;

        try {
            url = new URL(strUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST"); // post方式
            httpURLConnection.connect();
            //System.out.println("ResponseCode:" + httpURLConnection.getResponseCode());
            //POST方法时使用
            byte[] byteParam = param.getBytes("UTF-8");
            DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
            out.write(byteParam);
            out.flush();
            out.close();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            reader.close();
            returnStr = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return returnStr;
    }


    //将map型转为请求参数型
    public static String urlencode(Map<String,Object>data) {
        StringBuilder apistore = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                apistore.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return apistore.toString();
    }

    // 发起请求,获取内容
    public static void main(String[] args) {
        //请求地址
        String url="https://v.apistore.cn/api/a1";
        //您申请的key
        String APPKEY = "77ea2233ad9621b6ae2aa450166ad5a2";
        //请求参数
        Map params = new HashMap();
        params.put("key",APPKEY);
        params.put("cardNo","232101198912263051");
        params.put("realName","杜英东");
        params.put("information","");

        String result = doPost(url, urlencode(params));
        //输出结果
        System.out.println(result);
        //JSON
        JSONObject object = JSONObject.fromObject(result);
        //输出状态码
        System.out.println(object.getInt("error_code")) ;
        //输出返回结果
        System.out.println(object.get("reason")) ;
    }

}