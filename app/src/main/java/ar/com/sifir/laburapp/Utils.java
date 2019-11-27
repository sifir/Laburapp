package ar.com.sifir.laburapp;

/**
 * Created by sifir on 11/27/17.
 */

public class Utils {

    public static String formatPassValue(byte[] values) {
        String ss ="" ;
        for (int i = 0 ; i < 4 ; i++) {
            String s ="";
            if (i == 3){
                s = s + Integer.toHexString(values[i] + 128);
            } else {
                s = s + Integer.toHexString(values[i] + 128) + "-";
            }
            if (s.length() == 1) s = "0" + s;
            ss += s;
        }
        return ss.toUpperCase();
    }
}
