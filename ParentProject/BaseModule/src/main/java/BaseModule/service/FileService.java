package BaseModule.service;

import java.io.File;

import BaseModule.aliyun.AliyunMain;
import BaseModule.configurations.ServerConfig;

public final class FileService {	
	
	public static String uploadTeacherImg(final long partnerId, final File file, final String imgName){
		return AliyunMain.uploadImg(partnerId, file, imgName, ServerConfig.AliyunTeacherImgBucket);
	}
	
	public static String uploadClassPhotoImg(final long partnerId, final File file, final String imgName){
		return AliyunMain.uploadImg(partnerId, file, imgName, ServerConfig.AliyunClassroomImgBucket);
	}
	
	public static String uploadLogoImg(final long partnerId, final File file, final String imgName){
		return AliyunMain.uploadImg(partnerId, file, imgName, ServerConfig.AliyunLogoBucket);
	}

}
