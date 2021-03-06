package me.ns.androidplayground.sensor;

import android.content.Context;
import android.hardware.SensorEvent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link SensorEvent}アダプタ
 * Created by shintaro.nosaka on 2017/05/08.
 */
public class SensorEventAdapter extends ArrayAdapter<SensorEvent> {

    /**
     * タイムスタンプ
     */
    private long mTimestamp;

    /**
     * コンストラクタ
     *
     * @param context {@link Context}
     */
    public SensorEventAdapter(@NonNull Context context) {
        super(context, 0);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        ViewHolder viewHolder;

        // データの要素を取得
        SensorEvent item = getItem(position);

        if (view == null) {

            // レイアウト取得
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_sensor_event, parent, false);

            viewHolder = new ViewHolder(view);

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (item == null) {
            return view;
        }

        // Viewを設定
        viewHolder.dateTextView.setText(Long.toString(item.timestamp));
        viewHolder.dataTextView.setText(Constants.getData(item));

        return view;
    }

    /**
     * ViewHolder
     *
     * @author shintaro-nosaka
     */
    static class ViewHolder {

        @BindView(R.id.listItemSensorEvent_DateTextView)
        TextView dateTextView;

        @BindView(R.id.listItemSensorEvent_DataTextView)
        TextView dataTextView;

        /**
         * コンストラクタ
         *
         * @param view View
         */
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * タイムスタンプ設定処理
     *
     * @param timestamp タイムスタンプ
     */
    public void setTimestamp(long timestamp) {
        mTimestamp = timestamp;
    }
}
