package BaseModule.service;

import java.io.File;

import BaseModule.aliyun.AliyunMain;
import BaseModule.configurations.ServerConfig;

public final class FileService {	
	
	public static String uploadTeacherImg(final int courseId, final File file, final String imgName){
		return AliyunMain.uploadImg(courseId, file, imgName, ServerConfig.AliyunTeacherImgBucket);
	}
	
	public static String uploadBackgroundImg(final int courseId, final File file, final String imgName){
		return AliyunMain.uploadImg(courseId, file, imgName, ServerConfig.AliyunClassroomImgBucket);
	}
	
	public static String uploadLogoImg(final int partnerId, final File file, final String imgName){
		return AliyunMain.uploadImg(partnerId, file, imgName, ServerConfig.AliyunLogoBucket);
	}

}
