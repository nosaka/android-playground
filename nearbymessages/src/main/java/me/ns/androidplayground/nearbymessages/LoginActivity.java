package me.ns.androidplayground.nearbymessages;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    // //////////////////////////////////////////////////////////////////////////
    // staticフィールド
    // //////////////////////////////////////////////////////////////////////////

    // //////////////////////////////////////////////////////////////////////////
    // Bind UI
    // //////////////////////////////////////////////////////////////////////////

    @BindView(R.id.login_UserNameTextInputEditText)
    TextInputEditText mUserNameTextInputEditText;

    @BindView(R.id.login_LoginButton)
    Button mLoginButton;

    // //////////////////////////////////////////////////////////////////////////
    // インスタンスフィールド
    // //////////////////////////////////////////////////////////////////////////

    // //////////////////////////////////////////////////////////////////////////
    // イベントメソッド
    // //////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    String userName = mUserNameTextInputEditText.getText().toString();
                    startActivity(MainActivity.newIntent(getApplicationContext(), userName));
                }
            }
        });
    }


    // //////////////////////////////////////////////////////////////////////////
    // interface実装メソッド
    // //////////////////////////////////////////////////////////////////////////

    // //////////////////////////////////////////////////////////////////////////
    // その他メソッド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * ヴァリデーション処理
     *
     * @return ヴァリデーション結果
     */
    private boolean validation() {
        if (mUserNameTextInputEditText.getText() == null || mUserNameTextInputEditText.getText().length() <= 0) {
            mUserNameTextInputEditText.setError("ユーザ名を入力してください");
            return false;
        }
        mUserNameTextInputEditText.setError(null);
        return true;
    }

}
