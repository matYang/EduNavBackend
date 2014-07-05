package BaseModule.dbservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import BaseModule.eduDAO.ClassPhotoDao;
import BaseModule.eduDAO.EduDaoBasic;
import BaseModule.exception.notFound.ClassPhotoNotFoundException;
import BaseModule.model.ClassPhoto;

public class ClassPhotoDaoService {

	public static ArrayList<ClassPhoto> getClassPhotoByPartnerId(int partnerId) throws SQLException{
		return ClassPhotoDao.getPartnerClassPhotos(partnerId);
	}

	public static ArrayList<ClassPhoto> createClassPhotoList(ArrayList<ClassPhoto> classPhotos) throws SQLException{
		return ClassPhotoDao.addClassPhotosToDataBases(classPhotos);
	}

	public static void updateClassPhotoList(ArrayList<ClassPhoto> classPhotos, Connection...connections) throws ClassPhotoNotFoundException, SQLException{
		Connection conn = null;
		try{
			conn = EduDaoBasic.getConnection(connections);
			for(ClassPhoto c: classPhotos){
				ClassPhotoDao.updateClassPhoto(c, connections);
			}
		}finally{
			EduDaoBasic.closeResources(conn, null, null, EduDaoBasic.shouldConnectionClose(connections));
		}
	}

}
