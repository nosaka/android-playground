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
public class RegistrationView extends FrameLayout {

    @BindView(R.id.registration_LoginButton)
    Button mLoginButton;

    @BindView(R.id.registration_RegistrationButton)
    Button mRegistrationButton;

    @BindView(R.id.registration_EmailEditText)
    EditText mEmailEditText;

    /**
     * {@link ViewCallback}
     */
    private ViewCallback mViewCallback;

    public RegistrationView(Context context, ViewCallback callback) {
        super(context);
        inflate(context, R.layout.view_registration, this);
        ButterKnife.bind(this);
        mViewCallback = callback;
    }

    @OnClick(R.id.registration_LoginButton)
    void onClickLoginButton() {
        // ViewCallbackを引き渡す
        LoginView loginView = new LoginView(getContext(), mViewCallback);
        loginView.setEmailText(mEmailEditText.getText());
        Scene scene = new Scene(this, (View) loginView);
        TransitionManager.go(scene, new Fade());
    }

    @OnClick(R.id.registration_RegistrationButton)
    void onClickRegistrationButton() {
        if (mViewCallback != null) {
            mViewCallback.onAction(this, R.id.registration_RegistrationButton);
        }
    }

    void setEmailText(CharSequence charSequence) {
        mEmailEditText.setText(charSequence);
    }

}
