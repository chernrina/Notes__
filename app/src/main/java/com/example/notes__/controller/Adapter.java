package com.example.notes__.controller;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.notes__.R;
import com.example.notes__.model.Document;

import java.util.ArrayList;

public class Adapter extends ArrayAdapter<Document> {

    private View.OnClickListener checkBoxListener;

    public Adapter(Context context, ArrayList<Document> list, View.OnClickListener checkBoxListener) {
        super(context, R.layout.listview, list);
        this.checkBoxListener = checkBoxListener;
    }

    /**
     * @param position номер элемента в списке
     * @param convertView имеющийся список
     * @param parent контейнер для convertView
     * @return готовый список
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.todoName = convertView.findViewById(R.id.todoName);
            viewHolder.todoDate = convertView.findViewById(R.id.todoDate);
            viewHolder.imagePriority = convertView.findViewById(R.id.imageTask);
            viewHolder.checkBox = convertView.findViewById(R.id.checkBox);
            viewHolder.checkBox.setOnClickListener(checkBoxListener);
            convertView.setTag(viewHolder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        Document doc = getItem(position);
        holder.todoName.setText(doc.getName());
        holder.todoDate.setText(DateFormat.format("dd MMMM yyyy, kk:mm", doc.getCreateDate()));
        holder.checkBox.setChecked(doc.getCheckBox());
        holder.checkBox.setTag(doc);
        if (holder.checkBox.isChecked()) {
            holder.todoName.setTextColor(Color.GRAY);
            holder.todoDate.setTextColor(Color.GRAY);
            holder.imagePriority.setImageResource(R.drawable.ic_checked);
        } else {
            holder.todoName.setTextColor(Color.BLACK);
            holder.todoDate.setTextColor(Color.BLACK);
            switch (doc.getPriorityType()) {
                case ORDINARY:{
                    holder.imagePriority.setImageResource(R.drawable.ic_ordinary_note);
                    break;
                }
                case IMPORTANT: {
                    holder.imagePriority.setImageResource(R.drawable.ic_important_note);
                    break;
                }
                default:break;
            }
        }
        return convertView;
    }

    static class ViewHolder {
        private TextView todoName;
        private TextView todoDate;
        private ImageView imagePriority;
        private CheckBox checkBox;
    }

}
