package me.pete.ocarinalibrary.dialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import me.pete.ocarinalibrary.enumerator.DialogTypeEnum;
import me.pete.ocarinalibrary.object.PinLocationObject;

public class MaterialDialog extends BaseDialog {
    private DialogTypeEnum dialogTypeEnum;
    private me.pete.ocarinalibrary.listener.OnClickListener onPositiveClicked, onNegativeClicked, onNeutralClicked;
    private String neutralText, negativeText, positiveText, titleText, messageText;

    public MaterialDialog(@NonNull AppCompatActivity activity) {
        super(activity);
    }

    public MaterialDialog cancelable(boolean cancelable) {
        setCancelable(cancelable);
        setCanceledOnTouchOutside(cancelable);
        return this;
    }

    public MaterialDialog setViewType(DialogTypeEnum dialogTypeEnum) {
        this.dialogTypeEnum = dialogTypeEnum;
        return this;
    }

    public MaterialDialog setPositiveText(String text) {
        this.positiveText = text;
        return this;
    }

    public MaterialDialog setNegativeText(String text) {
        this.negativeText = text;
        return this;
    }

    public MaterialDialog setNeutralText(String text) {
        this.neutralText = text;
        return this;
    }

    public MaterialDialog setTitle(String titleText) {
        this.titleText = titleText;
        return this;
    }

    public MaterialDialog setMessage(String messageText) {
        this.messageText = messageText;
        return this;
    }

    public MaterialDialog setOnPositiveClicked(me.pete.ocarinalibrary.listener.OnClickListener onPositiveClicked) {
        this.onPositiveClicked = onPositiveClicked;
        return this;
    }

    public MaterialDialog setOnNegativeClicked(me.pete.ocarinalibrary.listener.OnClickListener onNegativeClicked) {
        this.onNegativeClicked = onNegativeClicked;
        return this;
    }

    public MaterialDialog setOnNeutralClicked(me.pete.ocarinalibrary.listener.OnClickListener onNeutralClicked) {
        this.onNeutralClicked = onNeutralClicked;
        return this;
    }

    public MaterialDialog setPinLocation(PinLocationObject... pinLocations) {
        super.setPinLocationObjects(pinLocations);
        return this;
    }

    @Override
    public void show() {
        setView(dialogTypeEnum);
        setConfiguration(titleText, messageText, positiveText, negativeText, neutralText,
                onPositiveClicked, onNegativeClicked, onNeutralClicked);
        super.show();
    }
}
