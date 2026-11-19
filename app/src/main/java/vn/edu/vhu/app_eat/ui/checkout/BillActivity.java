package vn.edu.vhu.app_eat.ui.checkout;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.DecimalFormat;

import vn.edu.vhu.app_eat.R;
import vn.edu.vhu.app_eat.model.OrderReceipt;
import vn.edu.vhu.app_eat.ui.tracking.TrackingActivity;
import vn.edu.vhu.app_eat.utils.OrderManager;
import vn.edu.vhu.app_eat.view.BarcodeView;

public class BillActivity extends AppCompatActivity {

    private final DecimalFormat formatter = new DecimalFormat("#,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        Toolbar toolbar = findViewById(R.id.toolbarBill);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Get receipt from Intent or OrderManager
        OrderReceipt receipt = (OrderReceipt) getIntent().getSerializableExtra("ORDER_RECEIPT");
        if (receipt == null) {
            receipt = OrderManager.getOrCreateCurrentReceipt(this);
        }

        TextView tvBillStatusBadge = findViewById(R.id.tvBillStatusBadge);
        TextView tvBillOrderId = findViewById(R.id.tvBillOrderId);
        TextView tvBillDateTime = findViewById(R.id.tvBillDateTime);
        TextView tvBillCustomer = findViewById(R.id.tvBillCustomer);
        TextView tvBillAddress = findViewById(R.id.tvBillAddress);
        TextView tvBillPaymentMethod = findViewById(R.id.tvBillPaymentMethod);

        LinearLayout layoutBillItems = findViewById(R.id.layoutBillItems);

        TextView tvBillSubtotal = findViewById(R.id.tvBillSubtotal);
        TextView tvBillShippingFee = findViewById(R.id.tvBillShippingFee);
        TextView tvBillDiscount = findViewById(R.id.tvBillDiscount);
        TextView tvBillFinalTotal = findViewById(R.id.tvBillFinalTotal);

        BarcodeView barcodeView = findViewById(R.id.barcodeView);
        TextView tvBarcodeString = findViewById(R.id.tvBarcodeString);

        Button btnShareBill = findViewById(R.id.btnShareBill);
        Button btnTrackDelivery = findViewById(R.id.btnTrackDelivery);

        // Bind data
        tvBillStatusBadge.setText(receipt.getPaymentStatus());
        tvBillOrderId.setText("Mã HĐ: #" + receipt.getOrderId());
        if (toolbar != null) {
            toolbar.setSubtitle("Mã đơn: #" + receipt.getOrderId());
        }
        tvBillDateTime.setText("Thời gian: " + receipt.getOrderTime());
        tvBillCustomer.setText(receipt.getCustomerName() + " (" + receipt.getCustomerPhone() + ")");
        tvBillAddress.setText(receipt.getDeliveryAddress());
        tvBillPaymentMethod.setText(receipt.getPaymentMethod());

        // Add items dynamically
        layoutBillItems.removeAllViews();
        if (receipt.getItems() != null && !receipt.getItems().isEmpty()) {
            for (OrderReceipt.ReceiptItem item : receipt.getItems()) {
                LinearLayout row = new LinearLayout(this);
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setGravity(Gravity.CENTER_VERTICAL);
                row.setPadding(dpToPx(6), dpToPx(6), dpToPx(6), dpToPx(6));

                // Item Name
                TextView tvName = new TextView(this);
                LinearLayout.LayoutParams pName = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                tvName.setLayoutParams(pName);
                tvName.setText(item.getFoodName());
                tvName.setTextSize(13f);
                tvName.setTextColor(0xFF2D3436);
                row.addView(tvName);

                // Qty
                TextView tvQty = new TextView(this);
                LinearLayout.LayoutParams pQty = new LinearLayout.LayoutParams(dpToPx(36), LinearLayout.LayoutParams.WRAP_CONTENT);
                tvQty.setLayoutParams(pQty);
                tvQty.setGravity(Gravity.CENTER);
                tvQty.setText("x" + item.getQuantity());
                tvQty.setTextSize(13f);
                tvQty.setTypeface(null, Typeface.BOLD);
                tvQty.setTextColor(0xFF00B14F);
                row.addView(tvQty);

                // Unit Price
                TextView tvUnit = new TextView(this);
                LinearLayout.LayoutParams pUnit = new LinearLayout.LayoutParams(dpToPx(75), LinearLayout.LayoutParams.WRAP_CONTENT);
                tvUnit.setLayoutParams(pUnit);
                tvUnit.setGravity(Gravity.END);
                tvUnit.setText(formatter.format(item.getUnitPrice()) + "đ");
                tvUnit.setTextSize(12f);
                tvUnit.setTextColor(0xFF636E72);
                row.addView(tvUnit);

                // Total
                TextView tvTotal = new TextView(this);
                LinearLayout.LayoutParams pTotal = new LinearLayout.LayoutParams(dpToPx(85), LinearLayout.LayoutParams.WRAP_CONTENT);
                tvTotal.setLayoutParams(pTotal);
                tvTotal.setGravity(Gravity.END);
                tvTotal.setText(formatter.format(item.getItemTotal()) + "đ");
                tvTotal.setTextSize(13f);
                tvTotal.setTypeface(null, Typeface.BOLD);
                tvTotal.setTextColor(0xFF2D3436);
                row.addView(tvTotal);

                layoutBillItems.addView(row);
            }
        }

        // Totals
        tvBillSubtotal.setText(formatter.format(receipt.getSubtotal()) + " đ");
        tvBillShippingFee.setText(formatter.format(receipt.getShippingFee()) + " đ");
        tvBillDiscount.setText("-" + formatter.format(receipt.getDiscount()) + " đ");
        tvBillFinalTotal.setText(formatter.format(receipt.getFinalTotal()) + " đ");

        // Barcode
        String code = receipt.getOrderId().replaceAll("[^A-Za-z0-9]", "");
        barcodeView.setBarcodeText(code);
        tvBarcodeString.setText("* " + receipt.getOrderId() + " *");

        // Listeners
        OrderReceipt finalReceipt = receipt;
        btnShareBill.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareBody = "🧾 [HÓA ĐƠN MAMZI]\n"
                    + "Mã đơn: #" + finalReceipt.getOrderId() + "\n"
                    + "Khách hàng: " + finalReceipt.getCustomerName() + "\n"
                    + "Tổng thanh toán: " + formatter.format(finalReceipt.getFinalTotal()) + " đ\n"
                    + "Giao đến: " + finalReceipt.getDeliveryAddress() + "\n"
                    + "Cảm ơn quý khách đã đặt món tại MamZi!";
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Hóa Đơn Thanh Toán #" + finalReceipt.getOrderId());
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(shareIntent, "Chia sẻ hóa đơn qua"));
        });

        btnTrackDelivery.setOnClickListener(v -> {
            Intent trackIntent = new Intent(BillActivity.this, TrackingActivity.class);
            startActivity(trackIntent);
            finish();
        });
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
