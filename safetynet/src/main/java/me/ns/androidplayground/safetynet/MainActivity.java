package me.ns.androidplayground.safetynet;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.ns.lib.AlertUtil;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    // //////////////////////////////////////////////////////////////////////////
    // staticフィールド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * メニューID：セーフティネットの要求
     */
    private static final int MENU_REQUEST_SAFETY_NET = 0x001;

    /**
     * 日付フォーマット
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());


    // //////////////////////////////////////////////////////////////////////////
    // Bind UI
    // //////////////////////////////////////////////////////////////////////////

    @BindView(R.id.main_NonceTextView)
    TextView mNonceTextView;

    @BindView(R.id.main_TimestampMsTextView)
    TextView mTimestampMsTextView;

    @BindView(R.id.main_ApkPackageNameTextView)
    TextView mApkPackageNameTextView;

    @BindView(R.id.main_ApkCertificateDigestSha256TextView)
    TextView mApkCertificateDigestSha256TextView;

    @BindView(R.id.main_ApkDigestSha256TextView)
    TextView mApkDigestSha256TextView;

    @BindView(R.id.main_CtsProfileMatchTextView)
    TextView mCtsProfileMatchTextView;

    @BindView(R.id.main_BasicIntegrityTextView)
    TextView mBasicIntegrityTextView;

    // //////////////////////////////////////////////////////////////////////////
    // インスタンスフィールド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * {@link GoogleApiClient}
     */
    private GoogleApiClient mGoogleApiClient;

    /**
     * {@link SecureRandom}
     */
    private final Random mRandom = new SecureRandom();

    /**
     * {@link ProgressDialog}
     */
    private ProgressDialog mProgressDialog;

    // //////////////////////////////////////////////////////////////////////////
    // イベントメソッド
    // //////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mProgressDialog = new ProgressDialog(this);

        // GoogleApiClientをBuild
        buildGoogleApiClient();

        // SafetyNetリクエストの送信
        sendSafetyNetRequest();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_REQUEST_SAFETY_NET, Menu.NONE, "SafetyNetResponse");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_REQUEST_SAFETY_NET:
                // SafetyNetリクエストの送信
                sendSafetyNetRequest();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // //////////////////////////////////////////////////////////////////////////
    // interface実装メソッド
    // //////////////////////////////////////////////////////////////////////////

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        String message = String.format(Locale.getDefault(), "接続エラー\nErrorCode:=%d, %s", connectionResult.getErrorCode(), connectionResult.getErrorMessage());
        AlertUtil.showAlert(this, message);
    }

    // //////////////////////////////////////////////////////////////////////////
    // その他メソッド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * build GoogleApiClient
     */
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
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.setMessage("接続中");
            mProgressDialog.show();
        }
        final byte[] nonce = generateRequestNonce(); // Should be at least 16 bytes in length.
        SafetyNet.SafetyNetApi.attest(mGoogleApiClient, nonce)
                .setResultCallback(new ResultCallback<SafetyNetApi.AttestationResult>() {
                    @Override
                    public void onResult(@NonNull SafetyNetApi.AttestationResult result) {

                        mProgressDialog.dismiss();

                        if (result.getStatus().isSuccess()) {
                            if (result.getJwsResult() == null) {
                                return;
                            }
                            SafetyNetResponse response = SafetyNetResponse.from(result.getJwsResult());
                            if (response != null) {
                                mNonceTextView.setText(response.nonce);
                                mTimestampMsTextView.setText(DATE_FORMAT.format(new Date(response.timestampMs)));
                                mApkPackageNameTextView.setText(response.apkPackageName);
                                StringBuilder builder = new StringBuilder();
                                for (String item : response.apkCertificateDigestSha256) {
                                    builder.append(item);
                                    builder.append("\n");
                                }
                                mApkCertificateDigestSha256TextView.setText(builder.toString());
                                mApkDigestSha256TextView.setText(response.apkDigestSha256);
                                mCtsProfileMatchTextView.setText(Boolean.toString(response.ctsProfileMatch));
                                mBasicIntegrityTextView.setText(Boolean.toString(response.basicIntegrity));
                            }
                        } else {
                            String message = String.format(Locale.getDefault(), "SafetyNetエラー\nErrorCode:=%d, %s", result.getStatus().getStatusCode(), result.getStatus().getStatusMessage());
                            AlertUtil.showAlert(MainActivity.this, message);
                        }
                    }
                });
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
