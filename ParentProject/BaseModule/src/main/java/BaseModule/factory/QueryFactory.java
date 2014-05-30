package BaseModule.factory;

import BaseModule.model.representation.AdminSearchRepresentation;
import BaseModule.model.representation.BookingSearchRepresentation;
import BaseModule.model.representation.CourseSearchRepresentation;
import BaseModule.model.representation.PartnerSearchRepresentation;
import BaseModule.model.representation.UserSearchRepresentation;

public class QueryFactory {

	public static String getSearchQuery(Object sr){
		if(sr instanceof CourseSearchRepresentation){
			return getCourseSearchQuery((CourseSearchRepresentation)sr);
		}else if(sr instanceof AdminSearchRepresentation){
			return getAdminSearchQuery((AdminSearchRepresentation)sr);
		}else if(sr instanceof UserSearchRepresentation){
			return getUserSearchQuery((UserSearchRepresentation)sr);
		}else if(sr instanceof PartnerSearchRepresentation){
			return getPartnerSearchQuery((PartnerSearchRepresentation)sr);
		}else if(sr instanceof BookingSearchRepresentation){
			return getBookingSearchQuery((BookingSearchRepresentation)sr);
		}else return new String("");
	}

	private static String getAdminSearchQuery(AdminSearchRepresentation sr) {
		String query = "SELECT * from AdminAccountDao ";
		boolean start = false;
		
		/* Note:Make sure the order following is the same as that in Dao */
		
		if(sr.getAdminId() > 0){
			query += "where ";
			start = true;
			
			query += "id = ? ";
		}
		if(sr.getName() != null && sr.getName().length() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "name = ? ";
		}
		if(sr.getPhone() != null && sr.getPhone().length() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "phone = ? ";
		}
		if(sr.getReference() != null && sr.getReference().length() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "reference = ? ";
		}
		if(sr.getPrivilege() != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "privilege = ? ";
		}
		if(sr.getStatus() != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "status = ? ";
		}
		
		return query;
	}

	private static String getBookingSearchQuery(BookingSearchRepresentation sr) {
		String query = "SELECT * from BookingDao ";
		boolean start = false;
		
		/* Note:Make sure the order following is the same as that in Dao */
		
		if(sr.getBookingId() > 0){
			query += "where ";
			start = true;
			
			query += "id = ? ";
		}
		if(sr.getCourseId() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "course_Id = ? ";
		}
		if(sr.getPartnerId() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "p_Id = ? ";
		}
		if(sr.getUserId() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "u_Id = ? ";
		}		
		if(sr.getStartPrice() >= 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}	
			query += "price >= ? ";
		}
		if(sr.getFinishPrice() >= 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}	
			query += "price <= ? ";
		}		
		if(sr.getStatus() != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "status = ? ";
		}		
		if(sr.getReference() !=null && sr.getReference().length() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "reference = ? ";
		}
		if(sr.getCreationTime() != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "creationTime = ? ";
		}		
		if(sr.getScheduledTime() != null){
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += "scheduledTime = ? ";
		}	
		if(sr.getEmail() != null){
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += "email = ? ";
		}
		if(sr.getName() != null && sr.getName().length() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "name = ? ";
		}
		if(sr.getPhone() != null && sr.getPhone().length() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "phone = ? ";
		}
		if(sr.getAdjustTime() != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "adjustTime = ? ";
		}
		if(sr.getWasConfirmedIndex() != -1){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "wasConfirmed = ? ";
		}
		return query;
	}

	private static String getPartnerSearchQuery(PartnerSearchRepresentation sr) {
		String query = "SELECT * from PartnerDao ";		
		boolean start = false;		
		
		/* Note:Make sure the order following is the same as that in Dao */
		
		if(sr.getPartnerId()>0){
			query += "where ";
			start = true;
			
			query += "id = ? ";
		}
		if(sr.getCreationTime() != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "creationTime = ? ";
		}
		if(sr.getWholeName() != null && sr.getWholeName().length() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "name = ? ";
		}
		if(sr.getPhone() != null && sr.getPhone().length() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "phone = ? ";
		}
		if(sr.getStatus() != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}			
			query += "status = ? ";
		}		
		if(sr.getInstName() != null && sr.getInstName().length() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "instName = ? ";
		}
		if(sr.getLicence() != null && sr.getLicence().length() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "licence = ? ";
		}
		if(sr.getOrganizationNum() != null && sr.getOrganizationNum().length() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "organizationNum = ? ";
		}
		if(sr.getReference() != null && sr.getReference().length() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "reference = ? ";
		}		
		return query;
	}

	private static String getUserSearchQuery(UserSearchRepresentation sr) {
		String query = "SELECT * from UserDao ";		
		boolean start = false;	
		
		/* Note:Make sure the order following is the same as that in Dao */
		
		if(sr.getUserId()>0){
			query += "where ";
			start = true;
			
			query += "id = ? ";
		}
		if(sr.getCreationTime() != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "creationTime = ? ";
		}
		if(sr.getName() != null && sr.getName().length() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "name = ? ";
		}
		if(sr.getPhone() != null && sr.getPhone().length() > 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "phone = ? ";
		}		
		if(sr.getStatus() != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}			
			query += "status = ? ";
		}
		if(sr.getBalance() >= 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "balance = ? ";
		}
		if(sr.getCoupon() >= 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "coupon = ? ";
		}
		if(sr.getCredit() >= 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "credit = ? ";
		}
		if(sr.getEmail() != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}
			query += "email = ? ";
		}
		return query;
	}

	private static String getCourseSearchQuery(CourseSearchRepresentation sr) {
		
		String query = "SELECT * from CourseDao ";		
		String joinQuery = "JOIN PartnerDao On " +
				"CourseDao.p_Id = PartnerDao.id ";
		
		boolean joinQ = false;		
		boolean start = false;	

		/* Note:Make sure the order following is the same as that in Dao */
		
		if(sr.getPartnerId() > 0){				
			query += joinQuery;
			joinQ = true;				
		}
		if(sr.getInstitutionName() != null && sr.getInstitutionName().length() > 0){
			if(!joinQ){										
				query += joinQuery;
				joinQ = true;
			}				
			query += "where ";
			start = true;

			query += "PartnerDao.instName = ? ";
		}
		if(sr.getPartnerReference() != null && sr.getPartnerReference().length() > 0){
			if(!joinQ){										
				query += joinQuery;
				joinQ = true;
			}
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += "PartnerDao.reference = ? ";
		}

		if(sr.getCreationTime() != null){			
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += "CourseDao.creationTime = ? ";
		}
		if(sr.getStartTime() != null){
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += "CourseDao.startTime >= ? ";			
		}
		if(sr.getFinishTime() != null){
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += "CourseDao.finishTime <= ? ";
		}	

		if(sr.getStartPrice() >= 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}	
			query += "CourseDao.price >= ? ";
		}
		if(sr.getFinishPrice() >= 0){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}	
			query += "CourseDao.price <= ? ";
		}
		if(sr.getStatus() != null){
			if(!start){
				query += "where ";
				start = true;
			}else{
				query += "and ";
			}	
			query += "CourseDao.status = ?  ";
		}
		if(sr.getCategory()!=null&&sr.getCategory().length()>0){
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += "CourseDao.category = ? ";
		}
		if(sr.getSubCategory()!=null&&sr.getSubCategory().length()>0){
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += "CourseDao.subcategory = ? ";
		}
		if(sr.getCity()!=null&&sr.getCity().length()>0){
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += "CourseDao.city = ? ";
		}
		if(sr.getDistrict()!=null&&sr.getDistrict().length()>0){
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += "CourseDao.district = ? ";
		}
		if(sr.getCourseReference()!=null&&sr.getCourseReference().length()>0){
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += "CourseDao.reference = ? ";
		}
		if(sr.getCourseId()>0){
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += " CourseDao.id = ? ";
		}
		if(sr.getClassModel()!=null){
			if(start){				
				query += "and ";
			}else {
				query += "where ";
				start = true;
			}
			query += "CourseDao.classModel = ? ";
		}
		
		return query;
	}
}
