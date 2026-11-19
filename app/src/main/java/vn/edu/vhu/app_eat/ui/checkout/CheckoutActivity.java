package vn.edu.vhu.app_eat.ui.checkout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import vn.edu.vhu.app_eat.R;
import vn.edu.vhu.app_eat.model.OrderReceipt;
import vn.edu.vhu.app_eat.ui.tracking.LocationPickerActivity;
import vn.edu.vhu.app_eat.utils.OrderManager;
import vn.edu.vhu.app_eat.utils.UserManager;

public class CheckoutActivity extends AppCompatActivity {

    private TextView tvCheckoutCustomer;
    private TextView tvCheckoutAddress;
    private TextView tvCheckoutSubtotal;
    private TextView tvFinalAmount;
    private RadioButton rbCod, rbBanking, rbMomo;
    private Button btnPlaceOrder;

    private double subtotal = 98000.0;
    private double shippingFee = 15000.0;
    private double discount = 15000.0;
    private double finalTotal = 98000.0;

    private final DecimalFormat formatter = new DecimalFormat("#,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Toolbar toolbar = findViewById(R.id.toolbarCheckout);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        tvCheckoutCustomer = findViewById(R.id.tvCheckoutCustomer);
        tvCheckoutAddress = findViewById(R.id.tvCheckoutAddress);
        tvCheckoutSubtotal = findViewById(R.id.tvCheckoutSubtotal);
        tvFinalAmount = findViewById(R.id.tvFinalAmount);

        rbCod = findViewById(R.id.rbCod);
        rbBanking = findViewById(R.id.rbBanking);
        rbMomo = findViewById(R.id.rbMomo);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

        findViewById(R.id.cardCheckoutAddress).setOnClickListener(v ->
                startActivity(new Intent(CheckoutActivity.this, LocationPickerActivity.class))
        );

        loadUserData();

        // Calculate total
        finalTotal = subtotal + shippingFee - discount;
        tvCheckoutSubtotal.setText(formatter.format(subtotal) + " đ");
        tvFinalAmount.setText(formatter.format(finalTotal) + " đ");
        btnPlaceOrder.setText("Xác Nhận & Xuất Hóa Đơn (" + formatter.format(finalTotal) + " đ)");

        btnPlaceOrder.setOnClickListener(v -> placeOrderAndShowBill());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();
    }

    private void loadUserData() {
        if (tvCheckoutCustomer != null) {
            tvCheckoutCustomer.setText(UserManager.getUserName(this) + " (" + UserManager.getUserPhone(this) + ")");
        }
        if (tvCheckoutAddress != null) {
            tvCheckoutAddress.setText(UserManager.getUserAddress(this));
        }
    }

    private void placeOrderAndShowBill() {
        String paymentMethod = "Tiền mặt khi nhận hàng (COD)";
        String paymentStatus = "🟡 Chờ thu tiền khi giao (COD)";

        if (rbBanking != null && rbBanking.isChecked()) {
            paymentMethod = "Chuyển khoản Ngân hàng (QR Code)";
            paymentStatus = "🟢 Đã thanh toán Chuyển Khoản QR";
        } else if (rbMomo != null && rbMomo.isChecked()) {
            paymentMethod = "Ví điện tử MoMo";
            paymentStatus = "🟢 Đã thanh toán qua Ví MoMo";
        }

        // Generate Order Receipt
        OrderReceipt receipt = new OrderReceipt();
        receipt.setOrderId("MZ-" + (100000 + new Random().nextInt(900000)));

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy", Locale.getDefault());
        receipt.setOrderTime(sdf.format(new Date()));

        receipt.setCustomerName(UserManager.getUserName(this));
        receipt.setCustomerPhone(UserManager.getUserPhone(this));
        receipt.setDeliveryAddress(UserManager.getUserAddress(this));

        receipt.setPaymentMethod(paymentMethod);
        receipt.setPaymentStatus(paymentStatus);

        receipt.setSubtotal(subtotal);
        receipt.setShippingFee(shippingFee);
        receipt.setDiscount(discount);
        receipt.setFinalTotal(finalTotal);

        // Add items from active order
        receipt.addItem(new OrderReceipt.ReceiptItem("Burger Bò Phô Mai Thượng Hạng", 2, 49000.0));

        // Save active receipt
        OrderManager.setCurrentReceipt(receipt);

        // Award bonus loyalty points
        UserManager.addPoints(this, 25);

        Toast.makeText(this, "🎉 Đặt hàng thành công! Đang xuất hóa đơn...", Toast.LENGTH_SHORT).show();

        // Launch BillActivity
        Intent intent = new Intent(CheckoutActivity.this, BillActivity.class);
        intent.putExtra("ORDER_RECEIPT", receipt);
        startActivity(intent);
        finish();
    }
}
