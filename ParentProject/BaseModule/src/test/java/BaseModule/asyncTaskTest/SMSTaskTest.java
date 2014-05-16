package BaseModule.asyncTaskTest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import BaseModule.asyncRelayExecutor.ExecutorProvider;
import BaseModule.asyncTask.SMSTask;
import BaseModule.configurations.EnumConfig.SMSEvent;

public class SMSTaskTest {

	@Test
	public void testConnection() throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://gbk.sms.webchinese.cn"); 
		
		List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		nvps.add(new BasicNameValuePair("Uid", "routea"));
		nvps.add(new BasicNameValuePair("Key", "a221a629eacbddc0720c"));
		nvps.add(new BasicNameValuePair("smsMob", "18662241356"));
		nvps.add(new BasicNameValuePair("smsText", "您的验证码：testConnection"));
		
		post.setEntity(new UrlEncodedFormEntity(nvps, "gbk"));
		
		post.addHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk");

		HttpResponse response = client.execute(post);
		
		
		Header[] headers = response.getAllHeaders();
		System.out.println("statusCode:"+response.getStatusLine().getStatusCode());
		for(Header h : headers){
			System.out.println(h.toString());
		}
		String result = new String(response.getEntity().toString().getBytes("gbk")); 
		System.out.println(result);


		post.releaseConnection();
	}
	
	@Test
	public void testSMSTask(){
		SMSTask smsTask = new SMSTask(SMSEvent.user_registration, "18662241356", "testSMSTask");
		smsTask.execute();
	}
	
	@Test
	public void testSMSRelay() throws InterruptedException{
		SMSTask smsTask = new SMSTask(SMSEvent.user_bookingConfirmation, "18662241356", "testSMSRelay");
		ExecutorProvider.executeRelay(smsTask);
		Thread.sleep(5000);
	}
	
	@Test
	public void testSMSForgetPassword() throws InterruptedException{
		SMSTask smsTaska = new SMSTask(SMSEvent.user_forgetPassword, "18662241356", "testSMSForgetPassword");
		ExecutorProvider.executeRelay(smsTaska);
		SMSTask smsTaskb = new SMSTask(SMSEvent.user_forgetPassword, "18662241356", "ku79DS3drR");
		ExecutorProvider.executeRelay(smsTaskb);
		Thread.sleep(5000);
	}

}
