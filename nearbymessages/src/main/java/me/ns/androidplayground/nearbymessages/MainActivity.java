package me.ns.androidplayground.nearbymessages;

import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.BleSignal;
import com.google.android.gms.nearby.messages.Distance;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    // //////////////////////////////////////////////////////////////////////////
    // staticフィールド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * リクエストコード：エラー受信時
     */
    private static final int REQUEST_RESOLVE_ERROR = 1001;

    // //////////////////////////////////////////////////////////////////////////
    // Bind UI
    // //////////////////////////////////////////////////////////////////////////

    @BindView(R.id.main_Container)
    ViewGroup mContainer;

    @BindView(R.id.main_MessageEditText)
    EditText mMessageEditText;

    @BindView(R.id.main_MessageListView)
    ListView mMessageListView;

    // //////////////////////////////////////////////////////////////////////////
    // インスタンスフィールド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * {@link GoogleApiClient}
     */
    private GoogleApiClient mGoogleApiClient;

    /**
     * Active {@link Message}
     */
    private Message mActiveMessage;

    /**
     * {@link MessageListener}
     */
    private MessageListener mMessageListener = new MessageListener() {

        @Override
        public void onFound(Message message) {
            mMessageAdapter.insert(message, 0);
            mMessageAdapter.notifyDataSetChanged();
        }

        @Override
        public void onLost(Message message) {
            mMessageAdapter.insert(message, 0);
            mMessageAdapter.notifyDataSetChanged();
        }

        @Override
        public void onDistanceChanged(Message message, Distance distance) {
            mMessageAdapter.insert(message, 0);
            mMessageAdapter.notifyDataSetChanged();
        }

        @Override
        public void onBleSignalChanged(Message message, BleSignal bleSignal) {
            mMessageAdapter.insert(message, 0);
            mMessageAdapter.notifyDataSetChanged();
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

        mMessageEditText.setText(null);
        mMessageAdapter = new MessageAdapter(this);
        mMessageListView.setAdapter(mMessageAdapter);

        mMessageEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    publish(mMessageEditText.getText());
                }
                return false;
            }
        });
        mMessageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Message item = mMessageAdapter.getItem(position);
                if (item != null) {
                    unsubscribeMessages();
                }

            }
        });

        // GoogleApiClientをBuild
        buildGoogleApiClient();
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
                publish(mMessageEditText.getText());
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

    // //////////////////////////////////////////////////////////////////////////
    // interface実装メソッド
    // //////////////////////////////////////////////////////////////////////////

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException ignored) {
            }
        } else {
            String message = String.format(Locale.getDefault(),
                    "接続エラー\nErrorCode:=%d, %s",
                    connectionResult.getErrorCode(), connectionResult.getErrorMessage());
            Snackbar.make(mContainer, message, Snackbar.LENGTH_LONG).show();
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Snackbar.make(mContainer, "接続しました", Snackbar.LENGTH_SHORT).show();
        subscribeMessages();
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
        mActiveMessage = new Message(message.toString().getBytes());
        Nearby.Messages.publish(mGoogleApiClient, mActiveMessage);
    }

    /**
     * Messages Unpublish
     */
    private void unpublish() {
        if (mActiveMessage != null) {
            Nearby.Messages.unpublish(mGoogleApiClient, mActiveMessage);
            mActiveMessage = null;
        }
    }

    /**
     * Messages Subscribe
     */
    private void subscribeMessages() {
        Nearby.Messages.subscribe(mGoogleApiClient, mMessageListener);
    }

    /**
     * Messages Unsubscribe
     */
    private void unsubscribeMessages() {
        if (mGoogleApiClient.isConnected()) {
            Nearby.Messages.unsubscribe(mGoogleApiClient, mMessageListener);
        }
    }

}
