package com.keelim.nandadiagnosis.mainFragment.category;

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

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;
import com.keelim.nandadiagnosis.R;
import com.keelim.nandadiagnosis.db.DatabaseHelper;
import com.keelim.nandadiagnosis.db.DbAdapter;
import com.keelim.nandadiagnosis.db.DbItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchFragment extends Fragment { //todo view model 하고 같이 수정을 할 것
    private ListView listview;
    private DatabaseHelper databaseHelper;
    private List<DbItem> dbItems;
    private FragmentTransaction transaction;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_searcht, container, false);
        listview = root.findViewById(R.id.dbanswer_listview);

        setHasOptionsMenu(true);
        databaseHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        databaseHelper = new DatabaseHelper(getActivity().getApplicationContext());

        listview.setOnItemClickListener((adapterView, view, i, l) -> {
            DbItem db = (DbItem) adapterView.getAdapter().getItem(i);
            Snackbar.make(view, "클래스 영역: " + db.getClass_name() + "도매인 영역" + db.getDomain_name(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show(); //텍스트 뷰로 넘길 수 있다.
            ; //ArrayList 반환
            transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.nav_host_fragment, SearchAnswerFragment.newInstance(diagnosis(db.getDomain_name())));
        });

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //search 등록 -> Fragment 마다 다르게 할 수 있음
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem item = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) item.getActionView();
        SearchManager searchManager = (SearchManager) Objects.requireNonNull(getActivity()).getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(Objects.requireNonNull(searchManager).getSearchableInfo(getActivity().getComponentName()));
        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchDiagnosis(query); //검색을 한다.
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

    private void searchDiagnosis(String keyword) {
        dbItems = databaseHelper.search(keyword);
        if (dbItems != null) {
            listview.setAdapter(new DbAdapter(getActivity().getApplicationContext(), dbItems));
        }
    }

    private ArrayList<String> diagnosis(String keyword) {
        return databaseHelper.diagnosisAll(keyword);
    }
}
