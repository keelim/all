/*
 * Designed and developed by 2021 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keelim.comssa.ui.main

import android.app.DownloadManager
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.keelim.comssa.R
import com.keelim.comssa.databinding.ActivityMainBinding
import com.keelim.comssa.extensions.toast
import com.keelim.comssa.provides.SuggestionProvider
import com.keelim.comssa.utils.DownloadReceiver
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels()
    private val itemAdapter = MainAdapter2(
        favoriteListener = { favorite, id ->
            viewModel.favorite(favorite, id)
            toast("관심 목록에 등록을 하였습니다.")
        }
    )

    @Inject
    lateinit var recevier: DownloadReceiver

    private lateinit var downloadManager: DownloadManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
        fileChecking()
    }

    private fun initViews() = with(binding) {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchSection.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false)
            setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean = true
                override fun onQueryTextChange(query: String): Boolean  {
                    search2(query.replace("\\s", ""))
                    return true
                }
            })
        }

        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                SearchRecentSuggestions(
                    this@MainActivity,
                    SuggestionProvider.AUTHORITY,
                    SuggestionProvider.MODE
                )
                    .saveRecentQuery(query, null)
            }
        }
        val snap = LinearSnapHelper()
        recycler.apply {
            adapter = itemAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        snap.attachToRecyclerView(recycler)
        
        bottomButton.setOnClickListener {
            toast("기능 준비중 입니다. 조금만 기다려주세요.")
        }
    }

    private fun fileChecking() {
        val check = File(getExternalFilesDir(null), "comssa.db")
        if (check.exists().not())
            databaseDownloadAlertDialog()
        else
            toast("데이터베이스가 존재합니다. 그대로 진행 합니다")
    }

    private fun databaseDownloadAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle("다운로드 요청")
            .setMessage("어플리케이션 사용을 위해 데이터베이스를 다운로드 합니다.")
            .setPositiveButton("ok") { _, _ ->
                toast("서버로부터 데이터 베이스를 요청 합니다. ")
                downloadDatabase()
            }
            .show()
    }

    private fun downloadDatabase() {
        downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val intentFilter: IntentFilter = IntentFilter().apply {
            addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED)
        }
        registerReceiver(recevier, intentFilter)

        downloadManager.enqueue(
            DownloadManager.Request(Uri.parse(getString(R.string.db_path)))
                .setTitle("Downloading")
                .setDescription("Downloading Database file")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationUri(Uri.fromFile(File(getExternalFilesDir(null), "comssa.db")))
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
        )
    }

    private fun search2(query: String) = lifecycleScope.launch {
        viewModel.getContent(query).collectLatest {
            itemAdapter.submitData(it)
        }
    }
}
