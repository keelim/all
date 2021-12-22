package com.keelim.data.model

data class Release(
    var date: String, // 2021-11-04
    var description: String, // 조금 더 향상된 검색을 할 수 있습니다.
    var title: String, // 정식으로 엘라스틱 서치를 붙였습니다.
    var version: String // v0.0.1
)