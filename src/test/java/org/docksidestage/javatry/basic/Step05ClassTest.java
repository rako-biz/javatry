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
package org.docksidestage.javatry.basic;

import org.docksidestage.bizfw.basic.buyticket.Ticket;
import org.docksidestage.bizfw.basic.buyticket.TicketBooth;
import org.docksidestage.bizfw.basic.buyticket.TicketBooth.TicketShortMoneyException;
import org.docksidestage.unit.PlainTestCase;

/**
 * The test of class. <br>
 * Operate exercise as javadoc. If it's question style, write your answer before test execution. <br>
 * (javadocの通りにエクササイズを実施。質問形式の場合はテストを実行する前に考えて答えを書いてみましょう) <br>
 * 
 * If ambiguous requirement exists, you can determine specification that seems appropriate. <br>
 * (要件が曖昧なところがあれば、適切だと思われる仕様を決めても良いです)
 * 
 * @author jflute
 * @author r.matsumoto
 */
public class Step05ClassTest extends PlainTestCase {

    // ===================================================================================
    //                                                                          How to Use
    //                                                                          ==========
    /**
     * What string is sea variable at the method end? <br>
     * (メソッド終了時の変数 sea の中身は？)
     */
    public void test_class_howToUse_basic() {
        TicketBooth booth = new TicketBooth();
        booth.buyOneDayPassport(7400);
        int sea = booth.getQuantity();
        log(sea); // your answer? => 9
        // after test => 9
    }

    /** Same as the previous method question. (前のメソッドの質問と同じ) */
    public void test_class_howToUse_overpay() {
        TicketBooth booth = new TicketBooth();
        booth.buyOneDayPassport(10000);
        Integer sea = booth.getSalesProceeds();
        log(sea); // your answer? => 1 newしたインスタンスで購入関数を適用しているから
        // after test => 10000 購入回数だと勘違いしていたが、newした後のfirst purchaseなので、`salesProceeds = handedMoney;`これが評価されるだけ
        // after fixed test => 1　これで上記の問題はとりあえず解決した
    }

    /** Same as the previous method question. (前のメソッドの質問と同じ) */
    public void test_class_howToUse_nosales() {
        TicketBooth booth = new TicketBooth();
        Integer sea = booth.getSalesProceeds();
        log(sea); // your answer? => null salesProceedsは参照型のIntegerでフィールドとして初期化されているため
        // after test => null
    }

    /** Same as the previous method question. (前のメソッドの質問と同じ) */
    public void test_class_howToUse_wrongQuantity() {
        Integer sea = doTest_class_ticket_wrongQuantity();
        log(sea); // your answer? => 9 お金の不足分を判定する前に`--quantity;`しているのでmax - 1になるはず。そして例外処理が走る
        // after test => 9 合っているけど、お金ないのにquantity-1するのはどうなんだろう
        // after fixed test => 10 上記の問題は解決
    }

    private Integer doTest_class_ticket_wrongQuantity() {
        TicketBooth booth = new TicketBooth();
        int handedMoney = 7399;
        try {
            booth.buyOneDayPassport(handedMoney);
            fail("always exception but none");
        } catch (TicketShortMoneyException continued) {
            log("Failed to buy one-day passport: money=" + handedMoney, continued);
        }
        return booth.getQuantity();
    }

    // ===================================================================================
    //                                                                           Let's fix
    //                                                                           =========
    /**
     * Fix the problem of ticket quantity reduction when short money. (Don't forget to fix also previous exercise answers) <br>
     * (お金不足でもチケットが減る問題をクラスを修正して解決しましょう (以前のエクササイズのanswerの修正を忘れずに))
     */
    public void test_class_letsFix_ticketQuantityReduction() {
        Integer sea = doTest_class_ticket_wrongQuantity();
        log(sea); // should be max quantity, visual check here // quantityの計算をお金の判定の後に持ってきた
        // after test => 例外処理の表示の後に、max quantityの10が表示される
    }

    /**
     * Fix the problem of sales proceeds increased by handed money. (Don't forget to fix also previous exercise answers) <br>
     * (受け取ったお金の分だけ売上が増えていく問題をクラスを修正して解決しましょう (以前のエクササイズのanswerの修正を忘れずに))
     */
    public void test_class_letsFix_salesProceedsIncrease() {
        TicketBooth booth = new TicketBooth();
        booth.buyOneDayPassport(10000);
        Integer sea = booth.getSalesProceeds();
        log(sea); // should be same as one-day price, visual check here // 初回だったら1にして、二回目以降だったら1日券分のお金で割った分を足したいが、まだ1日券分のお金を渡されたものから引いてないので要検討
        // after test => 1
    }

    /**
     * Make method for buying two-day passport (price is 13200). (which can return change as method return value)
     * (TwoDayPassport (金額は13200) も買うメソッドを作りましょう (戻り値でお釣りをちゃんと返すように))
     */
    public void test_class_letsFix_makeMethod_twoday() {
        // uncomment after making the method
        TicketBooth booth = new TicketBooth();
        int money = 14000;
        int change = booth.buyTwoDayPassport(money);
        Integer sea = booth.getSalesProceeds() + change; // todo jflute ここを読むまでsalesProceedsが購入手続き数だと思っていて、売上金だと思わなかったが書いてある文的には売上金ということで問題ない？ by r.matsumoto
        log(sea); // should be same as money // 14000
        // after test => 14000

        // and show two-day passport quantity here
        // quantityは一日券と二日券で分けた方がいいかも？まとめてしまったら、quantityの例外処理が増える。分けたらどっちがどのぐらい売れたかが分かり、売上金も合算すればいいだけ。
        // getQuantity()がStep6でも使われている。としたら、quantity1つの中に券が二つ混在することになるがそれは良いのだろうか。
        int mountain;
        mountain = booth.getQuantity();
        log(mountain); // after test => 9
    }

    /**
     * Recycle duplicate logics between one-day and two-day by e.g. private method in class. (And confirm result of both before and after) <br>
     * (OneDayとTwoDayで冗長なロジックがあったら、クラス内のprivateメソッドなどで再利用しましょう (修正前と修正後の実行結果を確認))
     */
    public void test_class_letsFix_refactor_recycle() {
        TicketBooth booth = new TicketBooth();
        booth.buyOneDayPassport(10000);
        log(booth.getQuantity(), booth.getSalesProceeds()); // should be same as before-fix // before test => 9, 7400
        // after test => 9, 7400　同じだが、冗長の判断軸がよく分からなかった。どこまで共通化して良いのかが難しく、今回は例外処理であれば他も使う可能性ありと判断してそれらのみ再利用できるようにした。
    }

    // ===================================================================================
    //                                                                           Challenge
    //                                                                           =========
    /**
     * Now you cannot get ticket if you buy one-day passport, so return Ticket class and do in-park. <br>
     * (OneDayPassportを買ってもチケットをもらえませんでした。戻り値でTicketクラスを戻すようにしてインしましょう)
     */
    public void test_class_moreFix_return_ticket() {
        // uncomment out after modifying the method
        TicketBooth booth = new TicketBooth();
        Ticket oneDayPassport = booth.buyOneDayPassport(10000);
        log(oneDayPassport.getDisplayPrice()); // should be same as one-day price // after test => 7400
        log(oneDayPassport.isAlreadyIn()); // should be false // after test => false
        oneDayPassport.doInPark();
        log(oneDayPassport.isAlreadyIn()); // should be true // after test => true
    }

    /**
     * Now also you cannot get ticket if two-day passport, so return class that has ticket and change. <br>
     * (TwoDayPassportもチケットをもらえませんでした。チケットとお釣りを戻すクラスを作って戻すようにしましょう)
     */
    public void test_class_moreFix_return_whole() {
        // uncomment after modifying the method
        //TicketBooth booth = new TicketBooth();
        //int handedMoney = 20000;
        //TicketBuyResult buyResult = booth.buyTwoDayPassport(handedMoney);
        //Ticket twoDayPassport = buyResult.getTicket();
        //int change = buyResult.getChange();
        //log(twoDayPassport.getDisplayPrice() + change); // should be same as money
    }

    /**
     * Now you can use only one in spite of two-day passport, so fix Ticket to be able to handle plural days. <br>
     * (TwoDayPassportなのに一回しか利用できません。複数日数に対応できるようにTicketを修正しましょう)
     */
    public void test_class_moreFix_usePluralDays() {
        // your confirmation code here
    }

    /**
     * Accurately determine whether type of bought ticket is two-day passport or not by if-statement. (fix Ticket classes if needed) <br>
     * (買ったチケットの種別がTwoDayPassportなのかどうかをif文で正確に判定してみましょう。(必要ならTicketクラスたちを修正))
     */
    public void test_class_moreFix_whetherTicketType() {
        // uncomment when you implement this exercise
        //TicketBooth booth = new TicketBooth();
        //Ticket oneDayPassport = booth.buyOneDayPassport(10000);
        //showTicketIfNeeds(oneDayPassport);
        //TicketBuyResult buyResult = booth.buyTwoDayPassport(10000);
        //Ticket twoDayPassport = buyResult.getTicket();
        //showTicketIfNeeds(twoDayPassport);
    }

    // uncomment when you implement this exercise
    //private void showTicketIfNeeds(Ticket ticket) {
    //    if (xxxxxxxxxxxxxxxxxx) { // write determination for two-day passport
    //        log("two-day passport");
    //    } else {
    //        log("other");
    //    }
    //}

    // ===================================================================================
    //                                                                           Good Luck
    //                                                                           =========
    /**
     * Fix it to be able to buy four-day passport (price is 22400). <br>
     * (FourDayPassport (金額は22400) のチケットも買えるようにしましょう)
     */
    public void test_class_moreFix_wonder_four() {
        // your confirmation code here
    }

    /**
     * Fix it to be able to buy night-only two-day passport (price is 7400), which can be used at only night. <br>
     * (NightOnlyTwoDayPassport (金額は7400) のチケットも買えるようにしましょう。夜しか使えないようにしましょう)
     */
    public void test_class_moreFix_wonder_night() {
        // your confirmation code here
    }

    // ===================================================================================
    //                                                                         Bonus Stage
    //                                                                         ===========
    /**
     * Refactor the code to the best readable code you can think of. <br>
     * (自分の中で思う最高に可読性の高いコードにリファクタリングしてみましょう)
     */
    public void test_class_moreFix_yourRefactoring() {
        // your confirmation code here
    }

    /**
     * Write intelligent JavaDoc comments seriously on the public classes/constructors/methods of the Ticket class. <br>
     * (Ticketクラスのpublicなクラス/コンストラクター/メソッドに、気の利いたJavaDocコメントを本気で書いてみましょう) <br>
     * <br>
     * Seriously → With the intention that the Ticket class (for example) becomes open source and is used by hundreds of people. <br>
     * (本気で → Ticketクラスが(例えば)オープンソースになって何百人の人から利用される想定のつもりで。)
     */
    public void test_class_moreFix_yourSuperJavaDoc() {
        // your confirmation code here
    }

    // ===================================================================================
    //                                                                         Devil Stage
    //                                                                         ===========
    /**
     * If your specification is to share inventory (quantity) between OneDay/TwoDay/...,
     * change the specification to separate inventory for each OneDay/TwoDay/.... <br>
     * (もし、OneDay/TwoDay/...で在庫(quantity)を共有する仕様になってたら、
     * OneDay/TwoDay/...ごとに在庫を分ける仕様に変えてみましょう)
     */
    public void test_class_moreFix_zonedQuantity() {
        // your confirmation code here
    }
}
