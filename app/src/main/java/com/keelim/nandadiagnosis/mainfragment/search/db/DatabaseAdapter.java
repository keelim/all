package com.keelim.nandadiagnosis.mainfragment.search.db;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.keelim.nandadiagnosis.R;

import java.util.List;

public class DatabaseAdapter extends BaseAdapter {
    private Context context;
    private List<DatabaseItem> arrayList;

    public DatabaseAdapter(Context context, List<DatabaseItem> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.item_db, null); //todo?
        TextView diagnosis_textView = view.findViewById(R.id.db_diagnosis);
        diagnosis_textView.setText(arrayList.get(i).getDiagnosis());
        return view;
    }
}
