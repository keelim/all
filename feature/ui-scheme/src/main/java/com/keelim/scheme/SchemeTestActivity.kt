package com.keelim.scheme

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.keelim.composeutil.resource.space8
import com.keelim.composeutil.ui.theme.KeelimTheme

class SchemeTestActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KeelimTheme {
                val activity = LocalActivity.current ?: this
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Button(
                        onClick = {
                            IdDetailActivity.Companion.startActivity(activity, "Main")
                        },
                    ) {
                        Text(
                            text = "Main",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = space8),
                        )
                    }

                    Button(
                        onClick = {
                            IdActivity.Companion.startActivity(activity, "Main Korea")
                        },
                    ) {
                        Text(
                            text = "Main Korea",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = space8),
                        )
                    }
                }
            }
        }
    }

    companion object {

        fun getIntent(context: Context): Intent =
            Intent(context, SchemeTestActivity::class.java).apply {
                addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                        or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        or Intent.FLAG_ACTIVITY_CLEAR_TASK,
                )
            }
    }
}
