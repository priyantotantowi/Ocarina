package me.pete.ocarinalibrary.client;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import me.pete.ocarinalibrary.callback.DiscAPICallback;
import me.pete.ocarinalibrary.enumerator.BodyPostTypeEnum;
import me.pete.ocarinalibrary.enumerator.DiscountAPIResultEnum;
import me.pete.ocarinalibrary.helper.DoubleHelper;
import me.pete.ocarinalibrary.helper.HttpHelper;
import me.pete.ocarinalibrary.jsonobject.DiscAPIDataResponseJsonObject;
import me.pete.ocarinalibrary.jsonobject.DiscAPIHeaderDataResponseJsonObject;
import me.pete.ocarinalibrary.jsonobject.DiscAPIHeaderResponseJsonObject;
import me.pete.ocarinalibrary.jsonobject.DiscAPIJsonObject;
import me.pete.ocarinalibrary.jsonobject.DiscAPIOrderJsonObject;
import me.pete.ocarinalibrary.jsonobject.DiscAPIResponseJsonObject;
import me.pete.ocarinalibrary.listener.OnCallbackListener;
import me.pete.ocarinalibrary.listener.OnDiscAPIListener;

public final class DiscAPIClient {
    private boolean isCalculate, isCancelable;
    private Activity activity;
    private DiscAPIJsonObject discAPIJsonObject;
    private ProgressDialog dialog;
    private String url;

    private DiscAPICallback discAPICallback;

    /**
     * This function used for build a discount API before test to server.
     *
     * @param activity              Your Activity
     * @param url                   Your API address for discount calculate
     * @param discAPIJsonObject     Discount API JsonObject
     * @param discAPICallback       Callback of function.
     * @return
     */
    public DiscAPIClient build(Activity activity, String url, final DiscAPIJsonObject discAPIJsonObject, final DiscAPICallback discAPICallback) {
        this.activity = activity;
        this.url = url;
        this.discAPIJsonObject = discAPIJsonObject;
        this.discAPICallback = discAPICallback;
        return this;
    }

    public void onCalculateDiscAPI() {
        isCalculate = true;
        dialog = new ProgressDialog(activity);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Calculate Discount...");
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isCancelable = true;
                dialog.dismiss();
            }
        });
        dialog.show();

        if(discAPIJsonObject.getORDER().isEmpty()) {
            dialog.dismiss();
            isCancelable = true;
            isCalculate = false;
            return;
        }

        final String json = new Gson().toJson(discAPIJsonObject);
        Log.i("CalculateDisc-Json", json);
        try{
            HttpHelper httpHelper = new HttpHelper(url);
            httpHelper.setTimeout(45);
            httpHelper.setBodyPostTypeEnum(BodyPostTypeEnum.RAW);
            httpHelper.setHttpRawBody(json);
            httpHelper.connect(new OnCallbackListener() {
                @Override
                public void onFailure(String message) {
                    dialog.dismiss();
                    isCancelable = true;
                    isCalculate = false;
                }

                @Override
                public void onResponse(String response) {
                    Log.i("CalculateDisc-Response", response);

                    if(isCancelable) {
                        return;
                    }

                    try {
                        discAPICallback.onBeforeCalculate();
                        if(discAPIJsonObject.getDMSNO().contentEquals("")) {
                            DiscAPIResponseJsonObject discAPIResponseJsonObject = new Gson().fromJson(response, DiscAPIResponseJsonObject.class);
                            if (discAPIResponseJsonObject.getCODE() == 1) {
                                for (DiscAPIDataResponseJsonObject data : discAPIResponseJsonObject.getDATA()) {
                                    if (!data.getDMSPCT().contentEquals("")) {
                                        try {
                                            String[] dmspcts = data.getDMSPCT().split("\\|");

                                            if (!data.getPCODE().contentEquals("")) {
                                                int bonusQty = data.getBONUS_QTY();
                                                int multiple = 0;
                                                int minQty = data.getMIN_QTY();
                                                int maxQty = data.getMAX_QTY();

                                                try {
                                                    multiple = Integer.parseInt(data.getMULTIPLE());
                                                } catch (Exception e) {
                                                    multiple = 0;
                                                }

                                                int qty = getQty(data.getITEM());
                                                if (qty >= minQty) {
                                                    int bonus = 0;

                                                    if (qty > maxQty && maxQty > 0) {
                                                        bonus = (maxQty / multiple) * bonusQty;
                                                    } else {
                                                        bonus = (qty / multiple) * bonusQty;
                                                    }

                                                    if (bonus > 0) {
                                                        discAPICallback.onFinishCalculate(dmspcts[0].split("#")[0], DoubleHelper.parseDouble(dmspcts[0].split("#")[1]), bonusQty, data.getITEM(), DiscountAPIResultEnum.MIX);
                                                    } else {
                                                        discAPICallback.onFinishCalculate(dmspcts[0].split("#")[0], DoubleHelper.parseDouble(dmspcts[0].split("#")[1]), 0, data.getITEM(),  DiscountAPIResultEnum.DISCOUNT);
                                                    }
                                                } else {
                                                    discAPICallback.onFinishCalculate(dmspcts[0].split("#")[0], DoubleHelper.parseDouble(dmspcts[0].split("#")[1]), 0, data.getITEM(), DiscountAPIResultEnum.DISCOUNT);
                                                }
                                            } else {
                                                discAPICallback.onFinishCalculate(dmspcts[0].split("#")[0], DoubleHelper.parseDouble(dmspcts[0].split("#")[1]), 0, data.getITEM(), DiscountAPIResultEnum.DISCOUNT);
                                            }
                                        } catch (Exception e) {
                                            activity.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    new MaterialDialog.Builder(activity)
                                                            .title("Information")
                                                            .content("Error parsing data, can't get discount.")
                                                            .cancelable(false)
                                                            .positiveText("Ok")
                                                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                                @Override
                                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                                                }
                                                            })
                                                            .show();
                                                }
                                            });
                                        }
                                    } else {
                                        continue;
                                    }
                                }
                            } else if (discAPIResponseJsonObject.getCODE() == 0) {
                                activity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        new MaterialDialog.Builder(activity)
                                                .title("Information")
                                                .content("You are not connected. Please check your internet connection.")
                                                .cancelable(false)
                                                .positiveText("Ok")
                                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                    @Override
                                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                                    }
                                                })
                                                .show();
                                    }
                                });
                            }
                        } else {
                            DiscAPIHeaderResponseJsonObject discAPIHeaderResponseJsonObject = new Gson().fromJson(response, DiscAPIHeaderResponseJsonObject.class);
                            if (discAPIHeaderResponseJsonObject.getCODE() == 1) {
                                for (DiscAPIHeaderDataResponseJsonObject data : discAPIHeaderResponseJsonObject.getDATA()) {
                                    if (data.getDISCOUNT() == 100) {
                                        discAPICallback.onFinishCalculate(data.getDMSNO(), 0, data.getQTY(), data.getPCODE(), DiscountAPIResultEnum.FREEGOOD);
                                    } else {
                                        discAPICallback.onFinishCalculate(data.getDMSNO(), data.getDISCOUNT(), 0, data.getPCODE(), DiscountAPIResultEnum.DISCOUNT);
                                    }
                                }
                            } else if (discAPIHeaderResponseJsonObject.getCODE() == 0) {
                                activity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        new MaterialDialog.Builder(activity)
                                                .title("Information")
                                                .content("You are not connected. Please check your internet connection.")
                                                .cancelable(false)
                                                .positiveText("Ok")
                                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                    @Override
                                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                                    }
                                                })
                                                .show();
                                    }
                                });
                            }
                        }
                    } catch (Exception e) {
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                new MaterialDialog.Builder(activity)
                                        .title("Information")
                                        .content("You are not connected. Please check your internet connection.")
                                        .cancelable(false)
                                        .positiveText("Ok")
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                            }
                                        })
                                        .show();
                            }
                        });

                    }
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss();
                            isCalculate = false;
                        }
                    });

                }
            });
        } catch (Exception e) {
            Log.e("CalculateDisc-Error", e.toString());
            dialog.dismiss();
        }
    }

    private int getQty(String item) {
        int result = 0;
        for(DiscAPIOrderJsonObject discAPIOrderJsonObject : discAPIJsonObject.getORDER()) {
            if (discAPIOrderJsonObject.getPCODE().contentEquals(item)) {
                result = discAPIOrderJsonObject.getQTY();
                break;
            }
        }
        return result;
    }
}
