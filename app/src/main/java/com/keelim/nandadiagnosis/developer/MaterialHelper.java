package com.keelim.nandadiagnosis.developer;

import android.app.Activity;
import android.widget.FrameLayout;

import com.keelim.nandadiagnosis.R;
import com.vansuita.materialabout.builder.AboutBuilder;
import com.vansuita.materialabout.views.AboutView;


public class MaterialHelper {

    private Activity activity;

    private MaterialHelper(Activity activity) {
        this.activity = activity;
    }

    public static MaterialHelper with(Activity activity) {
        return new MaterialHelper(activity);
    }

    public MaterialHelper init() {
        int theme = R.style.Theme_AppCompat_DayNight;
        activity.setTheme(theme);
        return this;
    }

    public void loadAbout() {
        final FrameLayout frameLayout = activity.findViewById(R.id.about);

        AboutBuilder builder = AboutBuilder.with(activity) //Builder pattern
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
        frameLayout.addView(view);
    }
}
