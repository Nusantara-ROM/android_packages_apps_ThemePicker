/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.android.customization.model.themedicon.domain.interactor

import com.android.wallpaper.picker.undo.domain.interactor.SnapshotRestorer
import com.android.wallpaper.picker.undo.domain.interactor.SnapshotStore
import com.android.wallpaper.picker.undo.shared.model.RestorableSnapshot

class ThemedIconSnapshotRestorer(
    private val isActivated: () -> Boolean,
    private val setActivated: (isActivated: Boolean) -> Unit,
    private val interactor: ThemedIconInteractor,
) : SnapshotRestorer {

    private lateinit var store: SnapshotStore

    override suspend fun setUpSnapshotRestorer(store: SnapshotStore): RestorableSnapshot {
        this.store = store
        return snapshot()
    }

    override suspend fun restoreToSnapshot(snapshot: RestorableSnapshot) {
        val isActivated = snapshot.args[KEY]?.toBoolean() == true
        setActivated(isActivated)
        interactor.setActivated(isActivated)
    }

    fun store(
        isActivated: Boolean,
    ) {
        store.store(snapshot(isActivated = isActivated))
    }

    private fun snapshot(
        isActivated: Boolean? = null,
    ): RestorableSnapshot {
        return RestorableSnapshot(
            args = buildMap { put(KEY, (isActivated ?: isActivated()).toString()) }
        )
    }

    companion object {
        private const val KEY = "is_activated"
    }
}
