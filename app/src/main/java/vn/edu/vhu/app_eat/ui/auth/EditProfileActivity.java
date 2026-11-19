package vn.edu.vhu.app_eat.ui.auth;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import vn.edu.vhu.app_eat.R;
import vn.edu.vhu.app_eat.utils.ImageLoader;
import vn.edu.vhu.app_eat.utils.UserManager;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView ivEditAvatar;
    private TextInputEditText etFullName, etPhone, etEmail, etAddress;
    private String selectedAvatarUrl;

    private final String[] presetAvatars = {
            "avt_1", "avt_2", "avt_3", "avt_4", "avt_5", "avt_6"
    };

    private MaterialCardView[] presetCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbar = findViewById(R.id.toolbarEditProfile);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        ivEditAvatar = findViewById(R.id.ivEditAvatar);
        etFullName = findViewById(R.id.etEditFullName);
        etPhone = findViewById(R.id.etEditPhone);
        etEmail = findViewById(R.id.etEditEmail);
        etAddress = findViewById(R.id.etEditAddress);
        Button btnSaveProfile = findViewById(R.id.btnSaveProfile);

        // Load existing profile from UserManager
        selectedAvatarUrl = UserManager.getUserAvatar(this);
        etFullName.setText(UserManager.getUserName(this));
        etPhone.setText(UserManager.getUserPhone(this));
        etEmail.setText(UserManager.getUserEmail(this));
        etAddress.setText(UserManager.getUserAddress(this));

        ImageLoader.loadAvatar(this, selectedAvatarUrl, ivEditAvatar);

        // Setup 6 preset cards
        presetCards = new MaterialCardView[]{
                findViewById(R.id.cardAvt1),
                findViewById(R.id.cardAvt2),
                findViewById(R.id.cardAvt3),
                findViewById(R.id.cardAvt4),
                findViewById(R.id.cardAvt5),
                findViewById(R.id.cardAvt6)
        };

        ImageView[] presetImageViews = new ImageView[]{
                findViewById(R.id.ivPresetAvt1),
                findViewById(R.id.ivPresetAvt2),
                findViewById(R.id.ivPresetAvt3),
                findViewById(R.id.ivPresetAvt4),
                findViewById(R.id.ivPresetAvt5),
                findViewById(R.id.ivPresetAvt6)
        };

        for (int i = 0; i < presetAvatars.length; i++) {
            final int index = i;
            if (presetImageViews[i] != null) {
                ImageLoader.loadAvatar(this, presetAvatars[i], presetImageViews[i]);
            }

            if (presetCards[i] != null) {
                presetCards[i].setOnClickListener(v -> selectAvatar(index));
            }
        }

        updatePresetBorders();

        // Tap big avatar to cycle
        ivEditAvatar.setOnClickListener(v -> {
            int currentIndex = 0;
            for (int i = 0; i < presetAvatars.length; i++) {
                if (presetAvatars[i].equals(selectedAvatarUrl)) {
                    currentIndex = i;
                    break;
                }
            }
            int nextIndex = (currentIndex + 1) % presetAvatars.length;
            selectAvatar(nextIndex);
        });

        btnSaveProfile.setOnClickListener(v -> saveProfile());
    }

    private void selectAvatar(int index) {
        selectedAvatarUrl = presetAvatars[index];
        ImageLoader.loadAvatar(this, selectedAvatarUrl, ivEditAvatar);
        updatePresetBorders();
        Toast.makeText(this, "Đã chọn ảnh đại diện mới!", Toast.LENGTH_SHORT).show();
    }

    private void updatePresetBorders() {
        if (presetCards == null) return;
        int activeGreen = getResources().getColor(R.color.mamzi_green);
        int inactiveGray = Color.parseColor("#E0E2E5");

        for (int i = 0; i < presetAvatars.length; i++) {
            if (presetCards[i] == null) continue;
            if (presetAvatars[i].equals(selectedAvatarUrl)) {
                presetCards[i].setStrokeColor(activeGreen);
                presetCards[i].setStrokeWidth(8);
            } else {
                presetCards[i].setStrokeColor(inactiveGray);
                presetCards[i].setStrokeWidth(2);
            }
        }
    }

    private void saveProfile() {
        String name = etFullName.getText() != null ? etFullName.getText().toString().trim() : "";
        String phone = etPhone.getText() != null ? etPhone.getText().toString().trim() : "";
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String address = etAddress.getText() != null ? etAddress.getText().toString().trim() : "";

        if (name.isEmpty()) {
            Toast.makeText(this, "Họ và tên không được để trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        UserManager.saveUserProfile(this, name, phone, email, address, selectedAvatarUrl);
        Toast.makeText(this, "✅ Cập nhật thông tin và Avatar thành công!", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }
}
