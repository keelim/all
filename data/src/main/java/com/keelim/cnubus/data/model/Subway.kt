package com.keelim.cnubus.data.model
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.room.PrimaryKey


enum class Subway(
        @PrimaryKey
        val id: Int,
        val label: String,
        @ColorInt val color: Int
) {
    LINE_1(1001, "1호선", Color.parseColor("#FF0D3692")),

    LINE_2(1002, "2호선", Color.parseColor("#FF33A23D")),

    LINE_3(1003, "3호선", Color.parseColor("#FFFE5D10")),

    LINE_4(1004, "4호선", Color.parseColor("#FF00A2D1")),

    LINE_5(1005, "5호선", Color.parseColor("#FF8B50A4")),

    LINE_6(1006, "6호선", Color.parseColor("#FFC55C1D")),

    LINE_7(1007, "7호선", Color.parseColor("#FF54640D")),

    LINE_8(1008, "8호선", Color.parseColor("#FFF14C82")),

    LINE_9(1009, "9호선", Color.parseColor("#FFAA9872")),

    LINE_63(1063, "경의중앙", Color.parseColor("#FF73C7A6")),

    LINE_65(1065, "공항철도", Color.parseColor("#FF3681B7")),

    LINE_67(1067, "경춘", Color.parseColor("#FF32C6A6")),

    LINE_71(1071, "수인분당", Color.parseColor("#FFFF8C00")),

    LINE_75(1075, "수인분당", Color.parseColor("#FFFF8C00")),

    LINE_77(1077, "신분당", Color.parseColor("#FFC82127")),

    UNKNOWN(-1, "확인불가", Color.LTGRAY);

    companion object {
        fun findById(id: Int): Subway = values().find { it.id == id } ?: UNKNOWN
    }
}
