package BaseModule.dataTest;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.configurations.EnumConfig.CommissionStatus;
import BaseModule.configurations.EnumConfig.ServiceFeeStatus;
import BaseModule.service.ValidStateTransferService;

public class StateTransferTest {

	@Test
	public void test() {
		Map map1 = ValidStateTransferService.bookingStatusMap;
		Map map2 = ValidStateTransferService.serviceFeeStatusMap;
		Map map3 = ValidStateTransferService.commissionStatusMap;
		System.out.println(map1);
		System.out.println(map2);
		System.out.println(map3);
	}
	
	@Test
	public void testBookingDFA(){
		assertFalse(ValidStateTransferService.validateBookingStatusTransfer(BookingStatus.cancelled, BookingStatus.noPay));
		assertFalse(ValidStateTransferService.validateBookingStatusTransfer(BookingStatus.started, BookingStatus.registered));
		assertFalse(ValidStateTransferService.validateBookingStatusTransfer(BookingStatus.cancelled, BookingStatus.refunded));
		assertFalse(ValidStateTransferService.validateBookingStatusTransfer(BookingStatus.failed, BookingStatus.noShow));
		assertFalse(ValidStateTransferService.validateBookingStatusTransfer(BookingStatus.delivered, BookingStatus.confirmed));
		assertFalse(ValidStateTransferService.validateBookingStatusTransfer(BookingStatus.started, BookingStatus.consolidated));
		
		assertTrue(ValidStateTransferService.validateBookingStatusTransfer(BookingStatus.delivered, BookingStatus.late));
		assertTrue(ValidStateTransferService.validateBookingStatusTransfer(BookingStatus.registered, BookingStatus.noPay));
		assertTrue(ValidStateTransferService.validateBookingStatusTransfer(BookingStatus.paid, BookingStatus.refunded));
		assertTrue(ValidStateTransferService.validateBookingStatusTransfer(BookingStatus.confirmed, BookingStatus.cancelled));
		assertTrue(ValidStateTransferService.validateBookingStatusTransfer(BookingStatus.awaiting, BookingStatus.confirmed));
		assertTrue(ValidStateTransferService.validateBookingStatusTransfer(BookingStatus.started, BookingStatus.refunded));
	}
	
	@Test
	public void testServiceFeeDFA(){
		assertFalse(ValidStateTransferService.validateServiceFeeStatusTransfer(ServiceFeeStatus.consolidated, ServiceFeeStatus.hasCharged));
		assertFalse(ValidStateTransferService.validateServiceFeeStatusTransfer(ServiceFeeStatus.refundCharge, ServiceFeeStatus.consolidated));
		assertFalse(ValidStateTransferService.validateServiceFeeStatusTransfer(ServiceFeeStatus.noCharge, ServiceFeeStatus.shouldCharge));
		assertFalse(ValidStateTransferService.validateServiceFeeStatusTransfer(ServiceFeeStatus.shouldCharge, ServiceFeeStatus.consolidated));
		assertFalse(ValidStateTransferService.validateServiceFeeStatusTransfer(ServiceFeeStatus.hasCharged, ServiceFeeStatus.noCharge));
		
		assertTrue(ValidStateTransferService.validateServiceFeeStatusTransfer(ServiceFeeStatus.shouldCharge, ServiceFeeStatus.hasCharged));
		assertTrue(ValidStateTransferService.validateServiceFeeStatusTransfer(ServiceFeeStatus.shouldCharge, ServiceFeeStatus.noCharge));
		assertTrue(ValidStateTransferService.validateServiceFeeStatusTransfer(ServiceFeeStatus.hasCharged, ServiceFeeStatus.refundCharge));
		assertTrue(ValidStateTransferService.validateServiceFeeStatusTransfer(ServiceFeeStatus.refundCharge, ServiceFeeStatus.noCharge));
		assertTrue(ValidStateTransferService.validateServiceFeeStatusTransfer(ServiceFeeStatus.hasCharged, ServiceFeeStatus.consolidated));
	}
	
	@Test
	public void testCommissionDFA(){
		assertFalse(ValidStateTransferService.validateCommissionStatusTransfer(CommissionStatus.consolidated, CommissionStatus.hasCharged));
		assertFalse(ValidStateTransferService.validateCommissionStatusTransfer(CommissionStatus.refundCharge, CommissionStatus.consolidated));
		assertFalse(ValidStateTransferService.validateCommissionStatusTransfer(CommissionStatus.noCharge, CommissionStatus.shouldCharge));
		assertFalse(ValidStateTransferService.validateCommissionStatusTransfer(CommissionStatus.shouldCharge, CommissionStatus.consolidated));
		assertFalse(ValidStateTransferService.validateCommissionStatusTransfer(CommissionStatus.hasCharged, CommissionStatus.noCharge));
		
		assertTrue(ValidStateTransferService.validateCommissionStatusTransfer(CommissionStatus.shouldCharge, CommissionStatus.hasCharged));
		assertTrue(ValidStateTransferService.validateCommissionStatusTransfer(CommissionStatus.shouldCharge, CommissionStatus.noCharge));
		assertTrue(ValidStateTransferService.validateCommissionStatusTransfer(CommissionStatus.hasCharged, CommissionStatus.refundCharge));
		assertTrue(ValidStateTransferService.validateCommissionStatusTransfer(CommissionStatus.refundCharge, CommissionStatus.noCharge));
		assertTrue(ValidStateTransferService.validateCommissionStatusTransfer(CommissionStatus.hasCharged, CommissionStatus.consolidated));
	}

}
