package com.keelim.cnubus.data.api

import android.app.DownloadManager
import android.net.Uri
import com.google.android.gms.maps.model.LatLng

import org.koin.dsl.module
import java.io.File

val downloadModule = module {
    single {
        ArrayList<LatLng>().apply {
            add(LatLng(36.363876, 127.345119)); //정삼화
            add(LatLng(36.367262, 127.342408)); //한누리관 뒤
            add(LatLng(36.368622, 127.341531)); // 서문
            add(LatLng(36.374241, 127.343924)); // 음대
            add(LatLng(36.376406, 127.344168)); // 공동 동물
            add(LatLng(36.372513, 127.343118)); //체육관 입구
            add(LatLng(36.370587, 127.343520)); // 예술대학앞
            add(LatLng(36.369522, 127.346725)); // 도서관앞
            add(LatLng(36.369119, 127.351884)); //농업생명과학대학
            add(LatLng(36.367465, 127.352190)); //동문
            add(LatLng(36.372480, 127.346155)); //생활관
            add(LatLng(36.369780, 127.346901)); //도서관앞
            add(LatLng(36.367404, 127.345517)); //공과대학앞
            add(LatLng(36.365505, 127.345159)); //산학협력관
            add(LatLng(36.367564, 127.345800)); //경상대학
        }
    }
}