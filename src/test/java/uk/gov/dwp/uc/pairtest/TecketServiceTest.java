package uk.gov.dwp.uc.pairtest;

import org.junit.Test;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TecketServiceTest {
    TicketService ticketService = new TicketServiceImpl();
    Long accountId = 1L;

    @Test
    public void testMaxNoOfTickets() {
        TicketTypeRequest[] ticketTypeRequests = new TicketTypeRequest[1];
        ticketTypeRequests[0] = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 20);

        ticketService.purchaseTickets(accountId, ticketTypeRequests);
    }

    @Test
    public void testAdultTicketWithChildTickets() {
        TicketTypeRequest[] ticketTypeRequests = new TicketTypeRequest[2];
        ticketTypeRequests[0] = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        ticketTypeRequests[1] = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);

        ticketService.purchaseTickets(accountId, ticketTypeRequests);
    }

    @Test
    public void testAdultTicketWithInfantTickets() {
        TicketTypeRequest[] ticketTypeRequests = new TicketTypeRequest[2];
        ticketTypeRequests[0] = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        ticketTypeRequests[1] = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);

        ticketService.purchaseTickets(accountId, ticketTypeRequests);
    }

    @Test
    public void testAdultWithChildAndInfantTickets() {
        TicketTypeRequest[] ticketTypeRequests = new TicketTypeRequest[3];
        ticketTypeRequests[0] = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        ticketTypeRequests[1] = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);
        ticketTypeRequests[2] = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);

        ticketService.purchaseTickets(accountId, ticketTypeRequests);

    }

    @Test(expected = InvalidPurchaseException.class)
    public void testTicketExceedingMaxLimit() {
        TicketTypeRequest[] ticketTypeRequests = new TicketTypeRequest[1];
        ticketTypeRequests[0] = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 21);

        ticketService.purchaseTickets(accountId, ticketTypeRequests);
    }

    @Test(expected = InvalidPurchaseException.class)
    public void testChildTicketWithoutAdultTicket() {
        TicketTypeRequest[] ticketTypeRequests = new TicketTypeRequest[1];
        ticketTypeRequests[0] = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 7);

        ticketService.purchaseTickets(accountId, ticketTypeRequests);
    }

    @Test(expected = InvalidPurchaseException.class)
    public void testInfantTicketWithoutAdultTicket() {
        TicketTypeRequest[] ticketTypeRequests = new TicketTypeRequest[1];
        ticketTypeRequests[0] = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 6);

        ticketService.purchaseTickets(accountId, ticketTypeRequests);
    }

    @Test(expected = InvalidPurchaseException.class)
    public void testChildAndInfantTicketWithoutAdultTicket() {
        TicketTypeRequest[] ticketTypeRequests = new TicketTypeRequest[2];
        ticketTypeRequests[0] = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 7);
        ticketTypeRequests[1] = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 6);

        ticketService.purchaseTickets(accountId, ticketTypeRequests);
    }

    @Test(expected = InvalidPurchaseException.class)
    public void testTicketServiceWithNoTicket() {
        TicketTypeRequest[] ticketTypeRequests = new TicketTypeRequest[]{};

        ticketService.purchaseTickets(accountId, ticketTypeRequests);
    }

    @Test(expected = InvalidPurchaseException.class)
    public void testTicketServiceWithNullTicketTypeRequest() {
        TicketTypeRequest[] ticketTypeRequests = null;

        ticketService.purchaseTickets(accountId, ticketTypeRequests);
    }

}
