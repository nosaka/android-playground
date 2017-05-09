package me.ns.androidplayground.nearbymessages;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.UUID;

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

    @SerializedName("id")
    UUID id;

    @SerializedName("user_name")
    String userName;

    @SerializedName("message")
    String message;

    @SerializedName("timestamp")
    Long timestamp;

    /**
     * コンストラクタ
     *
     * @param userName ユーザ名
     * @param message  メッセージ
     */
    public MessageContent(String userName, String message) {
        this.id = UUID.randomUUID();
        this.userName = userName;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
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
