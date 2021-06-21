/*
 * Designed and developed by 2020 keelim (Jaehyun Kim)
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
package com.keelim.nandadiagnosis.di

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 데이터 저장 및 로드 클래스
 */
@Singleton
class PreferenceManager @Inject constructor(
  @ApplicationContext private val context: Context
) {

  companion object {
    const val PREFERENCES_NAME = "com.keelim.nandaDiagnosis"
    private const val DEFAULT_VALUE_STRING = ""
    private const val DEFAULT_VALUE_BOOLEAN = false
    private const val DEFAULT_VALUE_INT = -1
    private const val DEFAULT_VALUE_LONG = -1L
    private const val DEFAULT_VALUE_FLOAT = -1f

    const val KEY_ID_TOKEN = "ID_TOKEN"
  }

  private fun getPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
  }

  private val prefs by lazy { getPreferences(context) }

  private val editor by lazy { prefs.edit() }

  /**
   * String 값 저장
   * @param context
   * @param key
   * @param value
   */
  fun setString(key: String?, value: String?) {
    editor.putString(key, value)
    editor.apply()
  }

  /**
   * boolean 값 저장
   * @param context
   * @param key
   * @param value
   */
  fun setBoolean(key: String?, value: Boolean) {
    editor.putBoolean(key, value)
    editor.apply()
  }

  /**
   * int 값 저장
   * @param context
   * @param key
   * @param value
   */
  fun setInt(key: String?, value: Int) {
    editor.putInt(key, value)
    editor.apply()
  }

  /**
   * long 값 저장
   * @param context
   * @param key
   * @param value
   */
  fun setLong(key: String?, value: Long) {
    editor.putLong(key, value)
    editor.apply()
  }

  /**
   * float 값 저장
   * @param context
   * @param key
   * @param value
   */
  fun setFloat(key: String?, value: Float) {
    editor.putFloat(key, value)
    editor.apply()
  }

  /**
   * String 값 로드
   * @param context
   * @param key
   * @return
   */
  fun getString(key: String?): String? {
    return prefs.getString(key, DEFAULT_VALUE_STRING)
  }

  /**
   * boolean 값 로드
   * @param context
   * @param key
   * @return
   */
  fun getBoolean(key: String?): Boolean {
    return prefs.getBoolean(key, DEFAULT_VALUE_BOOLEAN)
  }

  /**
   * int 값 로드
   * @param context
   * @param key
   * @return
   */
  fun getInt(key: String?): Int {
    return prefs.getInt(key, DEFAULT_VALUE_INT)
  }

  /**
   * long 값 로드
   * @param context
   * @param key
   * @return
   */
  fun getLong(key: String?): Long {
    return prefs.getLong(key, DEFAULT_VALUE_LONG)
  }

  /**
   * float 값 로드
   * @param context
   * @param key
   * @return
   */
  fun getFloat(key: String?): Float {
    return prefs.getFloat(key, DEFAULT_VALUE_FLOAT)
  }

  /**
   * 키 값 삭제
   * @param context
   * @param key
   */
  fun removeKey(key: String?) {
    editor.remove(key)
    editor.apply()
  }

  /**
   * 모든 저장 데이터 삭제
   * @param context
   */
  fun clear() {
    editor.clear()
    editor.apply()
  }

  fun putIdToken(idToken: String) {
    editor.putString(KEY_ID_TOKEN, idToken)
    editor.apply()
  }

  fun getIdToken(): String? {
    return prefs.getString(KEY_ID_TOKEN, null)
  }

  fun removedToken() {
    editor.putString(KEY_ID_TOKEN, null)
    editor.apply()
  }
}
