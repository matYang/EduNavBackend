package BaseModule.eduDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import BaseModule.common.DateUtility;
import BaseModule.configurations.EnumConfig.Visibility;
import BaseModule.exception.notFound.TeacherNotFoundException;
import BaseModule.model.Teacher;

public class TeacherDao {

	public static Teacher addTeacherToDataBases(Teacher teacher, Connection...connections) throws SQLException{
		String query = "insert into Teacher (p_Id,imgUrl,name,intro,creationTime,visibility,popularity) values(?,?,?,?,?,?,?)";
		PreparedStatement stmt = null;
		Connection conn = null;
		ResultSet rs = null;
		int stmtInt = 1;

		try{
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(stmtInt++, teacher.getPartnerId());
			stmt.setString(stmtInt++, teacher.getImgUrl());
			stmt.setString(stmtInt++, teacher.getName());
			stmt.setString(stmtInt++, teacher.getIntro());
			stmt.setString(stmtInt++, DateUtility.toSQLDateTime(teacher.getCreationTime()));
			stmt.setInt(stmtInt++, teacher.getVisibility().code);
			stmt.setInt(stmtInt++, teacher.getPopularity());

			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			rs.next();			
			teacher.setTeacherId(rs.getLong(1));
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		}
		return teacher;
	}

	public static ArrayList<Teacher> getTeachers(ArrayList<Long> tlist, Connection...connections) throws SQLException{
		String query = "select * from Teacher where id = ? ";
		ArrayList<Teacher> teachelist = new ArrayList<Teacher>();
		PreparedStatement stmt = null;
		Connection conn = null;
		ResultSet rs = null;

		if(tlist.size() == 0){
			return teachelist;
		}				
		for(Long i = 1L; i < tlist.size(); i++ ){
			query += " or id = ?";
		}

		try{			
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query);
			for(int i = 0; i < tlist.size(); i++){
				stmt.setLong(i+1, tlist.get(i));
			}
			rs = stmt.executeQuery();
			while(rs.next()){
				teachelist.add(createTeacherByResultSet(rs));
			}
		}finally  {
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		} 
		return teachelist;
	}

	public static ArrayList<Teacher> getPartnerTeachers(int partnerId, Connection...connections) throws SQLException{
		String query = "select * from Teacher where p_Id = ? ";
		ArrayList<Teacher> tlist = new ArrayList<Teacher>();
		PreparedStatement stmt = null;
		Connection conn = null;
		ResultSet rs = null;

		try{
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, partnerId);
			rs = stmt.executeQuery();
			while(rs.next()){
				tlist.add(createTeacherByResultSet(rs));
			}

		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		}
		return tlist;
	}

	public static void updateTeacherInDataBase(Teacher teacher, Connection...connections) throws TeacherNotFoundException, SQLException{
		String query = "update Teacher set imgUrl=?, name=?, intro=?, visibility=?, popularity=? where id=?";
		PreparedStatement stmt = null;
		Connection conn = null;
		ResultSet rs = null;
		int stmtInt = 1;

		try{
			conn = EduDaoBasic.getConnection(connections);
			stmt = conn.prepareStatement(query);
			stmt.setString(stmtInt++, teacher.getImgUrl());
			stmt.setString(stmtInt++, teacher.getName());
			stmt.setString(stmtInt++, teacher.getIntro());
			stmt.setInt(stmtInt++, teacher.getVisibility().code);
			stmt.setInt(stmtInt++, teacher.getPopularity());
			stmt.setLong(stmtInt++, teacher.getTeacherId());

			int recordsAffected = stmt.executeUpdate();
			if(recordsAffected==0){
				throw new TeacherNotFoundException();
			}
		}finally{
			EduDaoBasic.closeResources(conn, stmt, rs,EduDaoBasic.shouldConnectionClose(connections));
		}
	}	


	private static Teacher createTeacherByResultSet(ResultSet rs) throws SQLException {
		return new Teacher(rs.getLong("id"),rs.getInt("p_Id"),rs.getInt("popularity"),rs.getString("imgUrl"),rs.getString("name"),rs.getString("intro"),Visibility.fromInt(rs.getInt("visibility")),DateUtility.DateToCalendar(rs.getTimestamp("creationTime")));
	}
}
