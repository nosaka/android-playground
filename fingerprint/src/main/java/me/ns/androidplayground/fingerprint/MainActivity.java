package me.ns.androidplayground.fingerprint;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.ns.lib.AlertUtil;
import me.ns.lib.LogUtil;

public class MainActivity extends AppCompatActivity {

    // //////////////////////////////////////////////////////////////////////////
    // staticフィールド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * リクエストコード：パーミッション要求
     */
    private static final int REQUEST_PERMISSIONS = 0x002;

    // //////////////////////////////////////////////////////////////////////////
    // Bind UI
    // //////////////////////////////////////////////////////////////////////////

    @BindView(R.id.main_AuthenticationFingerprintButton)
    Button mAuthenticationFingerprintButton;

    // //////////////////////////////////////////////////////////////////////////
    // インスタンスフィールド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * {@link FingerprintManager}
     */
    private FingerprintManager mFingerprintManager;

    /**
     * 指紋認証ダイアログ
     */
    private AlertDialog mAuthenticationFingerprintDialog;

    // //////////////////////////////////////////////////////////////////////////
    // イベントメソッド
    // //////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mFingerprintManager = getSystemService(FingerprintManager.class);

        mAuthenticationFingerprintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticationFingerprint();
            }
        });

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
                    authenticationFingerprint();
                } else {
                    AlertUtil.showAlert(this, "指紋認証の利用を許可しない場合は利用できません", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                }
        }
    }

    // //////////////////////////////////////////////////////////////////////////
    // interface実装メソッド
    // //////////////////////////////////////////////////////////////////////////

    // //////////////////////////////////////////////////////////////////////////
    // その他メソッド
    // //////////////////////////////////////////////////////////////////////////


    /**
     * 指紋認証処理
     */
    private void authenticationFingerprint() {
        if (!checkFingerprintPermission()) {
            return;
        }
        if (!mFingerprintManager.isHardwareDetected()) {
            AlertUtil.showAlert(this, "この端末では指紋認証をサポートしていません");
            return;
        }
        if (!mFingerprintManager.hasEnrolledFingerprints()) {
            AlertUtil.showAlert(this, "指紋が登録されていません");
            return;
        }

        if (mAuthenticationFingerprintDialog != null
                && mAuthenticationFingerprintDialog.isShowing()) {
            return;
        }

        final CancellationSignal cancellationSignal = new CancellationSignal();
        mAuthenticationFingerprintDialog = new AlertDialog.Builder(this)
                .setView(R.layout.dialog_fingerprin)
                .setMessage("")
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancellationSignal.cancel();
                    }
                })
                .create();
        mAuthenticationFingerprintDialog.show();
        mFingerprintManager.authenticate(null, cancellationSignal, 0, new FingerprintManager.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                LogUtil.e(errString.toString());
            }

            @Override
            public void onAuthenticationFailed() {
                mAuthenticationFingerprintDialog.setMessage("認証エラー");
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                mAuthenticationFingerprintDialog.dismiss();
                Toast.makeText(MainActivity.this, "認証しました", Toast.LENGTH_SHORT).show();
            }
        }, new Handler());

    }


    /**
     * 指紋認証パーミッションチェック
     *
     * @return チェック結果
     */
    private boolean checkFingerprintPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ((ContextCompat.checkSelfPermission(this, android.Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED))) {

            // パーミッション要求
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.USE_FINGERPRINT}, REQUEST_PERMISSIONS);
            return false;
        }
        return true;
    }

}
