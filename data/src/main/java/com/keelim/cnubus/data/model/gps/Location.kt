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
package com.keelim.cnubus.data.model.gps

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    val latLng: LatLng,
    val roota: Int,
    val rootb: Int,
    val name: String,
    val rootc: Int = -1,
    val imgUrl: String = ""
) : Parcelable {
    companion object {
        const val EX_NUMBER = 999
        fun defaultLocation(): Location = Location(
            LatLng(36.363876, 127.345119),
            1,
            1,
            "정심화국제문화회관",
            999,
            "https://plus.cnu.ac.kr/images/cyber/thumbnails/691/d_39/2.jpg"
        )
    }
}

val locationList = listOf(
    Location(
        LatLng(36.363876, 127.345119),
        1,
        1,
        "정심화국제문화회관",
        999,
        "https://plus.cnu.ac.kr/images/cyber/thumbnails/691/d_39/2.jpg"
    ), // 정삼화
    Location(
        LatLng(36.367262, 127.342408),
        13,
        2,
        "사회과학대학입구(한누리회관뒤)",
        999,
        "https://plus.cnu.ac.kr/images/cyber/thumbnails/280/48.jpg"
    ), // 한누리관 뒤
    Location(LatLng(36.368622, 127.341531), 12, 3, "서문(공동실험실습관앞)", 999), // 서문
    Location(
        LatLng(36.374241, 127.343924),
        9,
        4,
        "음악2호관앞",
        999,
        "https://plus.cnu.ac.kr/images/cyber/thumbnails/691/d_30/1.jpg"
    ), // 음대
    Location(
        LatLng(36.376406, 127.344168),
        10,
        5,
        "생명과학대학",
        999,
        "https://plus.cnu.ac.kr/images/cyber/thumbnails/280/07.jpg"
    ), // 공동 동물
    Location(
        LatLng(36.372513, 127.343118),
        11,
        6,
        "체육관입구",
        999,
        "https://plus.cnu.ac.kr/images/cyber/thumbnails/691/d_23/2.jpg"
    ), // 체육관 입구
    Location(
        LatLng(36.370587, 127.343520),
        8,
        7,
        "예술대학앞",
        999,
        "https://plus.cnu.ac.kr/images/cyber/thumbnails/691/d_28/1.jpg"
    ), // 예술대학앞
    Location(
        LatLng(36.369522, 127.346725),
        3,
        8,
        "도서관앞(대학본부옆농대방향)",
        999,
        "https://plus.cnu.ac.kr/images/cyber/thumbnails/691/d_14/1.jpg"
    ), // 도서관앞
    Location(
        LatLng(36.369119, 127.351884),
        5,
        9,
        "농업생명과학대학 앞",
        999,
        "https://plus.cnu.ac.kr/images/cyber/thumbnails/691/d_11/1.jpg"
    ), // 농업생명과학대학
    Location(LatLng(36.367465, 127.352190), 6, 10, "동문주차장", 999), // 동문
    Location(
        LatLng(36.372480, 127.346155),
        4,
        11,
        "학생생활관3거리",
        999,
        "https://plus.cnu.ac.kr/images/cyber/thumbnails/280/47.jpg"
    ), // 생활관
    Location(
        LatLng(36.369780, 127.346901),
        7,
        12,
        "도서관앞",
        999,
        "https://plus.cnu.ac.kr/images/cyber/thumbnails/691/d_14/1.jpg"
    ), // 도서관앞
    Location(
        LatLng(36.367404, 127.345517),
        999,
        13,
        "공과대학앞",
        999,
        "https://plus.cnu.ac.kr/images/cyber/thumbnails/691/d_03/1.jpg"
    ), // 공과대학앞
    Location(
        LatLng(36.365505, 127.345159),
        14,
        14,
        "산학연교육연구관앞",
        999,
        "https://plus.cnu.ac.kr/images/cyber/thumbnails/691/d_19/1.jpg"
    ), // 산학협력관
    Location(
        LatLng(36.367564, 127.345800),
        2,
        999,
        "경상대학교",
        999,
        "https://plus.cnu.ac.kr/images/cyber/thumbnails/691/d_01/1.jpg"
    ), // 경상대학
    Location(
        LatLng(36.370397, 127.347331),
        999,
        999,
        "골프장",
        1,
        "https://plus.cnu.ac.kr/images/cyber/thumbnails/691/d_01/1.jpg"
    ), // 골프장
    Location(
        LatLng(36.369780, 127.346901),
        999,
        999,
        "도서관앞",
        2,
        "https://plus.cnu.ac.kr/images/cyber/thumbnails/691/d_01/1.jpg"
    ), // 도서관앞
    Location(
        LatLng(36.365505, 127.345159),
        999,
        999,
        "산학 연구관",
        3,
        "https://plus.cnu.ac.kr/images/cyber/thumbnails/691/d_01/1.jpg"
    ), // 산학 연구관
    Location(
        LatLng(36.352368611951015, 127.34041874632953),
        999,
        999,
        "유성 온천역 GS 주유소",
        4,
        "https://plus.cnu.ac.kr/images/cyber/thumbnails/691/d_01/1.jpg"
    ), // GS 주유소
    Location(
        LatLng(36.35369631319269, 127.34101029632413),
        999,
        999,
        "유성온천역 4번 출구",
        5,
        "https://plus.cnu.ac.kr/images/cyber/thumbnails/691/d_01/1.jpg"
    ), // 유성온천역 4번
    Location(
        LatLng(36.363876, 127.345119),
        999,
        999,
        "정심화 국제",
        6,
        "https://plus.cnu.ac.kr/images/cyber/thumbnails/691/d_01/1.jpg"
    ), // 정심화 국제
    Location(
        LatLng(36.369780, 127.346901),
        999,
        999,
        "도서관",
        7,
        "https://plus.cnu.ac.kr/images/cyber/thumbnails/691/d_01/1.jpg"
    ), // 도서관
    Location(
        LatLng(36.369780, 127.346901),
        999,
        999,
        "골프장",
        8,
        "https://plus.cnu.ac.kr/images/cyber/thumbnails/691/d_01/1.jpg"
    ), // 골프장
)
