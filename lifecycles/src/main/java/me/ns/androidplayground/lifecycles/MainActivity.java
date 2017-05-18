package me.ns.androidplayground.lifecycles;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends LifecycleActivity {

    public static class MainVM extends ViewModel {

        public MutableLiveData<CharSequence> text = new MutableLiveData<>();

    }

    @BindView(R.id.main_TextView)
    TextView mTextView;

    @BindView(R.id.main_EditText)
    EditText mEditText;

    @BindView(R.id.main_ShowDialogButton)
    Button mShowDialogButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        final MainVM vm = ViewModelProviders.of(this).get(MainVM.class);

        mEditText.setText(vm.text.getValue());

        vm.text.observe(this, new Observer<CharSequence>() {
            @Override
            public void onChanged(@Nullable CharSequence charSequence) {
                mTextView.setText(charSequence);
            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                vm.text.setValue(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mShowDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainDialog dialog = new MainDialog();
                dialog.show(getSupportFragmentManager(), null);
            }
        });

    }

}

