package me.ns.androidplayground.lifecycles;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shintaro.nosaka on 2017/05/18.
 */
public class MainDialog extends DialogFragment {

    @BindView(R.id.mainDialog_EditText)
    EditText mEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_main, container);

        ButterKnife.bind(this, view);

        final MainActivity.MainVM vm = ViewModelProviders.of(getActivity()).get(MainActivity.MainVM.class);

        mEditText.setText(vm.text.getValue());

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


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Dialog dialog = getDialog();

        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        layoutParams.width = (int) (metrics.widthPixels * 0.8);

        dialog.getWindow().setAttributes(layoutParams);

    }

}
