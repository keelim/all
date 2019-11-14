package com.keelim.nandadiagnosis.mainfragment.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.keelim.nandadiagnosis.R;
import com.keelim.nandadiagnosis.activities.WebViewActivity;
import com.keelim.nandadiagnosis.mainfragment.search.db.DatabaseAdapter;
import com.keelim.nandadiagnosis.mainfragment.search.db.DatabaseHelper;
import com.keelim.nandadiagnosis.mainfragment.search.db.DatabaseItem;

import java.util.List;

public class SearchFragment extends Fragment {
    private ListView listview;
    private DatabaseHelper databaseHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        listview = root.findViewById(R.id.dbanswer_listview);
        setHasOptionsMenu(true);
        databaseHelper = new DatabaseHelper(getActivity());

        listview.setOnItemClickListener((adapterView, view, i, l) -> {
            DatabaseItem db = (DatabaseItem) adapterView.getAdapter().getItem(i);
            Toast.makeText(getActivity(), "클래스 영역: " + db.getClass_name() + "도매인 영역" + db.getDomain_name(), Toast.LENGTH_SHORT).show();
            //web 으로 넘겨 버리자

            Intent intent_web = new Intent(getActivity(), WebViewActivity.class);
            intent_web.putExtra("URL", urlHandling(db));
            startActivity(intent_web);
        });
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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() { //검색을 할 수 있게 하는 것
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
        List<DatabaseItem> dbItemsQuery = databaseHelper.search(keyword);
        if (dbItemsQuery != null) {
            listview.setAdapter(new DatabaseAdapter(getActivity(), dbItemsQuery));
        }
    }


    private String urlHandling(DatabaseItem item) { //todo 이렇게 하면 기본 페이지로 가는 것을 할 수 가 있다.
        if (item == null) {
            return getString(R.string.url_default);
        } else { // 일단 데이터 베이스 구분자가 없으니까 아이디로 구분을 하자
            int handling = item.getPrimaryKey();
            String url;
            if (handling >= 250) {
                url = getString(R.string.url1);
            } else if (handling >= 200) {
                url = getString(R.string.url2);
            } else {
                url = getString(R.string.url3);
            }
            return url;
        }
    }

}
