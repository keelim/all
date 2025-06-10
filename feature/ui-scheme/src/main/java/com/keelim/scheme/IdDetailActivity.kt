package com.keelim.scheme

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.keelim.common.extensions.startActivity
import com.keelim.composeutil.resource.space8
import com.keelim.composeutil.ui.theme.KeelimTheme

class IdDetailActivity : ComponentActivity() {

    private lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSavedInstanceState(savedInstanceState)
        setContent {
            KeelimTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(
                        modifier = Modifier.height(130.dp)
                    )
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = space8),
                    )
                }
            }
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EXTRA_NAME, name)
        super.onSaveInstanceState(outState)
    }

    private fun getBundle(savedInstanceState: Bundle?) = savedInstanceState ?: intent.extras

    private fun setSavedInstanceState(savedInstanceState: Bundle?) {
        val bundle = getBundle(savedInstanceState)

        bundle?.getString(EXTRA_NAME)?.let {
            name = it
        } ?: run {
            // EXTRA_NAME 정보 없음 오류로그 남기기
            // EXTRA_NAME
            finish()
        }
    }


    companion object {
        private const val QUERY_NAME = "name"
        private const val EXTRA_NAME = "EXTRA_NAME"

        fun startActivity(context: Context, name: String) {
            context.startActivity<IdDetailActivity>(
                EXTRA_NAME to name
            )
        }

        private fun getIntent(context: Context, name: String) =
            Intent(context, IdDetailActivity::class.java).apply {
                putExtra(EXTRA_NAME, name)
            }

        fun getIntent(context: Context, deepLink: Uri): Intent {
            val name = deepLink.getQueryParameter(QUERY_NAME).orEmpty()
            return getIntent(context, name)
        }
    }
}
