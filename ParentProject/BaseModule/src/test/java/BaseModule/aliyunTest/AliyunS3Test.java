package BaseModule.aliyunTest;

import java.io.File;

import org.junit.Test;

import BaseModule.aliyun.AliyunMain;
import BaseModule.configurations.ImgConfig;
import BaseModule.configurations.ServerConfig;
import BaseModule.eduDAO.EduDaoBasic;

public class AliyunS3Test {

	@Test
	public void testUploadImg(){
		EduDaoBasic.clearAllDatabase();
		int userId = 1;
		//Teacher
		String teacherImgPrefix = ImgConfig.teacherImgPrefix;
		String imgName = teacherImgPrefix +userId;
		File file = new File(ServerConfig.resourcePrefix + ImgConfig.imgFolder + imgName+".png");
		AliyunMain.uploadImg(userId, file, imgName, ServerConfig.AliyunTeacherImgBucket,false);
		//Background
		String backImgPrefix = ImgConfig.classImgPrefix;
		imgName = backImgPrefix + userId;
		file = new File(ServerConfig.resourcePrefix + ImgConfig.imgFolder + imgName+".png");
		AliyunMain.uploadImg(userId, file, imgName, ServerConfig.AliyunClassroomImgBucket,false);
		//Logo
		String logoPrefix = ImgConfig.logoPrefix;
		imgName = logoPrefix + userId;
		file = new File(ServerConfig.resourcePrefix + ImgConfig.imgFolder + imgName+".png");
		AliyunMain.uploadImg(userId, file, imgName, ServerConfig.AliyunLogoBucket,false);
	}
}
