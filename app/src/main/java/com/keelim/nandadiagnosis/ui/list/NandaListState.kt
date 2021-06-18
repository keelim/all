package com.keelim.nandadiagnosis.ui.list

import com.keelim.nandadiagnosis.data.db.entity.NandaEntity2

sealed class NandaListState{
    object UnInitialized: NandaListState()

    object Loading: NandaListState()

    data class Success(
        val nandaList:List<NandaEntity2>
    ): NandaListState()

    object Error: NandaListState()

}
