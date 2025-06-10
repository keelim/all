package com.keelim.scheme

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.keelim.common.extensions.startActivity
import com.keelim.composeutil.resource.space8
import com.keelim.composeutil.ui.theme.KeelimTheme

class IdActivity : ComponentActivity() {

    private lateinit var id: String

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
                    Text(
                        text = id,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = space8),
                    )
                }
            }
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EXTRA_ID, id)
        super.onSaveInstanceState(outState)
    }

    private fun getBundle(savedInstanceState: Bundle?) = savedInstanceState ?: intent.extras

    private fun setSavedInstanceState(savedInstanceState: Bundle?) {
        val bundle = getBundle(savedInstanceState)

        bundle?.getString(EXTRA_ID)?.let {
            id = it
        } ?: run {
            // EXTRA_COUNTRY 정보 없음 오류로그 남기기
            // EXTRA_COUNTRY
            finish()
        }
    }


    companion object {
        private const val QUERY_COUNTRY = "id"
        private const val EXTRA_ID = "EXTRA_ID"

        fun startActivity(context: Context, id: String) {
            context.startActivity<IdActivity>(
                EXTRA_ID to id
            )
        }

        private fun getIntent(context: Context, id: String) =
            Intent(context, IdActivity::class.java).apply {
                putExtra(EXTRA_ID, id)
            }

        fun getIntent(context: Context, deepLink: Uri): Intent {
            val id = deepLink.getQueryParameter(QUERY_COUNTRY).orEmpty()
            return getIntent(context, id)
        }
    }
}
