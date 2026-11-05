package org.docksidestage.bizfw.basic.buyticket;

public class TicketBuyResult {
    private int displayPrice;

    public TicketBuyResult(int displayPrice) {
        this.displayPrice = displayPrice;
    }

    public Ticket getTicket() {
        return new Ticket(TicketBooth.TWO_DAY_PRICE);
    }

    public int getChange() {
        return (this.displayPrice - TicketBooth.TWO_DAY_PRICE);
    }
}
