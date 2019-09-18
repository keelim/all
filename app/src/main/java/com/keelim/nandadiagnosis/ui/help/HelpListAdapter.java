package com.keelim.nandadiagnosis.ui.help;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.keelim.nandadiagnosis.R;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HelpListAdapter extends BaseAdapter {
    Context context;
    ArrayList<HelpListItem> arrayList;
    TextView helpTitle_TextView;

    public HelpListAdapter(Context context, ArrayList<HelpListItem> arrayList) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item, null);
            helpTitle_TextView = view.findViewById(R.id.help_title);
            helpTitle_TextView.setText(arrayList.get(i).getTitle());
        return view;
    }
}
