package BaseModule.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import BaseModule.common.DebugLog;
import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.configurations.EnumConfig.CommissionStatus;
import BaseModule.configurations.EnumConfig.ServiceFeeStatus;

public final class StateTransferService {
	
	private static final String bookingStatusTransferPath = "src/main/resources/BookingStatusTransfer";
	private static final String ServiceFeeStatusTransferPath = "src/main/resources/ServiceFeeStatusTransfer";
	private static final String CommissionStatusTransferPath = "src/main/resources/CommissionStatusTransfer";
	
	//TODO public for primary testing, must be changed to private
	public static final ConcurrentHashMap<BookingStatus, List<BookingStatus>> bookingStatusMap = new ConcurrentHashMap<BookingStatus, List<BookingStatus>>();
	public static final ConcurrentHashMap<ServiceFeeStatus, List<ServiceFeeStatus>> serviceFeeStatusMap = new ConcurrentHashMap<ServiceFeeStatus, List<ServiceFeeStatus>>();
	public static final ConcurrentHashMap<CommissionStatus, List<CommissionStatus>> commissionStatusMap = new ConcurrentHashMap<CommissionStatus, List<CommissionStatus>>();
	
	static{
		DebugLog.d("Starting to load booking status DFA: " + bookingStatusTransferPath);
		BufferedReader br = null;
		int lineNumber = 1;
		
		try {
			String curLine;
			br = new BufferedReader(new FileReader(bookingStatusTransferPath));
			
			
			BookingStatus curStatus = null;
			while ((curLine = br.readLine()) != null && curLine.length() > 0) {
				curLine = curLine.trim();
				//odd line numbers indicate the current state
				if (lineNumber % 2 == 1){
					curStatus = BookingStatus.fromInt(Integer.parseInt(curLine));
				}
				//even line numbers contains the possible destination states
				else{
					//$ sign means end of states, marking a terminating state in the DFA
					ArrayList<BookingStatus> curDestStatusList = new ArrayList<BookingStatus>();
					if (curLine.equals("$")){
						//do nothing, leaving the list empty
					}
					else{
						//store all destinations states into the list
						String[] statusArr = curLine.split("-");
						if (statusArr.length == 0){
							throw new RuntimeException("[ERR] Fatal error,  destination states must not be empty for a non-terminating state at line: " + lineNumber);
						}
						for (String statusStr : statusArr){
							curDestStatusList.add(BookingStatus.fromInt(Integer.parseInt(statusStr)));
						}
					}
					//make the list immutable to guarantee read-only and thread safety
					List<BookingStatus> finalDestStatusList = Collections.unmodifiableList(curDestStatusList);
					//put the kvp into map
					bookingStatusMap.put(curStatus, finalDestStatusList);
				}
				lineNumber++;
			}
			
			//last line must be even, so final line number variable must be odd
			if (lineNumber % 2 != 1){
				throw new RuntimeException("[ERR] Fatal error, incomplete DFA - invalid EOF state");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("[ERR] Fatal error when reading booking status DFA at line: " + lineNumber);
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	static{
		DebugLog.d("Starting to load service fee status DFA: " + ServiceFeeStatusTransferPath);
		BufferedReader br = null;
		int lineNumber = 1;
		
		try {
			String curLine;
			br = new BufferedReader(new FileReader(ServiceFeeStatusTransferPath));
			
			
			ServiceFeeStatus curStatus = null;
			while ((curLine = br.readLine()) != null && curLine.length() > 0) {
				curLine = curLine.trim();
				//odd line numbers indicate the current state
				if (lineNumber % 2 == 1){
					curStatus = ServiceFeeStatus.fromInt(Integer.parseInt(curLine));
				}
				//even line numbers contains the possible destination states
				else{
					//$ sign means end of states, marking a terminating state in the DFA
					ArrayList<ServiceFeeStatus> curDestStatusList = new ArrayList<ServiceFeeStatus>();
					if (curLine.equals("$")){
						//do nothing, leaving the list empty
					}
					else{
						//store all destinations states into the list
						String[] statusArr = curLine.split("-");
						if (statusArr.length == 0){
							throw new RuntimeException("[ERR] Fatal error,  destination states must not be empty for a non-terminating state at line: " + lineNumber);
						}
						for (String statusStr : statusArr){
							curDestStatusList.add(ServiceFeeStatus.fromInt(Integer.parseInt(statusStr)));
						}
					}
					//make the list immutable to guarantee read-only and thread safety
					List<ServiceFeeStatus> finalDestStatusList = Collections.unmodifiableList(curDestStatusList);
					//put the kvp into map
					serviceFeeStatusMap.put(curStatus, finalDestStatusList);
				}
				lineNumber++;
			}
			
			//last line must be even, so final line number variable must be odd
			if (lineNumber % 2 != 1){
				throw new RuntimeException("[ERR] Fatal error, incomplete DFA - invalid EOF state");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("[ERR] Fatal error when reading servicefee status DFA at line: " + lineNumber);
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	static{
		DebugLog.d("Starting to load commission status DFA: " + CommissionStatusTransferPath);
		BufferedReader br = null;
		int lineNumber = 1;
		
		try {
			String curLine;
			br = new BufferedReader(new FileReader(CommissionStatusTransferPath));
			
			
			CommissionStatus curStatus = null;
			while ((curLine = br.readLine()) != null && curLine.length() > 0) {
				curLine = curLine.trim();
				//odd line numbers indicate the current state
				if (lineNumber % 2 == 1){
					curStatus = CommissionStatus.fromInt(Integer.parseInt(curLine));
				}
				//even line numbers contains the possible destination states
				else{
					//$ sign means end of states, marking a terminating state in the DFA
					ArrayList<CommissionStatus> curDestStatusList = new ArrayList<CommissionStatus>();
					if (curLine.equals("$")){
						//do nothing, leaving the list empty
					}
					else{
						//store all destinations states into the list
						String[] statusArr = curLine.split("-");
						if (statusArr.length == 0){
							throw new RuntimeException("[ERR] Fatal error,  destination states must not be empty for a non-terminating state at line: " + lineNumber);
						}
						for (String statusStr : statusArr){
							curDestStatusList.add(CommissionStatus.fromInt(Integer.parseInt(statusStr)));
						}
					}
					//make the list immutable to guarantee read-only and thread safety
					List<CommissionStatus> finalDestStatusList = Collections.unmodifiableList(curDestStatusList);
					//put the kvp into map
					commissionStatusMap.put(curStatus, finalDestStatusList);
				}
				lineNumber++;
			}
			
			//last line must be even, so final line number variable must be odd
			if (lineNumber % 2 != 1){
				throw new RuntimeException("[ERR] Fatal error, incomplete DFA - invalid EOF state");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("[ERR] Fatal error when reading commission status DFA at line: " + lineNumber);
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	
	public static boolean validateBookingStatusTransfer(final BookingStatus preStatus, final BookingStatus newStatus){
		List<BookingStatus> statusList = bookingStatusMap.get(preStatus);
		if (statusList == null || statusList.size() == 0){
			return false;
		}
		else{
			for (BookingStatus status : statusList){
				if (status == newStatus){
					return true;
				}
			}
			return false;
		}
	}
	
	
	public static boolean validateServiceFeeStatusTransfer(final ServiceFeeStatus preStatus, final ServiceFeeStatus newStatus){
		List<ServiceFeeStatus> statusList = serviceFeeStatusMap.get(preStatus);
		if (statusList == null || statusList.size() == 0){
			return false;
		}
		else{
			for (ServiceFeeStatus status : statusList){
				if (status == newStatus){
					return true;
				}
			}
			return false;
		}
	}
	
	public static boolean validateCommissionStatusTransfer(final CommissionStatus preStatus, final CommissionStatus newStatus){
		List<CommissionStatus> statusList = commissionStatusMap.get(preStatus);
		if (statusList == null || statusList.size() == 0){
			return false;
		}
		else{
			for (CommissionStatus status : statusList){
				if (status == newStatus){
					return true;
				}
			}
			return false;
		}
	}
	
	

}
