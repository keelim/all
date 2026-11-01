package com.keelim.lightbuildpreview

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView

/** Standalone preview app: it does not load code or configuration from the Gradle apps. */
class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(TextView(this).apply {
            text = "Google Lightbuild preview\n\nIndependent app + library project\nGradle applications remain separate."
            textSize = 20f
            gravity = Gravity.CENTER
            val inset = (24 * resources.displayMetrics.density).toInt()
            setPadding(inset, inset, inset, inset)
        })
    }
}
