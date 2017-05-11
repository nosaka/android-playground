package me.ns.androidplayground.nearbymessages;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.BleSignal;
import com.google.android.gms.nearby.messages.Distance;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeOptions;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.ns.lib.AlertUtil;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    // //////////////////////////////////////////////////////////////////////////
    // staticフィールド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * リクエストコード：エラー受信時
     */
    private static final int REQUEST_RESOLVE_ERROR = 0x001;

    /**
     * リクエストコード：パーミッション要求
     */
    private static final int REQUEST_PERMISSIONS = 0x002;


    /**
     * Bundleキー：ユーザー名
     */
    private static final String KEY_BUNDLE_USER_NAME = "user_name";

    // //////////////////////////////////////////////////////////////////////////
    // Bind UI
    // //////////////////////////////////////////////////////////////////////////

    @BindView(R.id.main_Container)
    ViewGroup mContainer;

    @BindView(R.id.main_MessageTextInputEditText)
    TextInputEditText mMessageTextInputEditText;

    @BindView(R.id.main_MessageListView)
    ListView mMessageListView;

    // //////////////////////////////////////////////////////////////////////////
    // インスタンスフィールド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * ユーザ名
     */
    private String mUserName;

    /**
     * {@link GoogleApiClient}
     */
    private GoogleApiClient mGoogleApiClient;

    /**
     * {@link MessageListener}
     */
    private MessageListener mMessageListener = new MessageListener() {

        @Override
        public void onFound(Message message) {
            if (!mMessageAdapter.isContains(message)) {
                mMessageAdapter.insert(message, 0);
                mMessageAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onLost(Message message) {
            if (!mMessageAdapter.isContains(message)) {
                mMessageAdapter.insert(message, 0);
                mMessageAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onDistanceChanged(Message message, Distance distance) {
            if (!mMessageAdapter.isContains(message)) {
                mMessageAdapter.insert(message, 0);
                mMessageAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onBleSignalChanged(Message message, BleSignal bleSignal) {
            if (!mMessageAdapter.isContains(message)) {
                mMessageAdapter.insert(message, 0);
                mMessageAdapter.notifyDataSetChanged();
            }
        }
    };

    /**
     * メッセージアダプタ
     */
    private MessageAdapter mMessageAdapter;

    // //////////////////////////////////////////////////////////////////////////
    // イベントメソッド
    // //////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if (getIntent() != null && getIntent().hasExtra(KEY_BUNDLE_USER_NAME)) {
            mUserName = getIntent().getStringExtra(KEY_BUNDLE_USER_NAME);
        }
        setTitle(mUserName);

        mMessageTextInputEditText.setText(null);
        mMessageAdapter = new MessageAdapter(this);
        mMessageListView.setAdapter(mMessageAdapter);

        mMessageTextInputEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    if (validation()) {
                        publish(mMessageTextInputEditText.getText());
                    }
                }
                return false;
            }
        });

        // GoogleApiClientをBuild
        buildGoogleApiClient();

        // 位置情報有効チェック
        if (checkLocationPermission()) {
            checkLocationEnable();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_PublishMessage:
                if (validation()) {
                    publish(mMessageTextInputEditText.getText());
                }
                if (getCurrentFocus() != null) {
                    InputMethodManager imm = getSystemService(InputMethodManager.class);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        unsubscribeMessages();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                boolean granted = true;
                for (int grantResult : grantResults) {
                    granted &= grantResult == PackageManager.PERMISSION_GRANTED;
                }
                if (granted) {
                    checkLocationEnable();
                } else {
                    AlertUtil.showAlert(this, "位置情報を許可しない場合はBLEを利用したNearby Messageは利用できません");
                }
        }
    }

    // //////////////////////////////////////////////////////////////////////////
    // interface実装メソッド
    // //////////////////////////////////////////////////////////////////////////

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Snackbar.make(mContainer, "接続しました", Snackbar.LENGTH_SHORT).show();
        subscribeMessages();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException ignored) {
                Snackbar.make(mContainer, "接続に失敗しました", Snackbar.LENGTH_SHORT).show();
            }
        } else {
            String message = String.format(Locale.getDefault(), "接続エラー\nErrorCode:=%d, %s", connectionResult.getErrorCode(), connectionResult.getErrorMessage());
            Snackbar.make(mContainer, message, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Snackbar.make(mContainer, "切断されました", Snackbar.LENGTH_SHORT).show();
        unsubscribeMessages();
    }

    // //////////////////////////////////////////////////////////////////////////
    // その他メソッド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * build GoogleApiClient
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Nearby.MESSAGES_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    /**
     * Messages Publish
     *
     * @param message メッセージ
     */
    private void publish(CharSequence message) {
        try {
            MessageContent content = new MessageContent(message.toString(), mUserName);
            Message activeMessage = new Message(content.toString().getBytes("UTF-8"));
            Nearby.Messages.publish(mGoogleApiClient, activeMessage);
        } catch (UnsupportedEncodingException e) {
            AlertUtil.showAlert(this, "メッセージの送信に失敗しました");
        }
    }

    /**
     * Messages Subscribe
     */
    private void subscribeMessages() {
        SubscribeOptions options = new SubscribeOptions.Builder()
                .setStrategy(Strategy.BLE_ONLY)
                .build();
        Nearby.Messages.subscribe(mGoogleApiClient, mMessageListener, options);
    }

    /**
     * Messages Unsubscribe
     */
    private void unsubscribeMessages() {
        if (mGoogleApiClient.isConnected()) {
            Nearby.Messages.unsubscribe(mGoogleApiClient, mMessageListener);
        }
    }

    /**
     * ヴァリデーション処理
     *
     * @return ヴァリデーション結果
     */
    private boolean validation() {
        if (mMessageTextInputEditText.getText() == null || mMessageTextInputEditText.getText().length() <= 0) {
            mMessageTextInputEditText.setError("メッセージを入力してください");
            return false;
        }
        mMessageTextInputEditText.setError(null);
        return true;
    }

    /**
     * 位置情報パーミッションチェック
     *
     * @return チェック結果
     */
    private boolean checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ((ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED))) {

            // パーミッション要求
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS);
            return false;
        }
        return true;
    }

    /**
     * 位置情報有効チェック
     */
    protected void checkLocationEnable() {
        LocationManager locationManager = getSystemService(LocationManager.class);
        boolean enable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!enable) {
            AlertUtil.showConfirm(this, "位置情報を有効にしますか", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            }, null);
        }
    }

    /**
     * {@link Intent}生成
     *
     * @param context {@link Context}
     * @return {@link Intent}
     */
    public static Intent newIntent(Context context, String userName) {
        return new Intent(context, MainActivity.class).putExtra(KEY_BUNDLE_USER_NAME, userName);
    }

}
