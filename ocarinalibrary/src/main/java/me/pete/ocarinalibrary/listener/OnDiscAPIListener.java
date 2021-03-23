package me.pete.ocarinalibrary.listener;

import me.pete.ocarinalibrary.enumerator.DiscountAPIResultEnum;

public interface OnDiscAPIListener {
    /**
     * This called when you after get response from API.
     */
    void onBeforeCalculate();

    /**
     * This called when after calculation discount on this library
     * is done.
     *
     * @param mstCodeDisc               A Discount Code you get.
     * @param discountPercent           Discount in percentage
     * @param freeQty                   Small qty of Free goods
     * @param productCode               A product code that get a discount.
     * @param discountAPIResultEnum     Type of discount (Ex: discount, free goods or mix between discount and free goods)
     */
    void onFinishCalculate(String mstCodeDisc, double discountPercent, int freeQty, String productCode, DiscountAPIResultEnum discountAPIResultEnum);
}
