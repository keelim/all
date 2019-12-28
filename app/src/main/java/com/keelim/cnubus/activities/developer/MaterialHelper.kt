package com.keelim.cnubus.activities.developer

import android.app.Activity
import android.widget.FrameLayout
import com.keelim.cnubus.R
import com.vansuita.materialabout.builder.AboutBuilder

class MaterialHelper private constructor(private val activity: Activity) {
    fun init(): MaterialHelper {
        val theme = R.style.Theme_AppCompat_DayNight
        activity.setTheme(theme)
        return this
    }

    fun loadAbout() {
        val flHolder = activity.findViewById<FrameLayout>(R.id.about)
        val builder = AboutBuilder.with(activity) //Builder pattern
                .setAppIcon(R.mipmap.ic_launcher)
                .setAppName(R.string.app_name)
                .setPhoto(R.mipmap.profile_picture)
                .setCover(R.mipmap.profile_cover)
                .setLinksAnimated(true)
                .setDividerDashGap(13)
                .setName("Keel_im")
                .setSubTitle("android Developer")
                .setLinksColumnsCount(4)
                .setBrief("Mobile developer")
                .addGitHubLink("keelim")
                .addEmailLink("kimh00335@gmail.com")
                .addFiveStarsAction()
                .addMoreFromMeAction("keel_im")
                .setVersionNameAsAppSubTitle()
                .addShareAction(R.string.app_name)
                .addUpdateAction()
                .setActionsColumnsCount(4)
                .addFeedbackAction("kimh00335@gmail.com")
        flHolder.addView(builder.build())
    }

    companion object {
        fun with(activity: Activity): MaterialHelper {
            return MaterialHelper(activity)
        }
    }

}