package me.ns.androidplayground.animation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    // //////////////////////////////////////////////////////////////////////////
    // staticフィールド
    // //////////////////////////////////////////////////////////////////////////

    // //////////////////////////////////////////////////////////////////////////
    // Bind UI
    // //////////////////////////////////////////////////////////////////////////

    @BindView(R.id.main_Toolbar)
    Toolbar mToolbar;

    @BindView(R.id.main_SharedTransitionAnimationButton)
    Button mSharedTransitionAnimationButton;

    @BindView(R.id.main_ObjectAnimatorButton)
    Button mObjectAnimatorButton;

    @BindView(R.id.main_ValueAnimatorButton)
    Button mValueAnimatorButton;

    @BindView(R.id.main_TransitionAnimationButton)
    Button mTransitionAnimationButton;


    // //////////////////////////////////////////////////////////////////////////
    // インスタンスフィールド
    // //////////////////////////////////////////////////////////////////////////

    // //////////////////////////////////////////////////////////////////////////
    // イベントメソッド
    // //////////////////////////////////////////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mSharedTransitionAnimationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SharedTransitionAnimationActivity.newIntent(MainActivity.this));
            }
        });


        mObjectAnimatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ObjectAnimatorActivity.newIntent(MainActivity.this));
            }
        });

        mValueAnimatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ValueAnimatorActivity.newIntent(MainActivity.this));
            }
        });
        mTransitionAnimationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(TransitionAnimationActivity.newIntent(MainActivity.this));
            }
        });


    }

    // //////////////////////////////////////////////////////////////////////////
    // interface実装メソッド
    // //////////////////////////////////////////////////////////////////////////

    // //////////////////////////////////////////////////////////////////////////
    // その他メソッド
    // //////////////////////////////////////////////////////////////////////////

}
