package BaseModule.aliyun;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.apache.log4j.Logger;
import BaseModule.common.DebugLog;
import BaseModule.configurations.ParamConfig;
import BaseModule.configurations.ServerConfig;
import com.aliyun.openservices.ClientException;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.OSSException;
import com.aliyun.openservices.oss.model.ObjectMetadata;



public class AliyunMain {

	private static final String myAccessKeyID = ServerConfig.AliyunAccessKeyID;
	private static final String mySecretKey = ServerConfig.AliyunAccessKeySecrete;

	static Logger logger = Logger.getLogger(AliyunMain.class);

	
	public static String uploadImg(final int id, final File file, final String imgName, final String Bucket){
		return uploadImg(id, file, imgName, Bucket,true);
	}


	//the boolean shouldDelete is used for testing so that the sample is not deleted every time
	public static String uploadImg(final int id, final File file, final String imgName, final String Bucket, final boolean shouldDelete){

		OSSClient client = new OSSClient(myAccessKeyID, mySecretKey);		
		String imgAddress = "";	
		try{
			String tempImageKey = getImageKey(id, imgName);
			InputStream content = new FileInputStream(file);
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentLength(file.length());
			client.putObject(Bucket, tempImageKey, content, meta);
			imgAddress = getOSSUrlPrefix(Bucket) + tempImageKey;
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
		System.out.println("Img Address is: " + imgAddress);
		return  imgAddress;	

	}		

	public static String uploadFile(final int id, final File file, final String fileName, final String Bucket){
		return uploadFile(id, file, fileName, Bucket,true);
	}
	
	public static String uploadFile(final int id, final File file, final String fileName, final String Bucket, final boolean shouldDelete){

		OSSClient client = new OSSClient(myAccessKeyID, mySecretKey);	
		String imgAddress = "";		
		try{			
			String tempFileKey = getFileKey(id,fileName);
			InputStream content = new FileInputStream(file);
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentLength(file.length());
			client.putObject(Bucket, tempFileKey, content, meta);
			imgAddress = getOSSUrlPrefix(Bucket) + tempFileKey;
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
		System.out.println("File Address is: " + imgAddress);
		return  imgAddress;	

	}
	
	private static String getFileKey(final int id, final String fileName) {
		return id + "/" + fileName + ".txt";
	}


	private static String getImageKey(final int id, final String imageName){
		return id + "/" + imageName + ".png";
	}	

	private static String getOSSUrlPrefix(final String Bucket){
		if (ServerConfig.configurationMap.get(ParamConfig.MAP_ENV_KEY).equals(ParamConfig.MAP_ENV_LOCAL)){
			return "http://" + Bucket + ".oss-cn-hangzhou.aliyuncs.com/";
		}
		else if (ServerConfig.configurationMap.get(ParamConfig.MAP_ENV_KEY).equals(ParamConfig.MAP_ENV_TEST)){
			return "http://" + Bucket + ".oss-cn-hangzhou.aliyuncs.com/";
		}
		else if (ServerConfig.configurationMap.get(ParamConfig.MAP_ENV_KEY).equals(ParamConfig.MAP_ENV_PROD)){
			return "http://" + Bucket + ".oss-internal.aliyuncs.com/";
		}
		else{
			throw new RuntimeException();
		}
		
	}
	
}
