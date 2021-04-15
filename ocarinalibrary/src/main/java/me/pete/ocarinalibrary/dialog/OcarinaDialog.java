package me.pete.ocarinalibrary.dialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import me.pete.ocarinalibrary.enumerator.DialogTypeEnum;
import me.pete.ocarinalibrary.object.PinLocationObject;

public class OcarinaDialog extends BaseDialog {
    private DialogTypeEnum dialogTypeEnum;
    private me.pete.ocarinalibrary.listener.OnClickListener onPositiveClicked, onNegativeClicked, onNeutralClicked;
    private String neutralText, negativeText, positiveText, titleText, messageText;

    public OcarinaDialog(@NonNull AppCompatActivity activity) {
        super(activity);
    }

    public OcarinaDialog cancelable(boolean cancelable) {
        setCancelable(cancelable);
        setCanceledOnTouchOutside(cancelable);
        return this;
    }

    public OcarinaDialog setViewType(DialogTypeEnum dialogTypeEnum) {
        this.dialogTypeEnum = dialogTypeEnum;
        return this;
    }

    public OcarinaDialog setPositiveText(String text) {
        this.positiveText = text;
        return this;
    }

    public OcarinaDialog setNegativeText(String text) {
        this.negativeText = text;
        return this;
    }

    public OcarinaDialog setNeutralText(String text) {
        this.neutralText = text;
        return this;
    }

    public OcarinaDialog setTitle(String titleText) {
        this.titleText = titleText;
        return this;
    }

    public OcarinaDialog setMessage(String messageText) {
        this.messageText = messageText;
        return this;
    }

    public OcarinaDialog setOnPositiveClicked(me.pete.ocarinalibrary.listener.OnClickListener onPositiveClicked) {
        this.onPositiveClicked = onPositiveClicked;
        return this;
    }

    public OcarinaDialog setOnNegativeClicked(me.pete.ocarinalibrary.listener.OnClickListener onNegativeClicked) {
        this.onNegativeClicked = onNegativeClicked;
        return this;
    }

    public OcarinaDialog setOnNeutralClicked(me.pete.ocarinalibrary.listener.OnClickListener onNeutralClicked) {
        this.onNeutralClicked = onNeutralClicked;
        return this;
    }

    public OcarinaDialog setPinLocation(PinLocationObject... pinLocations) {
        super.setPinLocationObjects(pinLocations);
        return this;
    }

    public OcarinaDialog setUsePolygonForMapDialog(boolean usePolygon) {
        super.setUsePolygon(usePolygon);
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
