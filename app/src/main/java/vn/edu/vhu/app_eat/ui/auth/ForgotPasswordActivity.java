package vn.edu.vhu.app_eat.ui.auth;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

import vn.edu.vhu.app_eat.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Toolbar toolbar = findViewById(R.id.toolbarForgot);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        TextInputEditText etResetEmail = findViewById(R.id.etResetEmail);
        Button btnSendOtp = findViewById(R.id.btnSendOtp);

        btnSendOtp.setOnClickListener(v -> {
            String input = etResetEmail.getText().toString().trim();
            if (input.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập Email hoặc Số điện thoại!", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Mã OTP khôi phục mật khẩu đã được gửi đến " + input, Toast.LENGTH_LONG).show();
            finish();
        });
    }
}
