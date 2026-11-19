package vn.edu.vhu.app_eat.ui.splash;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import vn.edu.vhu.app_eat.R;
import vn.edu.vhu.app_eat.ui.auth.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_splash);

            ImageView ivLogo = findViewById(R.id.ivAppLogo);
            if (ivLogo != null) {
                try {
                    Glide.with(this)
                            .load(R.drawable.logo)
                            .centerCrop()
                            .into(ivLogo);
                } catch (Exception ignored) {}
            }

            ImageView ivShipper = findViewById(R.id.ivShipper);
            if (ivShipper != null) {
                int screenWidth = getResources().getDisplayMetrics().widthPixels;
                ObjectAnimator animator = ObjectAnimator.ofFloat(ivShipper, "translationX", -150f, screenWidth + 150f);
                animator.setDuration(3200);
                animator.start();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (!isFinishing() && !isDestroyed()) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3200);
    }
}
