package com.keelim.nandadiagnosis.ui.main.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.databinding.FragmentSearchBinding
import com.keelim.nandadiagnosis.db.AppDatabase
import com.keelim.nandadiagnosis.db.NandaEntity

class SearchFragment : Fragment(R.layout.fragment_search) {
    private var fragmentSearchBinding: FragmentSearchBinding? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val binding = FragmentSearchBinding.bind(view)
        fragmentSearchBinding = binding

        binding.searchLv.adapter = DatabaseAdapter(requireContext(), arrayListOf())
        binding.searchLv.setOnItemClickListener { adapterView, _, i, _ ->
            val db = adapterView.adapter.getItem(i) as NandaEntity
            Toast.makeText(activity, "클래스 영역: " + db.class_name + "도매인 영역" + db.domain_name, Toast.LENGTH_SHORT).show() // 무슨 화면이 나와야 하는가?
            contentControl(db.nanda_id)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val item = menu.findItem(R.id.menu_search)

        val searchView = item.actionView as SearchView
        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.isSubmitButtonEnabled = true

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            //검색을 할 수 있게 하는 것
            override fun onQueryTextSubmit(query: String): Boolean {
                val items = searchDiagnosis(query) //검색을 한다.

                fragmentSearchBinding!!.searchLv.adapter = DatabaseAdapter(activity!!, items).apply {
                    notifyDataSetChanged()
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun searchDiagnosis(keyword: String): List<NandaEntity> { //여기까지는 이상이 없는 것 같다.
        return AppDatabase.getInstance(requireActivity())!!.dataDao().search("%$keyword%")
    }

    private fun contentControl(position: Int) { // 넘겨 받은 난다 아이디를 통하여 구분
        when (position) {
            in 0..10 -> {
            }
        }
    }

    override fun onDestroyView() {
        fragmentSearchBinding = null
        super.onDestroyView()
    }
}