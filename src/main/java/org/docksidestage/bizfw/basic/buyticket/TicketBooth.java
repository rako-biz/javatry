/*
 * Copyright 2019-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.docksidestage.bizfw.basic.buyticket;

// #1on1: AIでしごとなくなるのはベテランも？ (2026/11/26)
// 不安を持っているのはyes。
// 作業の抽象度はどんどん上がってくる。
// でもやることはいっぱいある。楽になった気がしない。
// コーディングは少なくなってきてる。
// コードとシステムのマネジメントに寄ってきている。
// どんどんビジネス寄りになってくる。
// jflute: 人生色々な経験することで、社会の仕組み自体に興味を持てるようになって...
// システムの対象ビジネスも理解しやすくなって興味も(ある程度)持てるようになった。
// 
// てんしょくで技術興味のある会社とビジネス興味のある会社。
// jflute: 技術興味のある会社の方のイメージではある
// とはいえ、お金をもらっているからには、ある一定のビジネス興味も出して成果に活かす。
//
// $逆にビジネスに関わる機会が少ない。
// これが5年とか続いたら確かにってのはあるけど、まだ半年なので...
// 今の仕事でしか得られないものもある。それを得られる内に得ておく。
// 今目の前の仕事を最大限やること、最大限って実は大変。
//
// TODO matsumoto [読み物課題] まず何より、目の前の道具を使いこなしてください by jflute (2026/11/26)
// https://jflute.hatenadiary.jp/entry/20180223/mastercurrent
//
// 悩む時間がもったいない、という考え方。
//
// $最大限は？与えられたIssueの中の...
// o 与えられたIssueの高い品質
// o 終わりにするスピード (インプット深堀りする時間を入れ込められるように)
// o システム全体の仕組みやビジネスの把握度 (範囲の広さ)
//
// TODO matsumoto [読み物課題]プライベートで一切勉強しなくても優秀になる七つのコツ by jflute (2026/11/26)
// https://jflute.hatenadiary.jp/entry/20180911/outofprivate
//
// $深堀りってどこまで？ (まつもとさんの特徴からすると、どこまで深ぼっちゃう)
// 意識して「踊り場」を見つけること。絶対のどこかにキリの良いラインがあるはず。
// 踊り場見つけてメモっておくなどして、いつでも再開できるようにしておく。
// 踊り場を見つけてないと、1年後に取り出しやすい記憶にならない。
// (1丁目を塗りつぶすこと)
//
// done matsumoto クラスJavaDoc, @authorの追加をお願いします by jflute (2026/11/08)

// TODO jflute 次回ペアプロの話 (2026/11/26)
/**
 * チケットの購入などの処理を担当するクラス
 * @author jflute
 * @author r.matsumoto
 */
public class TicketBooth {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final int MAX_QUANTITY = 10;
    protected static final int ONE_DAY_PRICE = 7400; // when 2019/06/15
    protected static final int TWO_DAY_PRICE = 13200;

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private int quantity = MAX_QUANTITY;
    private Integer salesProceeds; // null allowed: until first purchase

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public TicketBooth() {
    }

    // ===================================================================================
    //                                                                          Buy Ticket
    //                                                                          ==========
    // you can rewrite comments for your own language by jflute
    // e.g. Japanese
    // /**
    // * 1Dayパスポートを買う、パークゲスト用のメソッド。
    // * @param handedMoney パークゲストから手渡しされたお金(金額) (NotNull, NotMinus)
    // * @throws TicketSoldOutException ブース内のチケットが売り切れだったら
    // * @throws TicketShortMoneyException 買うのに金額が足りなかったら
    // */
    // done matsumoto JavaDoc, @returnを追加お願いします(戻り値をvoidからTicketに変えているので) by jflute (2026/11/08)
    /**
     * Buy one-day passport, method for park guest.
     * @param handedMoney The money (amount) handed over from park guest. (NotNull, NotMinus)
     * @throws TicketSoldOutException When ticket in booth is sold out.
     * @throws TicketShortMoneyException When the specified money is short for purchase.
     * @return Ticket 戻り値をvoidからTicketに変更し、お金がくれたら購入してTicketを返すようにした
     */
    public Ticket buyOneDayPassport(Integer handedMoney) {
        // TODO done matsumoto 再利用し過ぎし過ぎないの話をしましたが、トレーニングために積極的リファクタリング！ by jflute (2026/11/08)
        // OneDayPassport, TwoDayPassport で処理の流れが全く同じなので、流れを再利用してみましょう。
        // 何か処理の順番が変更になったとき、OneDay/TwoDay両方直すのではなく、一箇所直すだけで済むように。
        buyPassport(handedMoney, ONE_DAY_PRICE);
        return new Ticket(ONE_DAY_PRICE);
    }

    public TicketBuyResult buyTwoDayPassport(int handedMoney) {
        buyPassport(handedMoney, TWO_DAY_PRICE);
        // ここは上の金額ショートするかの判定を使ったほうがまとまりとしてはいいかもしれないが、returnすると考えるとquantityやsalesProceedsの計算ができなくなる。returnだけでも問題なさそう？
        // 例外処理として条件は省いてあるからreturnだけの方がいい？
        // そもそもif文で条件書いてreturnしたら`java: return文が指定されていません`とコンパイルエラーになった
        // if文の例：if (TWO_DAY_PRICE <= handedMoney) { return (handedMoney - TWO_DAY_PRICE)};
        return new TicketBuyResult(handedMoney);
    }

    // TODO jflute リファクタリングで処理をまとめようとしたが、ここは基本型のラッパークラスでまとめちゃってもいい？懸念点はある？ by r.matsumoto (2026/11/25)
    private void buyPassport(Integer handedMoney, int DAY_PRICE){
        quantityExist(quantity);
        checkHandedMoney(handedMoney, DAY_PRICE);
        --quantity;
        calculateSalesProceeds(salesProceeds, DAY_PRICE);
    }

    // TODO done matsumoto 切り出したメソッドたちは公開する必要がないのでpublicではなくprivateで by jflute (2026/11/08)

    // ここはcheck, existという単語に近いものを使うのだったらbooleanでも良いかと思ったが、booleanを使う用途が無い
    // TODO matsumoto [ふぉろー] booleanを戻すまでもなくここで例外throwで十分なのでそれでGoodです by jflute (2026/11/08)
    // TODO matsumoto メソッド名、何かしら動詞でthrowする感が欲しいところですね。 by jflute (2026/11/08)
    // 一方で、checkHandedMoney() の方はcheckが付いていますので、統一性のためにも。
    // (checkが良いのかは置いておいて)
    private void quantityExist(int quantity) {
        if (quantity <= 0) {
            throw new TicketSoldOutException("Sold out");
        }
    }

    // ここも上記と同じ懸念点。
    private void checkHandedMoney(int handedMoney, int dayPrice) {
        if (handedMoney < dayPrice) {
            throw new TicketShortMoneyException("Short money: " + handedMoney);
        }
    }

    // そもそもsalesProceedsをIntegerで持つ必要はあるのか？intとかあればunsigned intにしておけば初期値が0になるしクラスを合わせなくていい気がする
    // TODO matsumoto [へんじ] ほとんど必要ないと言えると思いますので int にしちゃっても良いレベルです。 by jflute (2026/11/08)
    // あえて出すなら、「未購入の状態」と「購入したけど0円(0円商品がある場合)」を区別できることってくらいですね。
    // それを区別する必要はほぼないでしょうし、必要であればそれは別のことで表現してもよいでしょうし。
    private void calculateSalesProceeds(Integer salesProceeds, int dayPrice) {
        if (salesProceeds != null) { // second or more purchase
            salesProceeds = salesProceeds + dayPrice;
        } else { // first purchase
            salesProceeds = dayPrice;
        }
    }

    public static class TicketSoldOutException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public TicketSoldOutException(String msg) {
            super(msg);
        }
    }

    public static class TicketShortMoneyException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public TicketShortMoneyException(String msg) {
            super(msg);
        }
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public int getQuantity() {
        return quantity;
    }

    public Integer getSalesProceeds() {
        return salesProceeds;
    }
}
