package me.ns.androidplayground.nfc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Messageアダプタ
 * Created by shintaro.nosaka on 2017/05/08.
 */
public class FeliCaHistoryAdapter extends ArrayAdapter<FeliCaHistoryItem> {

    /**
     * 日付フォーマット
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

    /**
     * コンストラクタ
     *
     * @param context {@link Context}
     */
    public FeliCaHistoryAdapter(@NonNull Context context) {
        super(context, 0);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        ViewHolder viewHolder;

        // データの要素を取得
        FeliCaHistoryItem item = getItem(position);

        FeliCaHistoryItem nextItem = null;
        if (getCount() > position + 1) {
            nextItem = getItem(position + 1);
        }

        if (view == null) {

            // レイアウト取得
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_felica_history, parent, false);

            viewHolder = new ViewHolder(view);

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (item == null) {
            return view;
        }

        // Viewを設定
        viewHolder.seqNoTextView.setText(Integer.toString(item.seqNo));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, item.year + 2000);
        calendar.set(Calendar.MONTH, item.month);
        calendar.set(Calendar.DATE, item.day);
        viewHolder.dateTextView.setText(DATE_FORMAT.format(calendar.getTime()));
        viewHolder.termTextView.setText(item.getTermName());
        viewHolder.procTextView.setText(item.getProcName());
        viewHolder.remainTextView.setText(NumberFormat.getCurrencyInstance().format(item.remain));
        if (nextItem != null) {
            int difference = -(nextItem.remain - item.remain);
            viewHolder.differenceTextView.setText(String.format(Locale.getDefault(), "(%1$s)", NumberFormat.getCurrencyInstance().format(difference)));
        } else {
            viewHolder.differenceTextView.setText(null);
        }

        return view;
    }

    /**
     * ViewHolder
     *
     * @author shintaro-nosaka
     */
    static class ViewHolder {

        @BindView(R.id.listItemFeliCaHistory_SeqNoTextView)
        TextView seqNoTextView;

        @BindView(R.id.listItemFeliCaHistory_DateTextView)
        TextView dateTextView;

        @BindView(R.id.listItemFeliCaHistory_TermTextView)
        TextView termTextView;

        @BindView(R.id.listItemFeliCaHistory_ProcTextView)
        TextView procTextView;

        @BindView(R.id.listItemFeliCaHistory_RemainTextView)
        TextView remainTextView;

        @BindView(R.id.listItemFeliCaHistory_DifferenceTextView)
        TextView differenceTextView;

        /**
         * コンストラクタ
         *
         * @param view View
         */
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
