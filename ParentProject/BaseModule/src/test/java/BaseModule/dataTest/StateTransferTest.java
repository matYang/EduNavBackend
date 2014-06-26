package BaseModule.dataTest;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import BaseModule.configurations.EnumConfig.BookingStatus;
import BaseModule.configurations.EnumConfig.CommissionStatus;
import BaseModule.configurations.EnumConfig.ServiceFeeStatus;
import BaseModule.service.StateTransferService;

public class StateTransferTest {

	@Test
	public void test() {
		Map map1 = StateTransferService.bookingStatusMap;
		Map map2 = StateTransferService.serviceFeeStatusMap;
		Map map3 = StateTransferService.commissionStatusMap;
		System.out.println(map1);
		System.out.println(map2);
		System.out.println(map3);
	}
	
	@Test
	public void testBookingDFA(){
		assertFalse(StateTransferService.validateBookingStatusTransfer(BookingStatus.cancelled, BookingStatus.noPay));
		assertFalse(StateTransferService.validateBookingStatusTransfer(BookingStatus.started, BookingStatus.registered));
		assertFalse(StateTransferService.validateBookingStatusTransfer(BookingStatus.cancelled, BookingStatus.refunded));
		assertFalse(StateTransferService.validateBookingStatusTransfer(BookingStatus.failed, BookingStatus.noShow));
		assertFalse(StateTransferService.validateBookingStatusTransfer(BookingStatus.delivered, BookingStatus.confirmed));
		assertFalse(StateTransferService.validateBookingStatusTransfer(BookingStatus.started, BookingStatus.consolidated));
		
		assertTrue(StateTransferService.validateBookingStatusTransfer(BookingStatus.delivered, BookingStatus.late));
		assertTrue(StateTransferService.validateBookingStatusTransfer(BookingStatus.registered, BookingStatus.noPay));
		assertTrue(StateTransferService.validateBookingStatusTransfer(BookingStatus.paid, BookingStatus.refunded));
		assertTrue(StateTransferService.validateBookingStatusTransfer(BookingStatus.confirmed, BookingStatus.cancelled));
		assertTrue(StateTransferService.validateBookingStatusTransfer(BookingStatus.awaiting, BookingStatus.confirmed));
		assertTrue(StateTransferService.validateBookingStatusTransfer(BookingStatus.started, BookingStatus.refunded));
	}
	
	@Test
	public void testServiceFeeDFA(){
		assertFalse(StateTransferService.validateServiceFeeStatusTransfer(ServiceFeeStatus.consolidated, ServiceFeeStatus.hasCharged));
		assertFalse(StateTransferService.validateServiceFeeStatusTransfer(ServiceFeeStatus.refundCharge, ServiceFeeStatus.consolidated));
		assertFalse(StateTransferService.validateServiceFeeStatusTransfer(ServiceFeeStatus.noCharge, ServiceFeeStatus.shouldCharge));
		assertFalse(StateTransferService.validateServiceFeeStatusTransfer(ServiceFeeStatus.shouldCharge, ServiceFeeStatus.consolidated));
		assertFalse(StateTransferService.validateServiceFeeStatusTransfer(ServiceFeeStatus.hasCharged, ServiceFeeStatus.noCharge));
		
		assertTrue(StateTransferService.validateServiceFeeStatusTransfer(ServiceFeeStatus.shouldCharge, ServiceFeeStatus.hasCharged));
		assertTrue(StateTransferService.validateServiceFeeStatusTransfer(ServiceFeeStatus.shouldCharge, ServiceFeeStatus.noCharge));
		assertTrue(StateTransferService.validateServiceFeeStatusTransfer(ServiceFeeStatus.hasCharged, ServiceFeeStatus.refundCharge));
		assertTrue(StateTransferService.validateServiceFeeStatusTransfer(ServiceFeeStatus.refundCharge, ServiceFeeStatus.noCharge));
		assertTrue(StateTransferService.validateServiceFeeStatusTransfer(ServiceFeeStatus.hasCharged, ServiceFeeStatus.consolidated));
	}
	
	@Test
	public void testCommissionDFA(){
		assertFalse(StateTransferService.validateCommissionStatusTransfer(CommissionStatus.consolidated, CommissionStatus.hasCharged));
		assertFalse(StateTransferService.validateCommissionStatusTransfer(CommissionStatus.refundCharge, CommissionStatus.consolidated));
		assertFalse(StateTransferService.validateCommissionStatusTransfer(CommissionStatus.noCharge, CommissionStatus.shouldCharge));
		assertFalse(StateTransferService.validateCommissionStatusTransfer(CommissionStatus.shouldCharge, CommissionStatus.consolidated));
		assertFalse(StateTransferService.validateCommissionStatusTransfer(CommissionStatus.hasCharged, CommissionStatus.noCharge));
		
		assertTrue(StateTransferService.validateCommissionStatusTransfer(CommissionStatus.shouldCharge, CommissionStatus.hasCharged));
		assertTrue(StateTransferService.validateCommissionStatusTransfer(CommissionStatus.shouldCharge, CommissionStatus.noCharge));
		assertTrue(StateTransferService.validateCommissionStatusTransfer(CommissionStatus.hasCharged, CommissionStatus.refundCharge));
		assertTrue(StateTransferService.validateCommissionStatusTransfer(CommissionStatus.refundCharge, CommissionStatus.noCharge));
		assertTrue(StateTransferService.validateCommissionStatusTransfer(CommissionStatus.hasCharged, CommissionStatus.consolidated));
	}

}
