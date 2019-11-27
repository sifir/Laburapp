package ar.com.sifir.laburapp.widget;

import android.content.Context;
import android.util.AttributeSet;

import ar.com.sifir.laburapp.R;

public class SimpleFormEditText extends FormEditText {

    public SimpleFormEditText(Context context) {
        super(context);
    }

    public SimpleFormEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleFormEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isValid() {
        if (getText().toString().isEmpty()) {
            setError("Este campo debe estar completo");
            //R.string.completeField
            return false;
        }
        if (!getText().toString().matches("[A-Za-z][^.]*")) {
            setError("Caracteres invalidos");
            //R.string.invalidChar
            return false;
        }
        return true;
    }

    @Override
    public boolean isMailValid(){
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if (!getText().toString().matches(regex)){
            setError("Email Invalido");
            //R.string.invalidEmail;
            return false;
        }
        return true;
    }

    @Override
    public boolean isPassValid(){
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        if (!getText().toString().matches(regex)){
            setError("El Password debe contener al menos 8 caracteres y al menos un numero y caracter especial");
            //R.string.passError
            return false;
        }
        return true;
    }
}
