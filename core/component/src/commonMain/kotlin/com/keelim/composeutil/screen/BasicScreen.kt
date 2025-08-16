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
package com.keelim.composeutil.screen

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.keelim.composeutil.resource.space24
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Private composable function for displaying a greeting with expandable content
 * Demonstrates Material Design 3 components and animations
 * 
 * @param name The name to display in the greeting
 */
@Composable
private fun Greeting(name: String) {
    var expanded by remember { mutableStateOf(false) }

    val extraPadding by animateDpAsState(
        if (expanded) 48.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow,
        ),
        label = "extra_padding",
    )
    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = space4, horizontal = space8),
    ) {
        Row(modifier = Modifier.padding(space24)) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = extraPadding.coerceAtLeast(0.dp)),
            ) {
                // TODO: Replace with string resources when build system supports it
                Text(text = "Hello, ")
                Text(text = name)
            }
            OutlinedButton(
                onClick = { expanded = !expanded },
            ) {
                // TODO: Replace with string resources when build system supports it
                Text(if (expanded) "Show less" else "Show more")
            }
        }
    }
}

/**
 * Composable function that displays a list of greetings with expandable content
 * Follows Material Design 3 guidelines and Android Jetpack Compose best practices
 * 
 * @param names List of names to display as greetings, defaults to 1000 numbered items
 */
@Composable
private fun Greetings(names: List<String> = List(1000) { "$it" }) {
    LazyColumn(modifier = Modifier.padding(vertical = space4)) {
        items(items = names) { name ->
            Greeting(name = name)
        }
    }
}

@Preview
/**
 * Main screen composable that switches between onboarding and main content
 * Follows Android architecture guidelines for state management
 */
@Composable
fun GreetingPreview() {
    Column(
        modifier = Modifier.padding(vertical = space4),
    ) {
        Greetings()
    }
}

/**
 * Onboarding screen composable that provides an introduction to the application
 * Demonstrates state management with rememberSaveable and proper Material Design 3 styling
 */
@Composable
fun OnBoarding() {
    var shouldShowOnBoarding by rememberSaveable {
        mutableStateOf(true)
    }
    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // TODO: Replace with string resources when build system supports it
            Text("Welcome to the basic Codelab!")
            Button(
                modifier = Modifier.padding(vertical = space24),
                onClick = { shouldShowOnBoarding = false },
            ) {
                // TODO: Replace with string resources when build system supports it
                Text(text = "Continue")
            }
        }
    }
}

@Composable
fun OnBoardingPreview() {
    OnBoarding()
}
