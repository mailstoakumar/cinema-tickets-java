package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationService;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TicketServiceImpl implements TicketService {
    /**
     * Should only have private methods other than the one below.
     */
    private static final int MAX_TICKETS = 20;
    private static final int ADULT_TICKET_PRICE = 20;
    private static final int CHILD_TICKET_PRICE = 10;
    private static final int INFANT_TICKET_PRICE = 0;
    private static final Map<TicketTypeRequest.Type, Integer> ticketTypePriceMap;

    static {
        Map<TicketTypeRequest.Type, Integer> priceTable = new HashMap<>();
        priceTable.put(TicketTypeRequest.Type.ADULT, ADULT_TICKET_PRICE);
        priceTable.put(TicketTypeRequest.Type.CHILD, CHILD_TICKET_PRICE);
        priceTable.put(TicketTypeRequest.Type.INFANT, INFANT_TICKET_PRICE);
        ticketTypePriceMap = Collections.unmodifiableMap(priceTable);
    }

    private TicketPaymentService ticketPaymentService = new TicketPaymentServiceImpl();
    private SeatReservationService seatReservationService = new SeatReservationServiceImpl();

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {

        if (!isValidRequest(ticketTypeRequests)) {
            throw new InvalidPurchaseException();
        }

        int totalAmountToPay = getCalculatedAmountToPay(ticketTypeRequests);
        int numberOfSeatsToAllocate = getNumberOfSeatsToAllocate(ticketTypeRequests);

        ticketPaymentService.makePayment(accountId, totalAmountToPay);
        seatReservationService.reserveSeat(accountId, numberOfSeatsToAllocate);
    }

    private int getCalculatedAmountToPay(TicketTypeRequest[] ticketTypeRequests) {
        return Arrays.stream(ticketTypeRequests)
                .mapToInt(ticketTypeRequest -> ticketTypeRequest.getNoOfTickets() * ticketTypePriceMap.get(ticketTypeRequest.getTicketType())).sum();

    }

    private int getNumberOfSeatsToAllocate(TicketTypeRequest[] ticketTypeRequests) {
        return Arrays.stream(ticketTypeRequests)
                .mapToInt(ticketTypeRequest -> {
                            if (ticketTypeRequest.getTicketType() != TicketTypeRequest.Type.INFANT) {
                                return ticketTypeRequest.getNoOfTickets();
                            }
                            return 0;
                        }
                ).sum();

    }

    private boolean isValidRequest(TicketTypeRequest[] ticketTypeRequests) {

        if (ticketTypeRequests == null || ticketTypeRequests.length == 0) {
            return false;
        }

        int numberOfTickets = Arrays.stream(ticketTypeRequests)
                .mapToInt(ticketTypeRequest -> ticketTypeRequest.getNoOfTickets()).sum();

        if (numberOfTickets > MAX_TICKETS) {
            return false;
        }

        boolean noAdultTicketIncluded = Arrays.stream(ticketTypeRequests).noneMatch(ticketTypeRequest -> ticketTypeRequest.getTicketType() == TicketTypeRequest.Type.ADULT);
        if (noAdultTicketIncluded == true) {
            return false;
        }

        return true;
    }
}
