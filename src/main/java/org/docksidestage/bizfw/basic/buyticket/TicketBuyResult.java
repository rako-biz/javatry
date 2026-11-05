package org.docksidestage.bizfw.basic.buyticket;

public class TicketBuyResult {
    private int displayPrice;

    public TicketBuyResult(int displayPrice) {
        this.displayPrice = displayPrice;
    }

    public Ticket getTicket() {
        return new Ticket(displayPrice);
    }

    public int getChange() {
        TicketBooth booth = new TicketBooth();
        return booth.buyTwoDayPassport(displayPrice).getChange();
    }
}
