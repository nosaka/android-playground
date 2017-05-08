package me.ns.androidplayground.nearbymessages;

import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * メッセージコンテンツ
 * <p>
 * Created by shintaro.nosaka on 2017/05/08.
 */
public class MessageContent {

    /**
     * {@link Gson}
     */
    private static Gson sGson;

    @SerializedName("user_name")
    public String userName;

    @SerializedName("message")
    public String message;

    @SerializedName("timestamp")
    public Long timestamp;

    public MessageContent(String inMessage) {
        userName = Build.USER;
        message = inMessage;
        timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return newGson().toJson(this);
    }

    /**
     * {@link MessageContent}変換処理
     *
     * @param json JSON文字列
     * @return {@link MessageContent}
     */
    public static MessageContent from(String json) {
        return newGson().fromJson(json, MessageContent.class);
    }

    /**
     * {@link Gson}生成
     *
     * @return {@link Gson}
     */
    private static Gson newGson() {
        if (sGson == null) {
            sGson = new Gson();
        }
        return sGson;
    }
}
