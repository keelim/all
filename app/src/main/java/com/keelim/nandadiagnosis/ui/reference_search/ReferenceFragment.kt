package com.keelim.nandadiagnosis.ui.reference_search

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.keelim.nandadiagnosis.databinding.FragmentReferenceBinding
import com.keelim.reference_search.Reference
import com.keelim.reference_search.ReferenceService
import com.keelim.reference_search.data.AppDatabase
import com.keelim.reference_search.data.History
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class ReferenceFragment : Fragment() {
    private var _binding: FragmentReferenceBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ReferenceAdapter
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var book: ReferenceService
    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReferenceBinding.inflate(inflater, container, false)

        initRecyclerView()
        db = Room.databaseBuilder(
            requireActivity().applicationContext,
            AppDatabase::class.java,
            "BookSearchDB"
        ).build()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://book.interface.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        book = retrofit.create(ReferenceService::class.java)
        book.getReference(
            "1",
            "2",
            "3"
        ).enqueue(object : Callback<Reference> {
            override fun onResponse(call: Call<Reference>, response: Response<Reference>) {
                if (response.isSuccessful.not()) {
                    return
                }
                response.body()?.let {
                    Timber.d("ReferenceFragment")
                }

                adapter.submitList(listOf())
            }

            override fun onFailure(call: Call<Reference>, t: Throwable) {
                Timber.e(t.toString())
            }
        })

        binding.searchEditText.setOnKeyListener { view, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == MotionEvent.ACTION_DOWN) {
                search(binding.searchEditText.text.toString())
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    private fun search(keyword: String) {
        book.getReference("1", "2", "3")
            .enqueue(object : Callback<Reference> {
                override fun onResponse(call: Call<Reference>, response: Response<Reference>) {

                    saveSearchKeyword(keyword)

                    if (response.isSuccessful.not()) {
                        return
                    }
                    response.body()?.let {
                        Timber.d("ReferenceFragment")
                    }

                    adapter.submitList(listOf())
//                adapter.submitList(response.body()?.books.orEmpty())
                }

                override fun onFailure(call: Call<Reference>, t: Throwable) {
                    Timber.e(t.toString())
                }
            })
    }

    private fun initRecyclerView() {

        adapter = ReferenceAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun saveSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().insertHistory(History(null, keyword))
        }.start()
    }

    private fun showHistoryView() {
        Thread{
            val keywords = db.historyDao().getAll().reversed()
        }
        binding.historyRecycler.isVisible = true
    }

    private fun hideHistoryView() {
        binding.historyRecycler.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}