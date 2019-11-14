package com.keelim.nandadiagnosis.diagnosis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.keelim.nandadiagnosis.R;

import java.util.ArrayList;


public class MyDiagnosisViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<DiagnosisItem> diagnosisItems;

    public MyDiagnosisViewAdapter(Context context, ArrayList<DiagnosisItem> diagnosisItems) {
        this.context = context;
        this.diagnosisItems = diagnosisItems;
    }

    @Override
    public int getCount() {
        return diagnosisItems.size();
    }

    @Override
    public Object getItem(int position) {
        return diagnosisItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.item_listview, null);
        TextView diagnosisItem = view.findViewById(R.id.diagnosis_item);
        diagnosisItem.setText(diagnosisItems.get(position).getDiagnosis());

        TextView diagnosisDes = view.findViewById(R.id.diagnosis_des);
        diagnosisDes.setText(diagnosisItems.get(position).getDiagnosis());
        return view;
    }
}
