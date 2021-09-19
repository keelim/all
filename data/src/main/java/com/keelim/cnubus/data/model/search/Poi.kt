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
package com.keelim.cnubus.data.model.search

data class Poi(
    // POI 의  id
    val id: String? = null,

    // POI 의 name
    val name: String? = null,

    // POI 에 대한 전화번호
    val telNo: String? = null,

    // 시설물 입구 위도 좌표
    val frontLat: Float = 0.0f,

    // 시설물 입구 경도 좌표
    val frontLon: Float = 0.0f,

    // 중심점 위도 좌표
    val noorLat: Float = 0.0f,

    // 중심점 경도 좌표
    val noorLon: Float = 0.0f,

    // 표출 주소 대분류명
    val upperAddrName: String? = null,

    // 표출 주소 중분류명
    val middleAddrName: String? = null,

    // 표출 주소 소분류명
    val lowerAddrName: String? = null,

    // 표출 주소 세분류명
    val detailAddrName: String? = null,

    // 본번
    val firstNo: String? = null,

    // 부번
    val secondNo: String? = null,

    // 도로명
    val roadName: String? = null,

    // 건물번호 1
    val firstBuildNo: String? = null,

    // 건물번호 2
    val secondBuildNo: String? = null,

    // 업종 대분류명
    val mlClass: String? = null,

    // 거리(km)
    val radius: String? = null,

    // 업소명
    val bizName: String? = null,

    // 시설목적
    val upperBizName: String? = null,

    // 시설분류
    val middleBizName: String? = null,

    // 시설이름 ex) 지하철역 병원 등
    val lowerBizName: String? = null,

    // 상세 이름
    val detailBizName: String? = null,

    // 길안내 요청 유무
    val rpFlag: String? = null,

    // 주차 가능유무
    val parkFlag: String? = null,

    // POI 상세정보 유무
    val detailInfoFlag: String? = null,

    // 소개 정보
    val desc: String? = null
)
