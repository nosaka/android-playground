package me.ns.androidplayground.animation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.ns.androidplayground.animation.view.LoginView;
import me.ns.androidplayground.animation.view.RegistrationView;
import me.ns.androidplayground.animation.view.ViewCallback;

public class TransitionAnimationActivity extends AppCompatActivity {

    // //////////////////////////////////////////////////////////////////////////
    // staticフィールド
    // //////////////////////////////////////////////////////////////////////////

    // //////////////////////////////////////////////////////////////////////////
    // Bind UI
    // //////////////////////////////////////////////////////////////////////////

    @BindView(R.id.transitionAnimation_Toolbar)
    Toolbar mToolbar;

    @BindView(R.id.transitionAnimation_Container)
    ViewGroup mContainer;

    // //////////////////////////////////////////////////////////////////////////
    // インスタンスフィールド
    // //////////////////////////////////////////////////////////////////////////

    // //////////////////////////////////////////////////////////////////////////
    // イベントメソッド
    // //////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition_animation);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mContainer.addView(new LoginView(this, new ViewCallback() {
            @Override
            public void onAction(View view, @IdRes int eventViewId) {
                if (view instanceof LoginView) {
                    switch (eventViewId) {
                        case R.id.login_LoginButton:
                            Toast.makeText(TransitionAnimationActivity.this, "ログインしました", Toast.LENGTH_SHORT).show();
                            finish();
                        default:
                            // 処理なし
                    }
                } else if (view instanceof RegistrationView) {
                    switch (eventViewId) {
                        case R.id.registration_RegistrationButton:
                            Toast.makeText(TransitionAnimationActivity.this, "会員登録メールを送信しました", Toast.LENGTH_SHORT).show();
                            finish();
                        default:
                            // 処理なし
                    }
                }

            }
        }));
    }

    // //////////////////////////////////////////////////////////////////////////
    // interface実装メソッド
    // //////////////////////////////////////////////////////////////////////////

    // //////////////////////////////////////////////////////////////////////////
    // その他メソッド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * {@link Intent}生成
     *
     * @param context {@link Context}
     * @return {@link Intent}
     */
    public static Intent newIntent(Context context) {
        return new Intent(context, TransitionAnimationActivity.class);
    }

}
