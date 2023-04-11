package com.mephisto.personal.Classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.mephisto.personal.R;

import java.util.List;

public class MessageAdapter  extends ArrayAdapter<Message> {

    private final List<Message> messages;
    private final Context context;

    public MessageAdapter(Context context, List<Message> messages) {
        super(context, R.layout.message_item, messages);
        this.messages = messages;
        this.context = context;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.message_item, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.contentTextView = view.findViewById(R.id.content_text_view);
            viewHolder.timestampTextView = view.findViewById(R.id.timestamp_text_view);
            view.setTag(viewHolder);
        }

        Message message = messages.get(position);
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.contentTextView.setText(message.getContent());
        viewHolder.timestampTextView.setText(message.getTimestamp());

        return view;
    }
}

