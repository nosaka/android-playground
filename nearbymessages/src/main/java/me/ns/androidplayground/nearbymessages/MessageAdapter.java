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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Messageアダプタ
 * Created by shintaro.nosaka on 2017/05/08.
 */
public class MessageAdapter extends ArrayAdapter<Message> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());

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

        MessageContent content = MessageContent.from(new String(item.getContent()));

        // Viewを設定
        viewHolder.labelTextView.setText(content.message);
        viewHolder.userNameTextView.setText(content.userName);
        viewHolder.timestampTextView.setText(DATE_FORMAT.format(new Date(content.timestamp)));

        return view;
    }

    /**
     * ViewHolder
     *
     * @author shintaro-nosaka
     */
    private static class ViewHolder {

        TextView labelTextView;

        TextView timestampTextView;

        TextView userNameTextView;

        /**
         * コンストラクタ
         *
         * @param view View
         */
        ViewHolder(View view) {
            labelTextView = (TextView) view.findViewById(R.id.listItemMessage_labelTextView);
            timestampTextView = (TextView) view.findViewById(R.id.listItemMessage_timestampTextView);
            userNameTextView = (TextView) view.findViewById(R.id.listItemMessage_userNameTextView);
        }
    }
}
