package ar.com.sifir.laburapp.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

public abstract class FormEditText extends AppCompatEditText {

    public FormEditText(Context context) {
        super(context);
    }

    public FormEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FormEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public abstract boolean isValid();
    public abstract boolean isMailValid();
    public abstract boolean isPassValid();
}
