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
package com.keelim.comssa.data.api

import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.keelim.comssa.data.model.Data
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class DataApiImpl @Inject constructor() : DataApi {
    private val fireStore = Firebase.firestore

    override suspend fun getAllData(): List<Data> {
        return fireStore.collection("datas")
            .get()
            .await()
            .map { it.toObject<Data>() }
    }

    override suspend fun getDatas(dataIds: List<String>): List<Data> {
        return fireStore.collection("datas")
            .whereIn(FieldPath.documentId(), dataIds)
            .get()
            .await()
            .map { it.toObject<Data>() }
    }
}
