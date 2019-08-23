package com.keelim.cnubus.activities;

import android.app.Activity;
import android.widget.FrameLayout;

import com.keelim.cnubus.R;
import com.vansuita.materialabout.builder.AboutBuilder;
import com.vansuita.materialabout.views.AboutView;

public class MaterialHelper {

    private Activity activity;
    private int theme = R.style.Theme_AppCompat_DayNight;

    private MaterialHelper(Activity activity) {
        this.activity = activity;
    }

    public static MaterialHelper with(Activity activity) {
        return new MaterialHelper(activity);
    }

    public MaterialHelper init() {
        activity.setTheme(theme);
        return this;
    }

    public void loadAbout() {
        final FrameLayout flHolder = activity.findViewById(R.id.about);

        AboutBuilder builder = AboutBuilder.with(activity)
                .setAppIcon(R.mipmap.ic_launcher)
                .setAppName(R.string.app_name)
                .setPhoto(R.mipmap.profile_picture)
                .setCover(R.mipmap.profile_cover)
                .setLinksAnimated(true)
                .setDividerDashGap(13)
                .setName("Keelim")
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
                .addFeedbackAction("kimh00335@gmail.com");

        AboutView view = builder.build();

        flHolder.addView(view);
    }


}
