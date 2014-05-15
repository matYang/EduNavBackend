package BaseModule.aliyunTest;

import java.io.File;

import org.junit.Test;

import BaseModule.aliyun.AliyunMain;
import BaseModule.configurations.ImgConfig;
import BaseModule.configurations.ServerConfig;
import BaseModule.eduDAO.EduDaoBasic;

public class AliyunS3Test {

	@Test
	public void testUplodaUserImg(){
		EduDaoBasic.clearBothDatabase();
		int userId = 1;
		String teacherImgPrefix = ImgConfig.teacherImgPrefix;
		String imgSize = ImgConfig.imgSize_m;
		String imgName = teacherImgPrefix + imgSize +userId;
		File file = new File(ServerConfig.resourcePrefix + ServerConfig.ImgFolder + imgName+".png");
		AliyunMain.uploadImg(userId, file, imgName, ServerConfig.AliyunEduCatTeacherImgBucket,false);
	}
}
