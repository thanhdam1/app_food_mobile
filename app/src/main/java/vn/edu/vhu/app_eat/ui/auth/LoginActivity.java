package vn.edu.vhu.app_eat.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import vn.edu.vhu.app_eat.MainActivity;
import vn.edu.vhu.app_eat.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_login);

            ImageView ivLogo = findViewById(R.id.ivLoginLogo);
            if (ivLogo != null) {
                try {
                    Glide.with(this)
                            .load(R.drawable.logo)
                            .centerCrop()
                            .into(ivLogo);
                } catch (Exception ignored) {}
            }

            TextInputEditText etEmail = findViewById(R.id.etEmail);
            TextInputEditText etPassword = findViewById(R.id.etPassword);
            TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);
            Button btnLogin = findViewById(R.id.btnLogin);
            MaterialButton btnGoogle = findViewById(R.id.btnGoogleLogin);
            MaterialButton btnFacebook = findViewById(R.id.btnFacebookLogin);

            if (btnGoogle != null) {
                btnGoogle.setIconTint(null);
                btnGoogle.setOnClickListener(v -> {
                    Toast.makeText(this, "Đăng nhập bằng Google thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                });
            }

            if (btnFacebook != null) {
                btnFacebook.setIconTint(null);
                btnFacebook.setOnClickListener(v -> {
                    Toast.makeText(this, "Đăng nhập bằng Facebook thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                });
            }

            if (btnLogin != null) {
                btnLogin.setOnClickListener(v -> {
                    String email = (etEmail != null && etEmail.getText() != null) ? etEmail.getText().toString().trim() : "";
                    String password = (etPassword != null && etPassword.getText() != null) ? etPassword.getText().toString().trim() : "";

                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin đăng nhập!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                });
            }

            if (tvForgotPassword != null) {
                tvForgotPassword.setOnClickListener(v -> {
                    Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                    startActivity(intent);
                });
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
