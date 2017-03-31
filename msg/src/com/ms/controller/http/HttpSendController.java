package com.ms.controller.http;

import java.io.IOException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class HttpSendController {

	public static void main(String[] args) throws HttpException, IOException {
		HttpClient client = new HttpClient();  
		client.getParams().setParameter("http.protocol.single-cookie-header", true);
		PostMethod post = new PostMethod("http://utf8.sms.webchinese.cn");  
        post.addRequestHeader("Content-Type",  
                "application/x-www-form-urlencoded;charset=utf8");// ��ͷ�ļ�������ת��  
        NameValuePair[] data = { new NameValuePair("Uid", "zbnb"),// �й������û���
                new NameValuePair("Key", "24329998e28d0ccdd09f"),// ��Կ(SMS����ͨAPI���нӿڲ���)
                new NameValuePair("smsMob", "15720310922"),// ���շ�����
                new NameValuePair("smsText", "���գ��ֻ���֤�룺12451597474") };// ����
        post.setRequestBody(data);  
  
        client.executeMethod(post);  
        Header[] headers = post.getResponseHeaders();  
        int statusCode = post.getStatusCode();  
        System.out.println("statusCode:" + statusCode);  
        for (Header h : headers) {  
            System.out.println(h.toString());  
        }  
        String result = new String(post.getResponseBodyAsString().getBytes(  
                "gbk"));  
        System.out.println(result);  
  
        post.releaseConnection();  

	}

}
