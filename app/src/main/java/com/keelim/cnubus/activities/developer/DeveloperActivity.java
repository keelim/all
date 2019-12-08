package com.keelim.cnubus.activities.developer;

        import android.os.Bundle;
        import android.widget.Toast;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.databinding.DataBindingUtil;

        import com.keelim.cnubus.R;
        import com.keelim.cnubus.databinding.ActivityDeveloperBinding;

public class DeveloperActivity extends AppCompatActivity {
    ActivityDeveloperBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_developer);
        MaterialHelper.with(this).init().loadAbout();
        Toast.makeText(this, "개발자 관련 화면 입니다.", Toast.LENGTH_SHORT).show();
    }
}
