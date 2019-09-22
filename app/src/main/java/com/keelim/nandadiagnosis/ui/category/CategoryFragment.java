package com.keelim.nandadiagnosis.ui.category;

import android.app.SearchManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.keelim.nandadiagnosis.R;
import com.keelim.nandadiagnosis.db.DBitem;
import com.keelim.nandadiagnosis.db.DatabaseHelper;
import com.keelim.nandadiagnosis.db.DbAdapter;

import java.util.List;

public class CategoryFragment extends Fragment { //
    private ListView listview;
    private DatabaseHelper databaseHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CategoryViewModel dashboardViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_category, container, false);

        listview = root.findViewById(R.id.dbanswer_listview);
        loadDatabase();


        final TextView textView = root.findViewById(R.id.text_category);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), s -> textView.setText(s));
        setHasOptionsMenu(true);

        databaseHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //search 등록 -> Fragment 마다 다르게 할 수 있음
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem item = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) item.getActionView();
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchDiagnosis(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchDiagnosis(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void loadDatabase() {
        databaseHelper = new DatabaseHelper(getActivity().getApplicationContext());
        List<DBitem> dBitems = databaseHelper.findAll();
        if (dBitems != null) {
            listview.setAdapter(new DbAdapter(getActivity().getApplicationContext(), dBitems));
        }
    }

    private void searchDiagnosis(String keyword) {
        databaseHelper = new DatabaseHelper(getActivity().getApplicationContext());
        List<DBitem> dBitems = databaseHelper.search(keyword);
        if (dBitems != null) {
            listview.setAdapter(new DbAdapter(getActivity().getApplicationContext(), dBitems));
        }
    }


}
