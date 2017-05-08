package me.ns.androidplayground.nearbymessages;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.nearby.messages.Message;

/**
 * Messageアダプタ
 * Created by shintaro.nosaka on 2017/05/08.
 */
public class MessageAdapter extends ArrayAdapter<Message> {

    /**
     * コンストラクタ
     *
     * @param context {@link Context}
     */
    public MessageAdapter(@NonNull Context context) {
        super(context, 0);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        ViewHolder viewHolder;

        // データの要素を取得
        Message item = getItem(position);

        if (view == null) {

            // レイアウト取得
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_message, parent, false);

            viewHolder = new ViewHolder(view);

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (item == null) {
            return view;
        }

        // Viewを設定
        viewHolder.labelTextView.setText(new String(item.getContent()));

        return view;
    }

    /**
     * ViewHolder
     *
     * @author shintaro-nosaka
     */
    private static class ViewHolder {

        TextView labelTextView;

        /**
         * コンストラクタ
         *
         * @param view View
         */
        ViewHolder(View view) {
            labelTextView = (TextView) view.findViewById(R.id.listItemMessage_labelTextView);
        }
    }
}
