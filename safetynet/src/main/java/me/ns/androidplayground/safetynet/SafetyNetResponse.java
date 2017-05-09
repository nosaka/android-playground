package me.ns.androidplayground.safetynet;

import android.util.Base64;

import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by shintaro.nosaka on 2017/05/09.
 * SafetyNetResponse
 */
public class SafetyNetResponse {

    /**
     * {@link Gson}
     */
    private static Gson sGson;

    @SerializedName("nonce")
    String nonce;

    @SerializedName("timestampMs")
    Long timestampMs;

    @SerializedName("apkPackageName")
    String apkPackageName;

    @SerializedName("apkCertificateDigestSha256")
    String[] apkCertificateDigestSha256;

    @SerializedName("apkDigestSha256")
    String apkDigestSha256;

    @SerializedName("ctsProfileMatch")
    Boolean ctsProfileMatch;

    @SerializedName("basicIntegrity")
    Boolean basicIntegrity;

    @Override
    public String toString() {
        return "nonce:=" + nonce
                + "timestampMs:=" + new Date(timestampMs) + "\n"
                + "apkPackageName:=" + apkPackageName + "\n"
                + "apkCertificateDigestSha256:=" + apkCertificateDigestSha256 + "\n"
                + "apkDigestSha256:=" + apkDigestSha256 + "\n"
                + "ctsProfileMatch:=" + ctsProfileMatch + "\n"
                + "basicIntegrity:=" + basicIntegrity;
    }

    /**
     * {@link SafetyNetResponse}変換処理
     *
     * @param jwsResult {@link SafetyNetApi.AttestationResult#getJwsResult()}
     * @return {@link SafetyNetResponse}
     */
    public static SafetyNetResponse from(String jwsResult) {
        if (jwsResult == null || jwsResult.length() <= 0) {
            return null;
        }
        final String[] jwtParts = jwsResult.split("\\.");
        if (jwtParts.length == 3) {
            String decodedPayload = new String(Base64.decode(jwtParts[1], Base64.DEFAULT));
            return newGson().fromJson(decodedPayload, SafetyNetResponse.class);
        }
        return null;
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
