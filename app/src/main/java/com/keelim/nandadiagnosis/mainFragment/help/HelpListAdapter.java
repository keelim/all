package com.keelim.nandadiagnosis.mainFragment.help;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.keelim.nandadiagnosis.R;

import java.util.ArrayList;

public class HelpListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<HelplistItem> arrayList;

    public HelpListAdapter(Context context, ArrayList<HelplistItem> arrayList) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_listview, null);
        TextView helpTitle_TextView = view.findViewById(R.id.help_item);
        helpTitle_TextView.setText(arrayList.get(i).getTitle());
        return view;
    }
}
