package com.keelim.core.database.wellness

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.db.SupportSQLiteDatabase
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.lang.reflect.Proxy

class WellnessMigrationTest : FunSpec({
    test("migration preserves existing values and permits partial records after reopening") {
        val file = java.io.File.createTempFile("wellness-migration", ".db")
        try {
            BundledSQLiteDriver().open(file.absolutePath).use { connection ->
                val db = Proxy.newProxyInstance(
                    SupportSQLiteDatabase::class.java.classLoader,
                    arrayOf(SupportSQLiteDatabase::class.java),
                ) { _, method, args ->
                    check(method.name == "execSQL")
                    connection.prepare(args!![0] as String).use { it.step() }
                    null
                } as SupportSQLiteDatabase
                WellnessDatabase.MIGRATION_1_2.migrate(db)
                connection.prepare(
                    "INSERT INTO daily_check_ins VALUES ('2026-09-05', 3, 2, 4, 5, 1, 'NOT_CHECKED', 0, 1, 0, 'legacy')",
                ).use { it.step() }
                WellnessDatabase.MIGRATION_2_3.migrate(db)
                connection.prepare("SELECT * FROM daily_check_ins").use { row ->
                    row.step() shouldBe true
                    row.getText(0) shouldBe "2026-09-05"
                    (1..5).map { row.getLong(it) } shouldBe listOf(3L, 2L, 4L, 5L, 1L)
                    row.getText(6) shouldBe "NOT_CHECKED"
                    (7..9).map { row.getLong(it) } shouldBe listOf(0L, 1L, 0L)
                    row.getText(10) shouldBe "legacy"
                }
                connection.prepare("INSERT INTO daily_check_ins (localDate, energy, note) VALUES ('2026-09-06', 4, '')").use { it.step() }
            }
            BundledSQLiteDriver().open(file.absolutePath).use { connection ->
                connection.prepare("SELECT sleep, energy, morningCondition, drankAlcohol FROM daily_check_ins WHERE localDate = '2026-09-06'").use { row ->
                    row.step() shouldBe true
                    row.isNull(0) shouldBe true
                    row.getLong(1) shouldBe 4L
                    row.isNull(2) shouldBe true
                    row.isNull(3) shouldBe true
                }
            }
        } finally { file.delete() }
    }
})
