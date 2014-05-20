package BaseModule.dbservice;

import java.io.File;

import BaseModule.aliyun.AliyunMain;

public class FileService {

	public static String upLoadTeacherImg(int courseId, File file, String imgName, String Bucket){
		return AliyunMain.uploadImg(courseId, file, imgName, Bucket);
	}
}
