package me.pete.ocarinalibrary.helper;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import java.text.NumberFormat;
import java.util.Locale;

import me.pete.ocarinalibrary.enumerator.OnTextWatcherListener;

public final class EditTextHelper {
    /**
     * This function is setup max length characters your EditText.
     *
     * @param editText      Your EditText
     * @param maxLength     Length of characters
     */
    public static void onSetMaxLenght(EditText editText, int maxLength) {
        editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxLength) });
    }

    /**
     * This function is handling next focus EditText if current EditText
     * has reach the max length of characters then will focus to next EditText
     * and if current EditText is empty string then will focus to before EditText.
     *
     * @param editText          Your current EditText.
     * @param editTextPrev      Your before EditText.
     * @param editTextNext      Your next EditText.
     * @param maxLength         Max length your current EditText.
     */
    public static void onSetPrevAndNextRequestFocus(final EditText editText, final EditText editTextPrev, final EditText editTextNext, final int maxLength) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("LongLogTag")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("onSetPrevAndNextRequestFocus", "Masuik sini");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() == 0 && s.toString().contentEquals("")) {
                    if(editTextPrev != null) {
                        editTextPrev.requestFocus();
                        editTextPrev.setSelection(editTextPrev.getText().toString().length());
                    }
                } else if(s.toString().length() > maxLength && !s.toString().contentEquals("")) {
                    if(editTextNext != null) {
                        editTextNext.requestFocus();
                        editText.setText(s.toString().substring(0, maxLength));
                        editTextNext.setText(s.toString().substring(maxLength, maxLength+1));
                        editTextNext.setSelection(editTextNext.getText().toString().length());
                    }
                }
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    if(editTextNext != null && editTextNext.getText().toString().length() > 0) {
                        editTextNext.requestFocus();
                        editTextNext.setSelection(editTextNext.getText().toString().length());
                    } else {
                        editText.setSelection(editText.getText().toString().length());
                    }
                }
            }
        });

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setSelection(editText.getText().toString().length());
            }
        });

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    if(editText.getText().toString().contentEquals("")) {
                        editTextPrev.requestFocus();
                        editTextPrev.setSelection(editTextPrev.getText().toString().length());
                    }
                }
                return false;
            }
        });
    }

    /**
     * This function return TextWatcher of Currency to EditText.
     *
     * @param editText      Your EditText
     * @return
     */
    public static TextWatcher textWatcherOnCurrency(final EditText editText) {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            String current = "";
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("EditTextHelper", s.toString());
                if(!s.toString().equals(current)){
                    editText.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");

                    double parsed = 0;
                    try {
                        parsed = Double.parseDouble(cleanString);
                    } catch (Exception e) {
                        parsed = 0;
                    }

                    String formatted = NumberFormat.getCurrencyInstance(Locale.US).format((parsed)).replace("$", "").replace(".00", "");

                    current = formatted;
                    editText.setText(formatted);
                    editText.setSelection(formatted.length());

                    editText.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        return textWatcher;
    }

    /**
     * This function return TextWatcher of Currency to EditText.
     *
     * @param editText                  Your EditText.
     * @param onTextWatcherListener     Handling for TextWatcher to custom after do.
     * @return
     */
    public static TextWatcher textWatcherOnCurrency(final EditText editText, final OnTextWatcherListener onTextWatcherListener) {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                onTextWatcherListener.beforeTextChanged();
            }

            String current = "";
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(current)){
                    editText.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");

                    double parsed = 0;
                    try {
                        parsed = Double.parseDouble(cleanString);
                    } catch (Exception e) {
                        parsed = 0;
                    }

                    String formatted = NumberFormat.getCurrencyInstance(Locale.US).format((parsed)).replace("$", "").replace(".00", "");

                    current = formatted;
                    editText.setText(formatted);
                    editText.setSelection(formatted.length());

                    editText.addTextChangedListener(this);
                    onTextWatcherListener.onTextChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                onTextWatcherListener.afterTextChanged();
            }
        };
        return textWatcher;
    }

    /**
     * This function return TextWatcher of Currency with cent style to EditText.
     *
     * @param editText      Your EditText
     * @return
     */
    public static TextWatcher textWatcherOnCurrencyDecimal(final EditText editText) {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            String current = "";
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("EditTextHelper", s.toString());
                if(!s.toString().equals(current)){
                    editText.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,]", "");

                    String decimal = "";
                    if(s.toString().contains(".")) {
                        try {
                            int cent = Integer.parseInt(cleanString.split("\\.")[1]);
                            if (cent >= 0 && !cleanString.split("\\.")[1].contentEquals("")) {
                                String newCent = "";
                                for(int i = 0; i < cleanString.split("\\.")[1].length(); i++) {
                                    if(i > 3) {
                                        break;
                                    } else {
                                        newCent += cleanString.split("\\.")[1].substring(i, i+1);
                                    }
                                }
                                decimal = "." + newCent;
                            } else if (s.toString().endsWith(".")) {
                                decimal = ".";
                            }
                        } catch (Exception e) {
                            decimal = ".";
                        }
                    }

                    String[] currencies = CurrencyHelper.doubleToCurrency(DoubleHelper.parseDouble(cleanString.split("\\.")[0])).split("\\.");
                    editText.setText(currencies[0] + decimal);
                    editText.setSelection(editText.getText().toString().length());

                    editText.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        return textWatcher;
    }

    /**
     * This function return TextWatcher of Currency style to EditText.
     *
     * @param editText      Your EditText
     * @param max           Max value acceptable. If value more than max then EditTex
     *                      will show max value.
     * @return
     */
    public static TextWatcher textWatcherOnCurrencyDecimal(final EditText editText, final double max) {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            String current = "";
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("EditTextHelper", s.toString());
                if(!s.toString().equals(current)){
                    editText.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,]", "");

                    String decimal = "";
                    if(s.toString().contains(".")) {
                        try {
                            int cent = Integer.parseInt(cleanString.split("\\.")[1]);
                            if (cent >= 0 && !cleanString.split("\\.")[1].contentEquals("")) {
                                String newCent = "";
                                for(int i = 0; i < cleanString.split("\\.")[1].length(); i++) {
                                    if(i > 3) {
                                        break;
                                    } else {
                                        newCent += cleanString.split("\\.")[1].substring(i, i+1);
                                    }
                                }
                                decimal = "." + newCent;
                            } else if (s.toString().endsWith(".")) {
                                decimal = ".";
                            }
                        } catch (Exception e) {
                            decimal = ".";
                        }
                    }

                    String[] currencies = CurrencyHelper.doubleToCurrency(DoubleHelper.parseDouble(cleanString.split("\\.")[0])).split("\\.");
                    if(CurrencyHelper.currencyToDouble(currencies[0] + decimal) > max) {
                        editText.setText(CurrencyHelper.doubleToCurrency(max));
                    } else {
                        editText.setText(currencies[0] + decimal);
                    }
                    editText.setSelection(editText.getText().toString().length());

                    editText.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        return textWatcher;
    }

    /**
     * This function return TextWatcher of Currency to EditText.
     *
     * @param editText                      Your EditText
     * @param onTextWatcherListener         Handling for TextWatcher to custom after do.
     * @return
     */
    public static TextWatcher textWatcherOnCurrencyDecimal(final EditText editText, final OnTextWatcherListener onTextWatcherListener) {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                onTextWatcherListener.beforeTextChanged();
            }

            String current = "";
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(current)){
                    editText.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,]", "");

                    double parsed = 0;
                    try {
                        parsed = Double.parseDouble(cleanString);
                    } catch (Exception e) {
                        parsed = 0;
                    }

                    String decimal = "";
                    if(s.toString().contains(".")) {
                        try {
                            int cent = Integer.parseInt(cleanString.split("\\.")[1]);
                            if (cent >= 0 && !cleanString.split("\\.")[1].contentEquals("")) {
                                if(cleanString.split("\\.")[1].length() > 1) {
                                    decimal = "." + cleanString.split("\\.")[1].substring(0, 2);
                                } else {
                                    decimal = "." + cleanString.split("\\.")[1];
                                }
                            } else if (s.toString().endsWith(".")) {
                                decimal = ".";
                            }
                        } catch (Exception e) {
                            decimal = ".";
                        }
                    }

                    String formatted = NumberFormat.getCurrencyInstance(Locale.US).format((parsed)).replace("$", "").replace(".00", "");
                    formatted = formatted.split("\\.")[0];
                    current = formatted;
                    editText.setText(formatted + decimal);
                    editText.setSelection(editText.getText().toString().length());

                    editText.addTextChangedListener(this);
                    onTextWatcherListener.onTextChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                onTextWatcherListener.afterTextChanged();
            }
        };
        return textWatcher;
    }

    /**
     * This function return TextWatcher of Currency with cent style to EditText.
     *
     * @param editText                      Your EditText
     * @param max                           Max value acceptable. If value more than max then EditTex
     *                                      will show max value.
     * @param onTextWatcherListener         Handling for TextWatcher to custom after do.
     * @return
     */
    public static TextWatcher textWatcherOnCurrencyDecimal(final EditText editText, final double max, final OnTextWatcherListener onTextWatcherListener) {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                onTextWatcherListener.beforeTextChanged();
            }

            String current = "";
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(current)){
                    editText.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,]", "");

                    double parsed = 0;
                    try {
                        parsed = Double.parseDouble(cleanString);
                    } catch (Exception e) {
                        parsed = 0;
                    }

                    String decimal = "";
                    if(s.toString().contains(".")) {
                        try {
                            int cent = Integer.parseInt(cleanString.split("\\.")[1]);
                            if (cent >= 0) {
                                if(cleanString.split("\\.")[1].length() > 4) {
                                    decimal = "." + cleanString.split("\\.")[1].substring(0, 4);
                                } else {
                                    decimal = "." + cleanString.split("\\.")[1];
                                }
                            } else if (s.toString().endsWith(".")) {
                                decimal = ".";
                            }
                        } catch (Exception e) {
                            decimal = ".";
                        }
                    }

                    String[] currencies = CurrencyHelper.doubleToCurrency(DoubleHelper.parseDouble(cleanString.split("\\.")[0])).split("\\.");
                    if(CurrencyHelper.currencyToDouble(currencies[0] + decimal) > max) {
                        editText.setText(CurrencyHelper.doubleToCurrency(max));
                    } else {
                        editText.setText(currencies[0] + decimal);
                    }
                    editText.setSelection(editText.getText().toString().length());

                    editText.addTextChangedListener(this);
                    onTextWatcherListener.onTextChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                onTextWatcherListener.afterTextChanged();
            }
        };
        return textWatcher;
    }
}
