package me.ns.lib;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * アラートUtility
 * Created by shintaro.nosaka on 2017/05/09.
 */
public final class AlertUtil {

    /**
     * コンストラクタ
     */
    private AlertUtil() {
        // 生成禁止
    }

    /**
     * アラートダイアログ表示処理
     *
     * @param context {@link Context}
     * @param message メッセージ
     */
    public static void showAlert(Context context, String message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .create()
                .show();
    }

    /**
     * アラートダイアログ表示処理
     *
     * @param context {@link Context}
     * @param title   タイトル
     * @param message メッセージ
     */
    public static void showAlert(Context context, String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .create()
                .show();
    }

    /**
     * アラートダイアログ表示処理
     *
     * @param context    {@link Context}
     * @param message    メッセージ
     * @param okListener OK押下リスナ
     */
    public static void showAlert(Context context, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, okListener)
                .create()
                .show();
    }

    /**
     * アラートダイアログ表示処理
     *
     * @param context    {@link Context}
     * @param title      タイトル
     * @param message    メッセージ
     * @param okListener OK押下リスナ
     */
    public static void showAlert(Context context, String title, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, okListener)
                .create()
                .show();
    }

    /**
     * 確認ダイアログ表示処理
     *
     * @param context        {@link Context}
     * @param message        メッセージ
     * @param okListener     OK押下リスナ
     * @param cancelListener キャンセル押下リスナ
     */
    public static void showConfirm(Context context, String message, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, okListener)
                .setNegativeButton(android.R.string.cancel, cancelListener)
                .create()
                .show();
    }

    /**
     * 確認ダイアログ表示処理
     *
     * @param context        {@link Context}
     * @param title          タイトル
     * @param message        メッセージ
     * @param okListener     OK押下リスナ
     * @param cancelListener キャンセル押下リスナ
     */
    public static void showConfirm(Context context, String title, String message, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, okListener)
                .setNegativeButton(android.R.string.cancel, cancelListener)
                .create()
                .show();
    }


}
