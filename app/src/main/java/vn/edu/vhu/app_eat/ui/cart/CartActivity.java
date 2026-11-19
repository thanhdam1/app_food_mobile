package vn.edu.vhu.app_eat.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import vn.edu.vhu.app_eat.R;
import vn.edu.vhu.app_eat.adapter.CartAdapter;
import vn.edu.vhu.app_eat.model.CartItem;
import vn.edu.vhu.app_eat.ui.checkout.CheckoutActivity;

public class CartActivity extends AppCompatActivity {

    private final List<CartItem> cartItems = new ArrayList<>();
    private CartAdapter adapter;
    private TextView tvCartTotal;
    private Button btnCheckout;
    private final DecimalFormat formatter = new DecimalFormat("#,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = findViewById(R.id.toolbarCart);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        RecyclerView rvCartItems = findViewById(R.id.rvCartItems);
        tvCartTotal = findViewById(R.id.tvCartTotal);
        btnCheckout = findViewById(R.id.btnCheckout);

        // Read food from Intent if added from FoodDetailActivity
        Intent intent = getIntent();
        String foodName = intent.getStringExtra("FOOD_NAME");
        double foodPrice = intent.getDoubleExtra("FOOD_PRICE", 49000.0);
        String foodImage = intent.getStringExtra("FOOD_IMAGE_URL");
        int foodQty = intent.getIntExtra("FOOD_QUANTITY", 1);

        if (foodImage == null) foodImage = "food_burger";
        if (foodName == null) foodName = "Burger Bò Phô Mai Thượng Hạng";

        cartItems.add(new CartItem(foodName, foodPrice, foodImage, foodQty));

        // Add a second popular item if this is the default burger
        if ("Burger Bò Phô Mai Thượng Hạng".equals(foodName)) {
            cartItems.add(new CartItem("Trà Sữa Trân Châu Hoàng Gia", 35000.0, "food_tra_sua", 1));
        }

        adapter = new CartAdapter(cartItems, this::updateCartTotal);
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        rvCartItems.setAdapter(adapter);

        updateCartTotal();

        btnCheckout.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Giỏ hàng đang trống!", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent checkoutIntent = new Intent(CartActivity.this, CheckoutActivity.class);
            startActivity(checkoutIntent);
        });
    }

    private void updateCartTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        tvCartTotal.setText(formatter.format(total) + " đ");

        if (cartItems.isEmpty()) {
            btnCheckout.setEnabled(false);
            btnCheckout.setAlpha(0.5f);
            btnCheckout.setText("Giỏ Hàng Trống");
        } else {
            btnCheckout.setEnabled(true);
            btnCheckout.setAlpha(1.0f);
            btnCheckout.setText("Tiến Hành Thanh Toán (" + formatter.format(total) + " đ)");
        }
    }
}
