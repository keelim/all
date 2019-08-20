package com.keelim.cnubus.activities;

        import android.os.Bundle;

        import androidx.appcompat.app.AppCompatActivity;
        import com.keelim.cnubus.R;
        import com.keelim.cnubus.utils.MaterialHelper;

public class DeveloperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);
        MaterialHelper.with(this).init().loadAbout();
    }
}
