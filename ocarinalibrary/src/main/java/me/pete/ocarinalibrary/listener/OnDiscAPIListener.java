package me.pete.ocarinalibrary.listener;

import me.pete.ocarinalibrary.enumerator.DiscountAPIResultEnum;

public interface OnDiscAPIListener {
    void onBeforeCalculate();
    void onCalculate();
    void onFinishCalculate(String mstCodeDisc, double discountPercent, String productCode, int freeQty, DiscountAPIResultEnum discountAPIResultEnum);
}
