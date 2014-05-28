package BaseModule.service;

import java.io.File;

import BaseModule.aliyun.AliyunMain;
import BaseModule.configurations.ServerConfig;

public class FileService {	
	
	public static String uploadTeacherImg(int courseId, File file, String imgName){
		return AliyunMain.uploadImg(courseId, file, imgName, ServerConfig.AliyunTeacherImgBucket);
	}
	
	public static String uploadBackgroundImg(int courseId, File file, String imgName){
		return AliyunMain.uploadImg(courseId, file, imgName, ServerConfig.AliyunBackgroundImgBucket);
	}
	
	public static String uploadLogoImg(int partnerId, File file, String imgName){
		return AliyunMain.uploadImg(partnerId, file, imgName, ServerConfig.AliyunLogoBucket);
	}
}
