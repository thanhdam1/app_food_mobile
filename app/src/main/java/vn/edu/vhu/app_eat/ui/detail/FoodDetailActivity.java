package vn.edu.vhu.app_eat.ui.detail;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;

import vn.edu.vhu.app_eat.R;
import vn.edu.vhu.app_eat.ui.cart.CartActivity;
import vn.edu.vhu.app_eat.utils.ImageLoader;

public class FoodDetailActivity extends AppCompatActivity {

    private Button btnAddToCart;
    private double price = 49000;
    private int quantity = 1;
    private boolean isWishlisted = false;
    private String foodName = "Burger Bò Phô Mai Thượng Hạng";
    private String foodImageUrl = "food_burger";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        ImageView ivFood = findViewById(R.id.ivDetailFood);
        ImageView ivWishlist = findViewById(R.id.ivWishlist);
        TextView tvName = findViewById(R.id.tvDetailName);
        TextView tvPrice = findViewById(R.id.tvDetailPrice);
        TextView tvOriginalPrice = findViewById(R.id.tvDetailOriginalPrice);
        TextView tvDesc = findViewById(R.id.tvDetailDesc);
        TextView tvQuantity = findViewById(R.id.tvQuantity);
        View btnMinus = findViewById(R.id.btnMinus);
        View btnPlus = findViewById(R.id.btnPlus);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        // Read intent extras
        Intent intent = getIntent();
        String nameExtra = intent.getStringExtra("FOOD_NAME");
        if (nameExtra != null) foodName = nameExtra;

        String desc = intent.getStringExtra("FOOD_DESC");
        if (desc == null) desc = "Thịt bò Úc nguyên chất nướng khói, phô mai cheddar tan chảy kết hợp sốt đặc biệt MamZi.";

        double rawPrice = intent.getDoubleExtra("FOOD_PRICE", 65000.0);
        double discountPrice = intent.getDoubleExtra("FOOD_DISCOUNT_PRICE", -1.0);
        if (discountPrice != -1.0) {
            price = discountPrice;
        } else {
            price = rawPrice;
        }

        String imageExtra = intent.getStringExtra("FOOD_IMAGE_URL");
        if (imageExtra != null) foodImageUrl = imageExtra;

        DecimalFormat formatter = new DecimalFormat("#,###");

        ImageLoader.loadFoodImage(this, foodImageUrl, ivFood);

        tvName.setText(foodName);
        tvDesc.setText(desc);
        tvPrice.setText(formatter.format(price) + " đ");

        if (discountPrice != -1.0) {
            tvOriginalPrice.setText(formatter.format(rawPrice) + " đ");
            tvOriginalPrice.setVisibility(View.VISIBLE);
            tvOriginalPrice.setPaintFlags(tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            tvOriginalPrice.setVisibility(View.GONE);
        }

        btnPlus.setOnClickListener(v -> {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
            updateTotalPrice();
        });

        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
                updateTotalPrice();
            }
        });

        ivWishlist.setOnClickListener(v -> {
            isWishlisted = !isWishlisted;
            if (isWishlisted) {
                ivWishlist.setColorFilter(getResources().getColor(android.R.color.holo_red_dark));
                Toast.makeText(this, "Đã thêm vào mục Yêu thích ❤️", Toast.LENGTH_SHORT).show();
            } else {
                ivWishlist.setColorFilter(null);
                Toast.makeText(this, "Đã xóa khỏi mục Yêu thích", Toast.LENGTH_SHORT).show();
            }
        });

        btnAddToCart.setOnClickListener(v -> {
            Toast.makeText(this, "Đã thêm " + quantity + " " + foodName + " vào giỏ hàng!", Toast.LENGTH_SHORT).show();
            Intent cartIntent = new Intent(FoodDetailActivity.this, CartActivity.class);
            cartIntent.putExtra("FOOD_NAME", foodName);
            cartIntent.putExtra("FOOD_PRICE", price);
            cartIntent.putExtra("FOOD_IMAGE_URL", foodImageUrl);
            cartIntent.putExtra("FOOD_QUANTITY", quantity);
            startActivity(cartIntent);
        });

        updateTotalPrice();
    }

    private void updateTotalPrice() {
        DecimalFormat formatter = new DecimalFormat("#,###");
        double total = price * quantity;
        btnAddToCart.setText("Thêm vào giỏ - " + formatter.format(total) + " đ");
    }
}
