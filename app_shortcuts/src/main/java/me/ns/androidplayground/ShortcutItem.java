package me.ns.androidplayground;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

/**
 * ショートカット要素
 * <p>
 * Created by shintaro.nosaka on 2017/05/02.
 */
public enum ShortcutItem {

    OPEN_GOOGLE("ID#1", "Googleを開く", "Googleを開く", android.R.drawable.ic_input_get),
    OPEN_MAIL("ID#2", "メールを送信", "メールを送信", android.R.drawable.ic_dialog_email),;

    String id;

    String shortLabel;

    String longLabel;

    @DrawableRes
    int icon;

    ShortcutItem(String id, String shortLabel, String longLabel, @DrawableRes int icon) {
        this.id = id;
        this.shortLabel = shortLabel;
        this.longLabel = longLabel;
        this.icon = icon;
    }

    @Override
    public String toString() {
        return shortLabel;
    }

    @NonNull
    public Intent getIntent() {
        switch (this) {
            case OPEN_GOOGLE:
                return new Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com"));
            case OPEN_MAIL:
                return new Intent(Intent.ACTION_SEND)
                        .setType("message/rfc822")
                        .putExtra(Intent.EXTRA_EMAIL, new String[]{"foo@example.com"})
                        .putExtra(Intent.EXTRA_SUBJECT, "件名")
                        .putExtra(Intent.EXTRA_TEXT, "本文");
            default:
                return new Intent();
        }
    }

    public static ShortcutItem[] items() {
        return new ShortcutItem[]{OPEN_GOOGLE, OPEN_MAIL};
    }
}
