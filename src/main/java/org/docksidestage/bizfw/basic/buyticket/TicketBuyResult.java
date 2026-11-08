package org.docksidestage.bizfw.basic.buyticket;

// TODO matsumoto 最低限のクラスJavaDocをお願いします。(一行概要とauthor) by jflute (2026/11/08)
public class TicketBuyResult {
    // TODO matsumoto immutableな属性なので、可読性のためにもfinalを付けましょう by jflute (2026/11/08)
    private int displayPrice;

    public TicketBuyResult(int displayPrice) {
        this.displayPrice = displayPrice;
    }

    public Ticket getTicket() {
        return new Ticket(TicketBooth.TWO_DAY_PRICE);
    }

    public int getChange() {
        // TODO matsumoto getメソッドの中で計算をすると、getが複数回呼ばれたときに何度も実行されてしまいます。 by jflute (2026/11/08)
        // getメソッドは「そこに置いてあるものをただ取るだけ」感の強いメソッドなので、
        // 呼び出し側も呼び出し回数に気を使って実装しない可能性があるので、
        // あまりgetの中でロジック的な処理は入れない方が無難です。(些細なパフォーマンスですが)
        // また、それがゆえに、getの中にロジックがあると思わない、という前提でコードを読む人も多いので、
        // getにあるとちょっと見つけづらいというのがあります。
        //
        // 一方で、calculateChange() みたいな名前だったらいいのか？ってところですが、
        // だいぶOKという感じですけども、このクラスはResultという入れ物クラス感の強いものなので、
        // 「お釣りの計算」という小さくてもれっきとしてビジネスロジックは入れ物クラスに入れない方が無難です。
        // TicketBoothで計算して、Resultはただ受け取って戻すだけの方が良いかなと。
        return (this.displayPrice - TicketBooth.TWO_DAY_PRICE);
    }
}
