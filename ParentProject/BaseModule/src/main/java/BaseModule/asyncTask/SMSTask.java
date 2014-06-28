package BaseModule.asyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.SMSEvent;
import BaseModule.interfaces.PseudoAsyncTask;

public class SMSTask implements PseudoAsyncTask{
	
	private final SMSEvent event;
	private final String cellNum;
	private final String payload;
	
	public SMSTask(final SMSEvent event, final String cellNum, final String payload){
		this.cellNum = cellNum;
		this.event = event;
		this.payload = payload;
	}

	public boolean execute() {
		return this.send();
	}
	
	private boolean send(){
		if (this.cellNum.contains("DONOTSEND")){
			return true;
		}
		
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://gbk.sms.webchinese.cn"); 
		
		List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		nvps.add(new BasicNameValuePair("Uid", "routea"));
		nvps.add(new BasicNameValuePair("Key", "a221a629eacbddc0720c"));
		nvps.add(new BasicNameValuePair("smsMob", this.cellNum));
		nvps.add(new BasicNameValuePair("smsText", this.payload));
		
		try{
			post.setEntity(new UrlEncodedFormEntity(nvps, "gbk"));
			post.addHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk");
			HttpResponse response = client.execute(post);
			
			DebugLog.d("SMSTask:  SMS to: " + this.cellNum + " under event: " + this.event.toString() + " returned with statusCode: " + response.getStatusLine().getStatusCode());
			System.out.println("SMSTask:  SMS to: " + this.cellNum + " under event: " + this.event.toString() + " returned with statusCode: " + response.getStatusLine().getStatusCode());
		} catch(IOException e){
			DebugLog.d(e);
			return false;
		} finally{
			post.releaseConnection();
		}
		
		return true;
	}

}
