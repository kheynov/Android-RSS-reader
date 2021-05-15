package com.example.rssreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListItemAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<ListItem> list;

    public ListItemAdapter(Context context, ArrayList<ListItem> list) {
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ListItem getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder.title = view.findViewById(R.id.textTitle);
            viewHolder.pubDate = view.findViewById(R.id.textPubDate);
            viewHolder.content = view.findViewById(R.id.textContent);
            view.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) view.getTag();
        ListItem item = getItem(position);
        viewHolder.title.setText(item.getTitle());
        viewHolder.pubDate.setText(item.getPubDate());
        viewHolder.content.setText(item.getContent());
        return view;
    }

    private static class ViewHolder {
        private TextView title;
        private TextView pubDate;
        private TextView content;
    }
}
