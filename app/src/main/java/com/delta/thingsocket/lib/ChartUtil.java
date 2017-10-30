package com.delta.thingsocket.lib;

import java.io.UnsupportedEncodingException;

/**
 * description :
 *
 * @author :  Wenju.Tian
 * @version date : 2017/10/19 16:32
 */


public class ChartUtil {
    final ChartUtil self = this;

    public static final String UTF_8 = "UTF-8";

    /* Constructors */


    /* Public Methods */
    public static byte[] stringToData(String string, String charsetName) {
        if (string != null) {
            try {
                return string.getBytes(charsetName);
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String dataToString(byte[] data, String charsetName) {
        if (data != null) {
            try {
                return new String(data, charsetName);
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
