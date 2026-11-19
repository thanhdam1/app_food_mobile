package vn.edu.vhu.app_eat.ui.tracking;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import vn.edu.vhu.app_eat.R;
import vn.edu.vhu.app_eat.adapter.ChatAdapter;
import vn.edu.vhu.app_eat.ui.checkout.BillActivity;
import vn.edu.vhu.app_eat.model.ChatMessage;
import vn.edu.vhu.app_eat.utils.UserManager;

public class TrackingActivity extends AppCompatActivity {

    private SimulatedMapView simulatedMapView;
    private TextView tvTrackingETA;
    private TextView tvTrackingDistance;
    private TextView tvTrackingSpeed;
    private TextView tvCurrentStreet;
    private TextView tvTrackingMotionStatus;
    private TextView tvLiveTelemetry;

    private final List<ChatMessage> chatMessages = new ArrayList<>();
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        Toolbar toolbar = findViewById(R.id.toolbarTracking);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        simulatedMapView = findViewById(R.id.simulatedMapView);
        tvTrackingETA = findViewById(R.id.tvTrackingETA);
        tvTrackingDistance = findViewById(R.id.tvTrackingDistance);
        tvTrackingSpeed = findViewById(R.id.tvTrackingSpeed);
        tvCurrentStreet = findViewById(R.id.tvCurrentStreet);
        tvTrackingMotionStatus = findViewById(R.id.tvTrackingMotionStatus);
        tvLiveTelemetry = findViewById(R.id.tvLiveTelemetry);

        Button btnReplayMap = findViewById(R.id.btnReplayMap);
        if (btnReplayMap != null) {
            btnReplayMap.setOnClickListener(v -> {
                if (simulatedMapView != null) {
                    simulatedMapView.restartSimulation();
                    Toast.makeText(this, "Đang khởi động lại mô phỏng GPS lộ trình!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        Button btnChangePinLocation = findViewById(R.id.btnChangePinLocation);
        if (btnChangePinLocation != null) {
            btnChangePinLocation.setOnClickListener(v ->
                    startActivity(new Intent(TrackingActivity.this, LocationPickerActivity.class))
            );
        }

        Button btnViewBill = findViewById(R.id.btnViewBill);
        if (btnViewBill != null) {
            btnViewBill.setOnClickListener(v ->
                    startActivity(new Intent(TrackingActivity.this, BillActivity.class))
            );
        }

        if (simulatedMapView != null) {
            simulatedMapView.setOnTrackingUpdateListener((progress, remainingTime, remainingDistance, currentStreet, speedKmH, motionStatus, liveTime) -> {
                runOnUiThread(() -> {
                    if (tvTrackingETA != null) tvTrackingETA.setText(remainingTime);
                    if (tvTrackingDistance != null) tvTrackingDistance.setText(remainingDistance);
                    if (tvTrackingSpeed != null) tvTrackingSpeed.setText(speedKmH + " km/h");
                    if (tvCurrentStreet != null) tvCurrentStreet.setText(currentStreet);
                    if (tvTrackingMotionStatus != null) tvTrackingMotionStatus.setText(motionStatus);
                    if (tvLiveTelemetry != null) tvLiveTelemetry.setText("🕒 GPS: " + liveTime + " • Sai số: 2m");
                });
            });

            simulatedMapView.setOnDestinationPinChangedListener((newAddress, pinX, pinY) -> {
                runOnUiThread(() ->
                        Toast.makeText(this, "📍 Đã ghim điểm giao mới: " + newAddress, Toast.LENGTH_SHORT).show()
                );
            });
        }

        ImageView ivDriverAvatar = findViewById(R.id.ivDriverAvatar);
        if (ivDriverAvatar != null) {
            try {
                Glide.with(this)
                        .load("https://images.unsplash.com/photo-1535713875002-d1d0cf377fde")
                        .circleCrop()
                        .into(ivDriverAvatar);
            } catch (Exception ignored) {}
        }

        MaterialButton btnCallDriver = findViewById(R.id.btnCallDriver);
        if (btnCallDriver != null) {
            btnCallDriver.setOnClickListener(v ->
                    Toast.makeText(this, "📞 Đang gọi cho tài xế Trần Văn Tài (0918.888.888)...", Toast.LENGTH_SHORT).show()
            );
        }

        MaterialButton btnChatDriver = findViewById(R.id.btnChatDriver);
        if (btnChatDriver != null) {
            btnChatDriver.setOnClickListener(v -> showDriverChatDialog());
        }

        // Initialize chat history with driver greeting
        initChatMessages();
    }

    private void initChatMessages() {
        if (chatMessages.isEmpty()) {
            chatMessages.add(new ChatMessage("Chào bạn! Mình là tài xế Tài từ MamZi, mình đang giao đơn hàng đến địa chỉ của bạn nhé 🛵", false, "11:40"));
            chatMessages.add(new ChatMessage("Dự kiến tầm 10-15 phút nữa mình sẽ tới nơi ạ!", false, "11:41"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (simulatedMapView != null) {
            float px = UserManager.getUserPinX(this);
            float py = UserManager.getUserPinY(this);
            String addr = UserManager.getUserAddress(this);
            simulatedMapView.updateDestinationPin(px, py, addr);
        }
    }

    private void showDriverChatDialog() {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_driver_chat);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }

        ImageView ivDriverAvt = dialog.findViewById(R.id.ivChatHeaderDriverAvt);
        if (ivDriverAvt != null) {
            try {
                Glide.with(this)
                        .load("https://images.unsplash.com/photo-1535713875002-d1d0cf377fde")
                        .circleCrop()
                        .into(ivDriverAvt);
            } catch (Exception ignored) {}
        }

        ImageView btnClose = dialog.findViewById(R.id.btnCloseChat);
        if (btnClose != null) {
            btnClose.setOnClickListener(v -> dialog.dismiss());
        }

        MaterialButton btnCall = dialog.findViewById(R.id.btnChatCallDriver);
        if (btnCall != null) {
            btnCall.setOnClickListener(v ->
                    Toast.makeText(this, "📞 Đang gọi cho tài xế Trần Văn Tài (0918.888.888)...", Toast.LENGTH_SHORT).show()
            );
        }

        RecyclerView rvChat = dialog.findViewById(R.id.rvChatMessages);
        chatAdapter = new ChatAdapter(chatMessages);
        if (rvChat != null) {
            LinearLayoutManager lm = new LinearLayoutManager(this);
            lm.setStackFromEnd(true);
            rvChat.setLayoutManager(lm);
            rvChat.setAdapter(chatAdapter);
            rvChat.scrollToPosition(chatMessages.size() - 1);
        }

        EditText etMessage = dialog.findViewById(R.id.etChatMessage);
        MaterialButton btnSend = dialog.findViewById(R.id.btnSendMessage);

        Runnable sendAction = () -> {
            if (etMessage == null) return;
            String text = etMessage.getText().toString().trim();
            if (text.isEmpty()) return;

            sendMessage(text, rvChat);
            etMessage.setText("");
        };

        if (btnSend != null) {
            btnSend.setOnClickListener(v -> sendAction.run());
        }

        if (etMessage != null) {
            etMessage.setOnEditorActionListener((v, actionId, event) -> {
                sendAction.run();
                return true;
            });
        }

        // Quick Suggestion Chips
        TextView chip1 = dialog.findViewById(R.id.chip1);
        TextView chip2 = dialog.findViewById(R.id.chip2);
        TextView chip3 = dialog.findViewById(R.id.chip3);
        TextView chip4 = dialog.findViewById(R.id.chip4);

        if (chip1 != null) chip1.setOnClickListener(v -> sendMessage("🛵 Đến nơi alo mình nhé!", rvChat));
        if (chip2 != null) chip2.setOnClickListener(v -> sendMessage("🚪 Để trước cửa phòng giúp mình nhé", rvChat));
        if (chip3 != null) chip3.setOnClickListener(v -> sendMessage("🌶️ Cho mình xin thêm tương ớt nha bác tài", rvChat));
        if (chip4 != null) chip4.setOnClickListener(v -> sendMessage("💵 Mình trả tiền mặt nhé bác tài", rvChat));

        dialog.show();
    }

    private void sendMessage(String text, RecyclerView rvChat) {
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        chatMessages.add(new ChatMessage(text, true, currentTime));
        if (chatAdapter != null) {
            chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        }
        if (rvChat != null) {
            rvChat.scrollToPosition(chatMessages.size() - 1);
        }

        // Driver automated reply after 1.5 seconds
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (isFinishing() || isDestroyed()) return;

            String replyText;
            String lower = text.toLowerCase();
            if (lower.contains("alo") || lower.contains("gọi")) {
                replyText = "Dạ vâng, gần tới nơi em sẽ nháy máy trước cho anh/chị liền ạ!";
            } else if (lower.contains("cửa")) {
                replyText = "Dạ vâng em nhớ rồi, em sẽ để trước cửa phòng cẩn thận cho anh/chị!";
            } else if (lower.contains("tiền")) {
                replyText = "Dạ được chứ anh/chị, em có sẵn tiền lẻ thối ạ!";
            } else if (lower.contains("tương") || lower.contains("ớt") || lower.contains("muỗng")) {
                replyText = "Dạ em đã nhờ quán bỏ thêm tương ớt và muỗng đầy đủ trong bọc rồi nha!";
            } else {
                replyText = "Dạ em nhận được tin nhắn rồi ạ, em đang lái xe tới ngay đây ạ!";
            }

            String replyTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
            chatMessages.add(new ChatMessage(replyText, false, replyTime));
            if (chatAdapter != null) {
                chatAdapter.notifyItemInserted(chatMessages.size() - 1);
            }
            if (rvChat != null) {
                rvChat.scrollToPosition(chatMessages.size() - 1);
            }
        }, 1500);
    }
}
