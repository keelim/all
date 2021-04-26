package com.keelim.nandadiagnosis.ui.main.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.keelim.nandadiagnosis.model.db.DataDaoV2
import com.keelim.nandadiagnosis.model.db.NandaEntity
import kotlinx.coroutines.launch

class SearchViewModel(val database: DataDaoV2, application: Application) :
    AndroidViewModel(application) {
    private var nanda = MutableLiveData<NandaEntity?>()

    init {
        initialDB()
    }

    private fun initialDB() {
        viewModelScope.launch {
            nanda.value = getNandaDatabase()
        }
    }

    private suspend fun getNandaDatabase(): NandaEntity? {
        val nanda = database.getNanda()
        return nanda
    }

      fun getSearchMyData(keyword: String?): List<NandaEntity>? {
          val result = MutableLiveData<List<NandaEntity>>()
          viewModelScope.launch{
              val nanda_list: List<NandaEntity> = database.search(keyword)
              result.value = nanda_list
          }
        return result.value
    }

    private suspend fun getCategoryData(number: Int?): List<NandaEntity>? {
        val nanda_list: List<NandaEntity> = database.get(number)
        return nanda_list
    }
}