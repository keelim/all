package com.keelim.nandadiagnosis.ui.auth.profile

import android.net.Uri
import com.keelim.nandadiagnosis.data.db.entity.NandaEntity

sealed class ProfileState {
    object UnInitialized:ProfileState()

    object Loading:ProfileState()

    data class Login(
        val token:String,
    ): ProfileState()

    sealed class Success:ProfileState(){
        data class Registered(
            val userName:String,
            val profileImage: Uri?,
            val favoriteList: List<NandaEntity> = listOf()
        ):Success()

        object NotRegistered:Success()
    }

    object Error:ProfileState()
}
