package me.ns.androidplayground.animation.view;

import android.content.Context;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.ns.androidplayground.animation.R;

/**
 * Login View
 * <p>
 * Created by shintaro.nosaka on 2017/05/16.
 */
public class LoginView extends FrameLayout {

    interface Listener {
        void onLoginResult();
    }

    @BindView(R.id.login_LoginButton)
    Button mLoginButton;

    @BindView(R.id.login_RegistrationButton)
    Button mRegistrationButton;

    @BindView(R.id.login_EmailEditText)
    EditText mEmailEditText;

    @BindView(R.id.login_PasswordEditText)
    EditText mPasswordEditText;

    /**
     * {@link ViewCallback}
     */
    private ViewCallback mViewCallback;

    public LoginView(Context context, ViewCallback callback) {
        super(context);
        inflate(context, R.layout.view_login, this);
        ButterKnife.bind(this);
        mViewCallback = callback;
    }

    @OnClick(R.id.login_LoginButton)
    void onClickLoginButton() {
        if (mViewCallback != null) {
            mViewCallback.onAction(this, R.id.login_LoginButton);
        }
    }

    @OnClick(R.id.login_RegistrationButton)
    void onClickRegistrationButton() {
        // ViewCallbackを引き渡す
        RegistrationView registrationView = new RegistrationView(getContext(), mViewCallback);
        registrationView.setEmailText(mEmailEditText.getText());
        Scene scene = new Scene(this, (View) registrationView);
        TransitionManager.go(scene, new Fade());
    }

    void setEmailText(CharSequence charSequence) {
        mEmailEditText.setText(charSequence);
    }

}
