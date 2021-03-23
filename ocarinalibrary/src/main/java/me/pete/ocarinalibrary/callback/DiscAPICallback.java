package me.pete.ocarinalibrary.callback;

import me.pete.ocarinalibrary.enumerator.DiscountAPIResultEnum;

public abstract class DiscAPICallback {
    /**
     * This called when you after get response from API.
     */
    public void onBeforeCalculate() {

    }


    /**
     * This called when after calculation discount on this library
     * is done.
     *
     * @param mstCodeDisc           A Discount Code you get.
     * @param productCode           A product code that get a discount.
     * @param discountPercent       Discount in percentage
     * @param productCodeFree       A product code that get by free
     * @param freeQty               Small qty of Free goods
     * @param discountAPIResultEnum Type of discount (Ex: discount, free goods or mix between discount and free goods)
     */
    public void onFinishCalculate(String mstCodeDisc, String productCode, double discountPercent, String productCodeFree, int freeQty, DiscountAPIResultEnum discountAPIResultEnum) {

    }
}
