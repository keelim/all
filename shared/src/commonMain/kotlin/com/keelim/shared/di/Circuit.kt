package com.keelim.shared.di

import androidx.compose.runtime.Composable
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui

fun provideCircuit(
    uiFactories: Set<Ui.Factory>,
    presenterFactories: Set<Presenter.Factory>,
): Circuit = Circuit.Builder()
    .addUiFactories(factories = uiFactories)
    .addPresenterFactories(factories = presenterFactories)
    .build()

@Composable
fun KeelimContent(
    circuit: Circuit = provideCircuit(
        setOf(),
        setOf(),
    ),
    content: @Composable () -> Unit,
) {
    CircuitCompositionLocals(circuit) {
        content()
    }
}
