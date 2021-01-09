package com.keelim.kakao.search.network

import com.keelim.kakao.search.enums.KakaoSearchSortType
import com.keelim.kakao.search.model.BaseKakaoResponse
import com.keelim.kakao.search.model.KakaoImageResponse
import com.keelim.kakao.search.model.KakaoWebResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoSearchApi {

    //    query	String	검색을 원하는 질의어	O
    //    sort	String	결과 문서 정렬 방식, accuracy(정확도순) 또는 recency(최신순), 기본 값 accuracy	X
    //    page	Integer	결과 페이지 번호, 1~50 사이의 값, 기본 값 1	X
    //    size	Integer	한 페이지에 보여질 문서 수, 1~50 사이의 값, 기본 값 10	X
    @GET("v2/search/web")
    suspend fun searchWeb(
        @Header("Authorization") restApiKey: String,
        @Query("query") query: String,
        @Query("sort") sort: KakaoSearchSortType = KakaoSearchSortType.ACCURACY,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
    ): BaseKakaoResponse<KakaoWebResponse>

    //  query	String	검색을 원하는 질의어	O
    //  sort	String	결과 문서 정렬 방식, accuracy(정확도순) 또는 recency(최신순), 기본 값 accuracy	X
    //  page	Integer	결과 페이지 번호, 1~15 사이의 값	X
    //  size	Integer	한 페이지에 보여질 문서 수, 1~30 사이의 값, 기본 값 15	X
//    @GET("v2/search/vclip")
//    suspend fun searchVclip(
//        @Header("Authorization") restApiKey: String,
//        @Query("query") query: String,
//        @Query("sort") sort: KakaoSearchSortType = KakaoSearchSortType.ACCURACY,
//        @Query("page") page: Int = 1,
//        @Query("size") size: Int = 15,
//    ): BaseKakaoResponse<KakaoVclipResponse>

    // query	String	검색을 원하는 질의어	O
    // sort	String	결과 문서 정렬 방식, accuracy(정확도순) 또는 recency(최신순), 기본 값 accuracy	X
    // page	Integer	결과 페이지 번호, 1~50 사이의 값, 기본 값 1	X
    // size	Integer	한 페이지에 보여질 문서 수, 1~80 사이의 값, 기본 값 80	X
    @GET("v2/search/image")
    suspend fun searchImage(
        @Header("Authorization") restApiKey: String,
        @Query("query") query: String,
        @Query("sort") sort: KakaoSearchSortType = KakaoSearchSortType.ACCURACY,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 80,
    ): BaseKakaoResponse<KakaoImageResponse>

    // query	String	검색을 원하는 질의어
    // 특정 블로그 글만 검색하고 싶은 경우, 블로그 url과 검색어를 공백(' ') 구분자로 넣을 수 있음	O
    // sort	String	결과 문서 정렬 방식, accuracy(정확도순) 또는 recency(최신순), 기본 값 accuracy	X
    // page	Integer	결과 페이지 번호, 1~50 사이의 값, 기본 값 1	X
    // size	Integer	한 페이지에 보여질 문서 수, 1~50 사이의 값, 기본 값 10	X
//    @GET("v2/search/blog")
//    suspend fun searchBlog(
//        @Header("Authorization") restApiKey: String,
//        @Query("query") query: String,
//        @Query("sort") sort: KakaoSearchSortType = KakaoSearchSortType.ACCURACY,
//        @Query("page") page: Int = 1,
//        @Query("size") size: Int = 10,
//    ): BaseKakaoResponse<KakaoBlogResponse>

    // query	String	검색을 원하는 질의어	O
    // sort	String	결과 문서 정렬 방식, accuracy(정확도순) 또는 latest(발간일순), 기본값 accuracy	X
    // page	Integer	결과 페이지 번호, 1~50 사이의 값, 기본 값 1	X
    // size	Integer	한 페이지에 보여질 문서 수, 1~50 사이의 값, 기본 값 10	X
    // target	String	검색 필드 제한
    // 사용 가능한 값: title(제목), isbn (ISBN), publisher(출판사), person(인명)	X
//    @GET("v3/search/book")
//    suspend fun searchBook(
//        @Header("Authorization") restApiKey: String,
//        @Query("query") query: String,
//        @Query("sort") sort: KakaoSearchSortType = KakaoSearchSortType.ACCURACY,
//        @Query("page") page: Int = 1,
//        @Query("size") size: Int = 10,
//        @Query("target") target: KakaoSearchBookTargetType? = null,
//    ): BaseKakaoResponse<KakaoBookResponse>

    // query	String	검색을 원하는 질의어	O
    // sort	String	결과 문서 정렬 방식, accuracy(정확도순) 또는 recency(최신순), 기본 값 accuracy	X
    // page	Integer	결과 페이지 번호, 1~50 사이의 값, 기본 값 1	X
    // size	Integer	한 페이지에 보여질 문서 수, 1~50 사이의 값, 기본 값 10	X
//    @GET("v2/search/cafe")
//    suspend fun searchCafe(
//        @Header("Authorization") restApiKey: String,
//        @Query("query") query: String,
//        @Query("sort") sort: KakaoSearchSortType = KakaoSearchSortType.ACCURACY,
//        @Query("page") page: Int = 1,
//        @Query("size") size: Int = 10,
//    ): BaseKakaoResponse<KakaoCafeResponse>

}
