package BaseModule.eduDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import BaseModule.common.DateUtility;
import BaseModule.exception.notFound.ClassPhotoNotFoundException;
import BaseModule.model.ClassPhoto;

public class ClassPhotoDao {

	
	public static ClassPhoto addClassPhotoToDataBases(ClassPhoto classPhoto, Connection...connections) throws SQLException{
		String query = "insert into ClassPhoto (p_Id,imgUrl,title,description,creationTime) values(?,?,?,?,?)";
		PreparedStatement stmt = null;
		Connection conn = null;
		ResultSet rs = null;
		int stmtInt = 1;
		
		try{
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(stmtInt++, classPhoto.getPartnerId());
			stmt.setString(stmtInt++, classPhoto.getImgUrl());
			stmt.setString(stmtInt++, classPhoto.getTitle());
			stmt.setString(stmtInt++, classPhoto.getDescription());
			stmt.setString(stmtInt++, DateUtility.toSQLDateTime(classPhoto.getCreationTime()));
			
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			rs.next();			
			classPhoto.setClassPhotoId(rs.getLong(1));
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		}
		return classPhoto;
	}
	
	public static ArrayList<ClassPhoto> getClassPhotos(ArrayList<Long> classIdList,Connection...connections) throws SQLException{
		String query = "select * from ClassPhoto where id = ? ";
		ArrayList<ClassPhoto> clist = new ArrayList<ClassPhoto>();
		PreparedStatement stmt = null;
		Connection conn = null;
		ResultSet rs = null;
		
		if(classIdList.size() == 0){
			return clist;
		}				
		for(Long i = 1L; i < classIdList.size(); i++ ){
			query += " or id = ?";
		}
		
		try{
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query);
			for(int i = 0; i < classIdList.size(); i++){
				stmt.setLong(i+1, classIdList.get(i));
			}
			rs = stmt.executeQuery();
			while(rs.next()){
				clist.add(createClassPhotoByResultSet(rs));
			}
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 
		return clist;
	}
	
	public static void updateClassPhoto(ClassPhoto classPhoto,Connection...connections) throws SQLException, ClassPhotoNotFoundException{
		String query = "update ClassPhoto set imgUrl=?, title=?, description=? where id=?";
		PreparedStatement stmt = null;
		Connection conn = null;
		ResultSet rs = null;
		int stmtInt = 1;
		
		try{
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query);
			stmt.setString(stmtInt++, classPhoto.getImgUrl());
			stmt.setString(stmtInt++, classPhoto.getTitle());
			stmt.setString(stmtInt++, classPhoto.getDescription());
			stmt.setLong(stmtInt++, classPhoto.getClassPhotoId());
			
			int recordsAffected = stmt.executeUpdate();
			if(recordsAffected==0){
				throw new ClassPhotoNotFoundException();
			}
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		}
	}
	
	private static ClassPhoto createClassPhotoByResultSet(ResultSet rs) throws SQLException {
		return new ClassPhoto(rs.getLong("id"),rs.getInt("p_Id"),rs.getString("imgUrl"),rs.getString("title"),rs.getString("description"),DateUtility.DateToCalendar(rs.getTimestamp("creationTime")));
	}
}
