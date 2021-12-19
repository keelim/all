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

import com.google.android.gms.maps.model.LatLng
import com.keelim.cnubus.data.model.gps.Location
import kotlinx.coroutines.flow.asFlow

data class Location(
    val latLng: LatLng,
    val roota: Int,
    val rootb: Int,
    val name: String,
    val rootc: Int = -1
)

val locationList = listOf(
    Location(LatLng(36.363876, 127.345119), 1, 1, "정심화국제문화회관"), // 정삼화
    Location(LatLng(36.367262, 127.342408), 13, 2, "사회과학대학입구(한누리회관뒤)"), // 한누리관 뒤
    Location(LatLng(36.368622, 127.341531), 12, 3, "서문(공동실험실습관앞)"), // 서문
    Location(LatLng(36.374241, 127.343924), 9, 4, "임시정차)예술대학 앞"), // 음대
    Location(LatLng(36.376406, 127.344168), 10, 5, "음악2호관앞"), // 공동 동물
    Location(
        LatLng(36.372513, 127.343118), 11, 6, "체육관입구"
    ), // 체육관 입구
    Location(LatLng(36.370587, 127.343520), 8, 7, "예술대학앞"), // 예술대학앞
    Location(LatLng(36.369522, 127.346725), 3, 8, "도서관앞(대학본부옆농대방향)"), // 도서관앞
    Location(LatLng(36.369119, 127.351884), 5, 9, "농업생명과학대학 앞"), // 농업생명과학대학
    Location(LatLng(36.367465, 127.352190), 6, 10, "동문주차장"), // 동문
    Location(LatLng(36.372480, 127.346155), 4, 11, "학생생활관3거리"), // 생활관
    Location(LatLng(36.369780, 127.346901), 7, 12, "도서관앞"), // 도서관앞
    Location(LatLng(36.367404, 127.345517), 999999, 13, "공과대학앞"), // 공과대학앞
    Location(LatLng(36.365505, 127.345159), 14, 14, "산학연교육연구관앞"), // 산학협력관
    Location(LatLng(36.367564, 127.345800), 2, 999999, "정심화국제문화회관"), // 경상대학
).asFlow()