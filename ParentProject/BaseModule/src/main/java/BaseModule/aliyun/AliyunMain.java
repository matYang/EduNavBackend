package BaseModule.aliyun;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.apache.log4j.Logger;
import BaseModule.common.DateUtility;
import BaseModule.common.DebugLog;
import BaseModule.configurations.ServerConfig;
import com.aliyun.openservices.ClientException;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.OSSException;
import com.aliyun.openservices.oss.model.ObjectMetadata;



public class AliyunMain {

	private static final String myAccessKeyID = ServerConfig.AliyunAccessKeyID;
	private static final String mySecretKey = ServerConfig.AliyunAccessKeySecrete;


	static Logger logger = Logger.getLogger(AliyunMain.class);

	
	public static String uploadImg(int userId, File file, String imgName, String Bucket){
		return uploadImg(userId, file, imgName, Bucket,true);
	}


	//the boolean shouldDelete is used for testing so that the sample is not deleted every time
	public static String uploadImg(int userId, File file, String imgName, String Bucket,boolean shouldDelete){

		OSSClient client = new OSSClient(myAccessKeyID, mySecretKey);		
		String imgAddress = "";	
		try{
			String tempImageKey = getUserImageKey(userId, imgName);
			InputStream content = new FileInputStream(file);
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentLength(file.length());
			client.putObject(Bucket, tempImageKey, content, meta);
			imgAddress = ServerConfig.AliyunGetImgPrefix + tempImageKey;

		} catch(ClientException | OSSException e){
			e.printStackTrace();  
			DebugLog.d(e);
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
			DebugLog.d(e);
		} finally{
			IdleConnectionReaper.shutdown();
			if (shouldDelete){
				file.delete();
			}
		}		
		//System.out.println("Img Address is: " + imgAddress);
		return  imgAddress;	

	}	
	
	private static String getUserImageKey(int userId, String imageName){
		long msec = DateUtility.getCurTime();				
		return userId + "/" + imageName + "-" + msec + ".png";
	}	

	
}
