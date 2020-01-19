package com.keelim.nandadiagnosis.view.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.model.roomdb.AppDatabase
import com.keelim.nandadiagnosis.model.roomdb.NandaEntity
import com.keelim.nandadiagnosis.model.DatabaseAdapter

class SearchFragment : Fragment() {

    private lateinit var listview: ListView

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_search, container, false)
        setHasOptionsMenu(true)

        val adapter = DatabaseAdapter(context!!, arrayListOf())
        listview = root.findViewById(R.id.dbanswer_listview)
        listview.adapter = adapter
        listview.onItemClickListener = OnItemClickListener { adapterView: AdapterView<*>, view: View?, i: Int, l: Long ->
            val db = adapterView.adapter.getItem(i) as NandaEntity
            Toast.makeText(activity, "클래스 영역: " + db.class_name + "도매인 영역" + db.domain_name, Toast.LENGTH_SHORT).show()
        }
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val item = menu.findItem(R.id.menu_search)

        val searchView: SearchView? = item.actionView as SearchView
        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))
        searchView?.isSubmitButtonEnabled = true

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            //검색을 할 수 있게 하는 것
            override fun onQueryTextSubmit(query: String): Boolean {
                var items = searchDiagnosis(query) //검색을 한다.
                listview.adapter = DatabaseAdapter(activity!!, items)
                (listview.adapter as DatabaseAdapter).notifyDataSetChanged()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun searchDiagnosis(keyword: String): List<NandaEntity> { //여기까지는 이상이 없는 것 같다.
        return AppDatabase.getInstance(activity!!)!!.DataDao().search("%$keyword%")
    }
}