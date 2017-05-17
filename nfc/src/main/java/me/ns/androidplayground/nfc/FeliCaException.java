package me.ns.androidplayground.nfc;

import java.util.Locale;

/**
 * FeliCa例外
 * <p>
 * Created by shintaro.nosaka on 2017/05/16.
 */
public class FeliCaException extends Exception {

    /**
     * コンストラクタ
     *
     * @param code エラーコード
     */
    public FeliCaException(int code) {
        super(message(code));
    }

    /**
     * メッセージ
     *
     * @param code エラーコード
     * @return メッセージ
     */
    private static String message(int code) {
        return String.format(Locale.getDefault(), "FeliCa Error [%1d$]", code);
    }
}
