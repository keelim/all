package com.keelim.scheme

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.core.app.TaskStackBuilder

class SchemeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleDeepLink()
    }

    private fun handleDeepLink() {
        val deepLinkUri = intent.data ?: run {
            // 딥링크 URI가 없는 경우, 기본 메인 화면으로 이동하고 액티비티를 종료합니다.
            startActivity(DeepLinkInfo.getMainIntent(this))
            finish()
            return
        }

        val deepLinkInfo = DeepLinkInfo.invoke(deepLinkUri)
        val deepLinkIntent = deepLinkInfo.getIntent(this, deepLinkUri)

        if (isTaskRoot) {
            // 앱이 루트 태스크에서 실행 중인 경우, 새로운 태스크 스택을 생성합니다.
            // 이렇게 하면 딥링크로 열린 화면 위에 이전 화면들이 쌓이는 것을 방지할 수 있습니다.
            TaskStackBuilder.create(this).apply {
                if (needAddMainForParent(deepLinkIntent)) {
                    // 특정 딥링크의 경우, 부모 스택에 메인 화면을 추가합니다.
                    // 예를 들어, 상세 화면으로 바로 이동하는 딥링크의 경우,
                    // 사용자가 뒤로 가기 버튼을 눌렀을 때 메인 화면으로 이동하도록 합니다.
                    addNextIntentWithParentStack(DeepLinkInfo.getMainIntent(this@SchemeActivity))
                }
                startActivities()
            }.run {
                addNextIntent(deepLinkIntent)
            }
        } else {
            // 앱이 이미 실행 중인 경우, 현재 태스크에 딥링크 인텐트를 시작합니다.
            Log.d("[lab]", "isTaskRoot is false, starting activity directly")
            startActivity(deepLinkIntent)
        }
        finish()
    }

    private fun needAddMainForParent(intent: Intent): Boolean =
        when (intent.component?.className) {
            SchemeTestActivity::class.java.name -> false
            else -> true
        }
}
