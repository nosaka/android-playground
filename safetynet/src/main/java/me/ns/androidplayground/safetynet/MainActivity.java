package me.ns.androidplayground.safetynet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolver;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    /**
     * {@link GoogleApiClient}
     */
    private GoogleApiClient mGoogleApiClient;

    /**
     * {@link SecureRandom}
     */
    private final Random mRandom = new SecureRandom();

    /**
     * メニューID：ショートカット追加
     */
    private static final int MENU_ADD_SAFETY_NET = 0x001;

    @BindView(R.id.main_MessageTextView)
    TextView mMessageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mMessageTextView.setText(null);

        // GoogleApiClientをBuild
        buildGoogleApiClient();

        // SafetyNetリクエストの送信
        sendSafetyNetRequest();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ADD_SAFETY_NET, Menu.NONE, "SafetyNet");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ADD_SAFETY_NET:
                // SafetyNetリクエストの送信
                sendSafetyNetRequest();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        String message = String.format(Locale.getDefault(),
                "接続エラー\nErrorCode:=%d, %s",
                connectionResult.getErrorCode(), connectionResult.getErrorMessage());
        mMessageTextView.setText(message);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(SafetyNet.API)
                .enableAutoManage(this, this)
                .build();
    }

    /**
     * SafetyNetリクエストの送信
     */
    private void sendSafetyNetRequest() {
        mMessageTextView.setText("接続中");
        final byte[] nonce = generateRequestNonce(); // Should be at least 16 bytes in length.
        SafetyNet.SafetyNetApi.attest(mGoogleApiClient, nonce)
                .setResultCallback(new ResultCallback<SafetyNetApi.AttestationResult>() {

                    @Override
                    public void onResult(@NonNull SafetyNetApi.AttestationResult result) {
                        if (result.getStatus().isSuccess()) {
                            if (result.getJwsResult() == null) {
                                return;
                            }
                            final String[] jwtParts = result.getJwsResult().split("\\.");
                            if (jwtParts.length == 3) {
                                String decodedPayload = new String(Base64.decode(jwtParts[1], Base64.DEFAULT));
                                mMessageTextView.setText(decodedPayload);
                            }
                        } else {
                            String message = String.format(Locale.getDefault(),
                                    "SafetyNetエラー\nErrorCode:=%d, %s",
                                    result.getStatus().getStatusCode(), result.getStatus().getStatusMessage());
                            mMessageTextView.setText(message);
                        }
                    }
                });
    }

    private static class MySigningKeyResolver implements SigningKeyResolver {

        @Override
        public Key resolveSigningKey(JwsHeader header, Claims claims) {
            return null;
        }

        @Override
        public Key resolveSigningKey(JwsHeader header, String plaintext) {
            return null;
        }
    }

    /**
     * リクエストNonceデータの生成
     */
    private byte[] generateRequestNonce() {
        String nonceData = Long.toBinaryString(System.currentTimeMillis());
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[24];
        mRandom.nextBytes(bytes);
        try {
            byteStream.write(bytes);
            byteStream.write(nonceData.getBytes());
        } catch (IOException e) {
            return null;
        }

        return byteStream.toByteArray();
    }


}
