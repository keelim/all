package com.keelim.commonAndroid.extensions

import com.keelim.common.extensions.toUiCurrency
import com.keelim.common.extensions.toUiNumber
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

class UiFormatExtensionsTest : FunSpec({
    test("Instant date formats stay fixed regardless of locale") {
        val instant = Instant.parse("2026-02-22T15:07:09Z")
        val originalLocale = Locale.getDefault()

        try {
            Locale.setDefault(Locale.KOREA)
            instant.toUiDate(TimeZone.UTC) shouldBe "2026.02.22"
            instant.toUiDateTime(TimeZone.UTC) shouldBe "2026.02.22 15:07"

            Locale.setDefault(Locale.US)
            instant.toUiDate(TimeZone.UTC) shouldBe "2026.02.22"
            instant.toUiDateTime(TimeZone.UTC) shouldBe "2026.02.22 15:07"
        } finally {
            Locale.setDefault(originalLocale)
        }
    }

    test("LocalDateTime uses fixed UI date time pattern") {
        val localDateTime = LocalDateTime(
            year = 2026,
            month = Month.FEBRUARY,
            day = 3,
            hour = 4,
            minute = 5,
            second = 40,
            nanosecond = 0,
        )

        localDateTime.toUiDateTime() shouldBe "2026.02.03 04:05"
    }

    test("Number formatting follows locale grouping") {
        val number = 1_234_567

        number.toUiNumber(Locale.US) shouldBe NumberFormat.getNumberInstance(Locale.US).format(number)
        number.toUiNumber(Locale.GERMANY) shouldBe NumberFormat.getNumberInstance(Locale.GERMANY).format(number)
    }

    test("Currency formatting uses locale default and explicit currency override") {
        val amount = 1234.5
        val locale = Locale.US

        amount.toUiCurrency(locale) shouldBe NumberFormat.getCurrencyInstance(locale).format(amount)

        val expectedWithKrw = NumberFormat.getCurrencyInstance(locale).apply {
            currency = Currency.getInstance("KRW")
        }.format(amount)

        amount.toUiCurrency(locale, Currency.getInstance("KRW")) shouldBe expectedWithKrw
    }
})
