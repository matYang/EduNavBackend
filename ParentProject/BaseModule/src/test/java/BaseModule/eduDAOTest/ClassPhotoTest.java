package BaseModule.eduDAOTest;

import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import BaseModule.eduDAO.ClassPhotoDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.exception.notFound.ClassPhotoNotFoundException;
import BaseModule.model.ClassPhoto;

public class ClassPhotoTest {

	@Test
	public void testAdd(){
		EduDaoBasic.clearAllDatabase();
		int p_Id = 1;
		ClassPhoto classPhoto = new ClassPhoto(p_Id, "ImgUrl", "classPhoto","classDescription");
		try {
			ClassPhotoDao.addClassPhotoToDataBases(classPhoto);
		} catch (SQLException e) {		
			fail();
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGet() throws SQLException{
		EduDaoBasic.clearAllDatabase();
		int p_Id = 1;
		ClassPhoto classPhoto = new ClassPhoto(p_Id, "ImgUrl", "classPhoto","classDescription");
		ClassPhotoDao.addClassPhotoToDataBases(classPhoto);
		ClassPhoto classPhoto2 = new ClassPhoto(p_Id, "ImgUrl2", "classPhoto2","classDescription2");
		ClassPhotoDao.addClassPhotoToDataBases(classPhoto2);
		ArrayList<ClassPhoto> clist = new ArrayList<ClassPhoto>();
		ArrayList<Long> idList = new ArrayList<Long>();
		idList.add(classPhoto.getClassPhotoId());
		idList.add(classPhoto2.getClassPhotoId());
		
		clist = ClassPhotoDao.getClassPhotos(idList);
		if(clist.size()==2 && clist.get(0).equals(classPhoto) && clist.get(1).equals(classPhoto2)){
			//Passed;
		}else fail();
		
		clist = ClassPhotoDao.getPartnerClassPhotos(p_Id);
		if(clist.size()==2 && clist.get(0).equals(classPhoto) && clist.get(1).equals(classPhoto2)){
			//Passed;
		}else fail();
	}
	
	@Test
	public void testUpDate() throws SQLException, ClassPhotoNotFoundException{
		EduDaoBasic.clearAllDatabase();
		int p_Id = 1;
		ClassPhoto classPhoto = new ClassPhoto(p_Id, "ImgUrl", "classPhoto","classDescription");		
		ClassPhoto test = ClassPhotoDao.addClassPhotoToDataBases(classPhoto);
		ArrayList<Long> cpidList = new ArrayList<Long>();
		cpidList.add(test.getClassPhotoId());
		test.setDescription("FUCK");
		ClassPhotoDao.updateClassPhoto(test);		
		ArrayList<ClassPhoto> clist = ClassPhotoDao.getClassPhotos(cpidList);
		if(clist.size()==1 && clist.get(0).equals(test) && clist.get(0).getDescription().equals("FUCK")){
			//Passed;
		}else fail();
		
	}
}
