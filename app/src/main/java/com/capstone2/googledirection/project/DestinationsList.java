package com.capstone2.googledirection.project;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class DestinationsList extends ArrayAdapter<CompanyInfo> {
 private Activity context;
 private List<CompanyInfo> destinationsList;

 public DestinationsList(Activity context, List<CompanyInfo> destinationsList){
     super(context, R.layout.list_layout, destinationsList);
     this.context = context;
     this.destinationsList = destinationsList;
 }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout,null, true);
        TextView textView4 = (TextView) listViewItem.findViewById(R.id.textView4);
        TextView textView6 = (TextView) listViewItem.findViewById(R.id.textView6);

        CompanyInfo company = destinationsList.get(position);
        textView4.setText(company.getCompanyName());
        textView6.setText(company.getOpenHr());

        return listViewItem;

    }
}

