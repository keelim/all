package com.keelim.nandadiagnosis.ui.main.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.keelim.nandadiagnosis.data.db.DataDao
import com.keelim.nandadiagnosis.data.db.NandaEntity
import kotlinx.coroutines.launch

class SearchViewModel(
    val database: DataDao,
    application: Application
): AndroidViewModel(application) {
    private var _items = MutableLiveData<List<NandaEntity>>()
    val items: LiveData<List<NandaEntity>> = _items

     fun search(word: String){
        viewModelScope.launch{
            _items.value = getItemFromDatabase(word)
        }
    }

    private suspend fun getItemFromDatabase(word:String):List<NandaEntity>{
        return database.search(word)
    }
}