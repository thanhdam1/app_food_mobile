package vn.edu.vhu.app_eat.ui.tracking;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import vn.edu.vhu.app_eat.R;
import vn.edu.vhu.app_eat.utils.UserManager;

public class LocationPickerActivity extends AppCompatActivity {

    private InteractivePinMapView interactiveMapView;
    private TextView tvPinnedAddressTitle;
    private TextView tvPinnedCoordinates;
    private EditText etAddressDetail;
    private MaterialButton btnConfirmPinLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_picker);

        Toolbar toolbar = findViewById(R.id.toolbarLocationPicker);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        interactiveMapView = findViewById(R.id.interactiveMapView);
        tvPinnedAddressTitle = findViewById(R.id.tvPinnedAddressTitle);
        tvPinnedCoordinates = findViewById(R.id.tvPinnedCoordinates);
        etAddressDetail = findViewById(R.id.etAddressDetail);
        btnConfirmPinLocation = findViewById(R.id.btnConfirmPinLocation);

        // Load existing address & pin
        String currentAddress = UserManager.getUserAddress(this);
        float pinX = UserManager.getUserPinX(this);
        float pinY = UserManager.getUserPinY(this);

        if (tvPinnedAddressTitle != null) {
            tvPinnedAddressTitle.setText(currentAddress);
        }
        updateCoordinatesDisplay(pinX, pinY);

        if (interactiveMapView != null) {
            interactiveMapView.setPinPosition(pinX, pinY);
            interactiveMapView.setOnPinMovedListener((xPercent, yPercent, address) -> {
                if (tvPinnedAddressTitle != null) {
                    tvPinnedAddressTitle.setText(address);
                }
                updateCoordinatesDisplay(xPercent, yPercent);
            });
        }

        // Quick Preset Chips
        TextView chipNguyenHue = findViewById(R.id.chipPresetNguyenHue);
        TextView chipBitexco = findViewById(R.id.chipPresetBitexco);
        TextView chipVincom = findViewById(R.id.chipPresetVincom);
        TextView chipBenThanh = findViewById(R.id.chipPresetBenThanh);
        TextView chipPark = findViewById(R.id.chipPresetPark);

        if (chipNguyenHue != null) {
            chipNguyenHue.setOnClickListener(v -> {
                if (interactiveMapView != null) interactiveMapView.setPinPosition(0.78f, 0.75f);
            });
        }
        if (chipBitexco != null) {
            chipBitexco.setOnClickListener(v -> {
                if (interactiveMapView != null) interactiveMapView.setPinPosition(0.72f, 0.58f);
            });
        }
        if (chipVincom != null) {
            chipVincom.setOnClickListener(v -> {
                if (interactiveMapView != null) interactiveMapView.setPinPosition(0.80f, 0.26f);
            });
        }
        if (chipBenThanh != null) {
            chipBenThanh.setOnClickListener(v -> {
                if (interactiveMapView != null) interactiveMapView.setPinPosition(0.22f, 0.70f);
            });
        }
        if (chipPark != null) {
            chipPark.setOnClickListener(v -> {
                if (interactiveMapView != null) interactiveMapView.setPinPosition(0.21f, 0.48f);
            });
        }

        FloatingActionButton fabMyLocation = findViewById(R.id.fabMyLocation);
        if (fabMyLocation != null) {
            fabMyLocation.setOnClickListener(v -> {
                if (interactiveMapView != null) {
                    interactiveMapView.setPinPosition(0.78f, 0.82f);
                    Toast.makeText(this, "📍 Đã định vị vị trí hiện tại!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (btnConfirmPinLocation != null) {
            btnConfirmPinLocation.setOnClickListener(v -> {
                String baseAddress = interactiveMapView != null ? interactiveMapView.getCurrentAddress() : currentAddress;
                String detail = etAddressDetail != null ? etAddressDetail.getText().toString().trim() : "";
                String finalAddress = detail.isEmpty() ? baseAddress : detail + ", " + baseAddress;

                float finalX = interactiveMapView != null ? interactiveMapView.getPinX() : pinX;
                float finalY = interactiveMapView != null ? interactiveMapView.getPinY() : pinY;

                UserManager.setUserAddress(this, finalAddress, finalX, finalY);

                Toast.makeText(this, "📍 Đã lưu và ghim địa chỉ giao hàng mới!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            });
        }
    }

    private void updateCoordinatesDisplay(float x, float y) {
        if (tvPinnedCoordinates != null) {
            double lat = 10.7700 + (1f - y) * 0.015;
            double lng = 106.6950 + x * 0.018;
            tvPinnedCoordinates.setText(String.format("Tọa độ GPS: %.4f° N, %.4f° E • Quận 1", lat, lng));
        }
    }
}