package com.keelim.comssa.utils

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.keelim.comssa.data.model.Data

class SampleDataGenerator {

    fun generate() {
        val firestore = Firebase.firestore

        datas.forEach {
            firestore.collection("datas").add(it)
        }
    }

    private val datas = listOf(
        Data(
            isFeatured = false,
            title = "크루엘라",
            actors = "엠마 스톤 ,엠마 톰슨 ,조엘 프라이 ,폴 월터 하우저 ,에밀리 비샴 ,마크 스트롱",
            country = "미국",
            director = "크레이그 질레스피",
            genre = "#드라마 #범죄 #코메디",
            posterUrl = "http://file.koreafilm.or.kr/poster/99/17/28/DPF022415_01.jpg",
            rating = "12세관람가",
            averageScore = 4.7f,
            numberOfScore = 457_967,
            releaseYear = 2021,
            runtime = 134
        ),
        Data(
            isFeatured = false,
            title = "분노의 질주: 더 얼티메이트",
            actors = "빈 디젤 , 존 시나 , 성강 , 샤를리즈 테론 , 미셸 로드리게즈",
            country = "미국",
            director = "저스틴 린",
            genre = "#액션 #어드벤처 #범죄 #스릴러",
            posterUrl = "http://file.koreafilm.or.kr/poster/99/17/26/DPF022323_01.jpg",
            rating = "12세 관람가",
            averageScore = 4.5f,
            numberOfScore = 1_900_000,
            releaseYear = 2021,
            runtime = 142
        ),
        Data(
            isFeatured = false,
            title = "컨저링 3: 악마가 시켰다",
            actors = "베라 파미가 , 패트릭 윌슨 , 로우리 오코너 , 사라 캐서린 훅 , 줄리안 힐리아드",
            country = "미국",
            director = "마이클 차베즈",
            genre = "#공포 #미스터리 #스릴러",
            posterUrl = "http://file.koreafilm.or.kr/poster/99/17/27/DPF022386_01.jpg",
            rating = "15세 관람가",
            averageScore = 3.9f,
            numberOfScore = 111_115,
            releaseYear = 2021,
            runtime = 111
        ),
        Data(
            isFeatured = false,
            title = "파이프라인",
            actors = "서인국, 이수혁, 음문",
            country = "한국",
            director = "유하",
            genre = "#범죄",
            posterUrl = "http://file.koreafilm.or.kr/poster/99/17/28/DPK017049_01.jpg",
            rating = "15세 관₩람가",
            averageScore = 3.9f,
            numberOfScore = 114_358,
            releaseYear = 2021,
            runtime = 108
        ),
        Data(
            isFeatured = false,
            title = "극장판 귀멸의 칼날: 무한열차편",
            actors = "하나에 나츠키(카마도 탄지로 목소리), 키토 아카리(카마도 네즈코 목소리)",
            country = "일본",
            director = "소토자키 하루오",
            genre = "#애니메이션",
            posterUrl = "http://file.koreafilm.or.kr/poster/99/17/11/DPF021428_01.jpg",
            rating = "15세 관람가",
            averageScore = 4.6f,
            numberOfScore = 2_075_854,
            releaseYear = 2020,
            runtime = 117
        ),
        Data(
            isFeatured = false,
            title = "보이저스",
            actors = "콜린 파렐, 타이 쉐이던, 릴리 로즈 멜로디 뎁",
            country = "미국",
            director = "닐 버거",
            genre = "#SF #모험 #스릴러",
            posterUrl = "http://file.koreafilm.or.kr/poster/99/17/27/DPF022380_01.jpg",
            rating = "15세 관람가",
            averageScore = 3.0f,
            numberOfScore = 18_245,
            releaseYear = 2021,
            runtime = 108
        ),
        Data(
            isFeatured = false,
            title = "학교 가는 길",
            actors = "이은자, 정난모, 조부용",
            country = "한국",
            director = "김정인",
            genre = "#다큐멘터리",
            posterUrl = "https://t1.daumcdn.net/movie/d1497bec0b89201b43f1929f77dea53202df148d",
            rating = "12세 관람가",
            averageScore = 4.8f,
            numberOfScore = 18_633,
            releaseYear = 2020,
            runtime = 99
        ),
        Data(
            isFeatured = false,
            title = "낫아웃",
            actors = "정재광, 정승길, 김희창",
            country = "한국",
            director = "이정곤",
            genre = "#드라마",
            posterUrl = "http://file.koreafilm.or.kr/poster/99/17/29/DPK017068_01.jpg",
            rating = "15세 관람가",
            averageScore = 4.6f,
            numberOfScore = 3360,
            releaseYear = 2021,
            runtime = 107
        ),
        Data(
            isFeatured = false,
            title = "굴뚝마을의 푸펠",
            actors = "아시다 마나(루비치 목소리), 쿠보타 마사타카(푸펠 목소리)",
            country = "일본",
            director = "히로타 유스케",
            genre = "#애니메이션 #판타지",
            posterUrl = "http://file.koreafilm.or.kr/poster/99/17/27/DPF022390_01.jpg",
            rating = "전체 관람가",
            averageScore = 4.4f,
            numberOfScore = 16_305,
            releaseYear = 2021,
            runtime = 100
        )
    )
}