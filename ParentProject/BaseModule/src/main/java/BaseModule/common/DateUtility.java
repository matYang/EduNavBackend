package BaseModule.common;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;

import BaseModule.configurations.ServerConfig;
import BaseModule.exception.validation.ValidationException;
import BaseModule.model.Coupon;
import BaseModule.service.EncodingService;

public class DateUtility {

	public static long milisecInDay = 86400000l;
	public static long milisecInHour = 3600000l;


	public static Calendar getCurTimeInstance(){
		return Calendar.getInstance(TimeZone.getTimeZone(ServerConfig.timeZoneIdCH));
	}
	public static long getCurTime(){
		Calendar c = getCurTimeInstance();
		return c.getTimeInMillis();
	}
	public static Calendar getTimeFromLong(long mili){
		Calendar c = getCurTimeInstance();
		c.setTimeInMillis(mili);
		return c;
	}


	public static String toSQLDateTime(Calendar c){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(c.getTime());
	}

	public static Calendar DateToCalendar(Date date){ 
		if (date==null)return null;

		Calendar cal = getCurTimeInstance();
		cal.setTime(date);
		return cal;
	}


	public static int getHourDifference(Calendar startTime, Calendar endTime){
		return (int) ((endTime.getTimeInMillis() - startTime.getTimeInMillis())/ (1000*60*60));
	}


	public static Calendar castFromAPIFormat(String dateString) throws ValidationException{
		try{
			Calendar cal = getCurTimeInstance();
			dateString = EncodingService.decodeURI(dateString);
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			cal.setTime(sdf1.parse(dateString));
			return cal;
		} catch (ParseException | UnsupportedEncodingException e) {
			DebugLog.d(e);
			throw new ValidationException("无效日期格式");
		}
		
	}
	public static String castToAPIFormat(Calendar c) throws ValidationException{
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return EncodingService.encodeURI(sdf.format(c.getTime()));
		} catch (UnsupportedEncodingException e){
			DebugLog.d(e);
			throw new ValidationException("无效日期源");
		}
	}

	
	public static String castToDateFormat(Calendar c){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(c.getTime());
	}
	public static String castToReadableString(Calendar c){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(c.getTime());
	}


	public static String getTimeStamp(){
		return getCurTime() +"";
	}


	public static long getLongFromTimeStamp(String timeStamp){
		return Long.parseLong(timeStamp, 10);
	}



	public static int compareday(Calendar cal1, Calendar cal2){
		if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)){
			return -1;
		}
		else if (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) < cal2.get(Calendar.DAY_OF_YEAR)){
			return -1;
		}
		else if (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)){
			return 0;
		}
		else{
			return 1;
		}

	}
	
	public static Comparator<Coupon> couponExpireComparator = new Comparator<Coupon>(){
		public int compare(Coupon c1, Coupon c2){
			return toSQLDateTime(c1.getExpireTime()).compareTo(toSQLDateTime(c2.getExpireTime()));
		}	
	};
}
