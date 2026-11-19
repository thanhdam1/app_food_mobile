package vn.edu.vhu.app_eat.ui.category;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import vn.edu.vhu.app_eat.R;
import vn.edu.vhu.app_eat.adapter.FoodAdapter;
import vn.edu.vhu.app_eat.model.Food;
import vn.edu.vhu.app_eat.ui.detail.FoodDetailActivity;

public class CategoryFoodsActivity extends AppCompatActivity {

    private List<Food> allFoods;
    private RecyclerView rvCategoryFoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_foods);

        String categoryName = getIntent().getStringExtra("CATEGORY_NAME");
        if (categoryName == null) categoryName = "Danh Mục Món Ăn";

        Toolbar toolbar = findViewById(R.id.toolbarCategory);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(categoryName);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        rvCategoryFoods = findViewById(R.id.rvCategoryFoods);
        ChipGroup chipGroupFilter = findViewById(R.id.chipGroupFilter);

        // Comprehensive & perfectly matched food dataset by category
        allFoods = new ArrayList<>();
        if (categoryName.contains("Trà Sữa")) {
            allFoods.add(new Food(3, "Trà Sữa Trân Châu Hoàng Gia", "Hồng trà sữa đậm vị kết hợp trân châu dai giòn thơm ngon", 45000.0, 35000.0, "food_tra_sua", 4.9, 3200, true, true));
            allFoods.add(new Food(6, "Hồng Trà Kem Cheese", "Hồng trà thơm mát phủ lớp kem cheese sánh mịn béo ngậy", 48000.0, null, "food_hong_tra", 4.8, 1450, false, true));
            allFoods.add(new Food(7, "Sữa Tươi Trân Châu Đường Đen", "Sữa tươi thanh trùng kết hợp trân châu sốt đường đen đậm vị", 50000.0, 42000.0, "food_tra_sua", 4.9, 2100, true, true));
        } else if (categoryName.contains("Fast Food")) {
            allFoods.add(new Food(1, "Burger Bò Phô Mai Thượng Hạng", "Thịt bò Úc nguyên chất nướng khói, phô mai cheddar tan chảy", 65000.0, 49000.0, "food_burger", 4.9, 1280, true, true));
            allFoods.add(new Food(2, "Gà Rán Sốt Cay Hàn Quốc", "Gà giòn rụm phủ sốt cay ngọt chuẩn vị Seoul", 75000.0, null, "food_ga_ran", 4.8, 950, false, true));
            allFoods.add(new Food(8, "Pizza Phô Mai Hải Sản", "Pizza đế mỏng topping tôm mực phô mai mozzarella ngập tràn", 129000.0, 99000.0, "food_pizza", 4.9, 850, true, true));
        } else if (categoryName.contains("Cơm Tấm")) {
            allFoods.add(new Food(4, "Cơm Tấm Sườn Bì Chả Đặc Biệt", "Sườn nướng mật ong thơm lừng, chả trứng, bì giòn đậm đà", 55000.0, null, "food_com_tam", 4.9, 2100, false, true));
            allFoods.add(new Food(9, "Cơm Gà Xối Mỡ Giòn Rụm", "Đùi gà góc tư chiên giòn ăn kèm cơm chiên đỏ và dưa leo cà chua", 52000.0, 45000.0, "food_com_ga", 4.8, 1600, true, true));
            allFoods.add(new Food(11, "Cơm Bò Lúc Lắc Hạt Điều", "Thịt bò mềm mọng thơm đậm đà kèm cơm hạt dẻo thơm nức mũi", 68000.0, 58000.0, "food_bo_luc_lac", 4.9, 920, true, false));
        } else {
            // Đồ Ăn Vặt & Default
            allFoods.add(new Food(5, "Tokbokki Phô Mai Hàn Quốc", "Bánh gạo dẻo dai đẫm sốt tương ớt Gochujang phủ phô mai béo ngậy", 50000.0, 39000.0, "food_tokbokki", 4.7, 740, true, false));
            allFoods.add(new Food(10, "Bánh Tráng Trộn Sốt Bơ", "Bánh tráng dẻo dai trộn bò khô, trứng cút, tép khô, xoài xanh sốt bơ béo", 25000.0, 19000.0, "food_banh_trang", 4.9, 4300, true, true));
            allFoods.add(new Food(12, "Khoai Tây Chiên Lắc Phô Mai", "Khoai tây chiên giòn rụm phủ bột phô mai mặn ngọt thơm lừng", 35000.0, 29000.0, "food_khoai_tay", 4.7, 1850, true, false));
        }

        FoodAdapter adapter = new FoodAdapter(allFoods, food -> {
            Intent intent = new Intent(CategoryFoodsActivity.this, FoodDetailActivity.class);
            intent.putExtra("FOOD_NAME", food.getName());
            intent.putExtra("FOOD_DESC", food.getDescription());
            intent.putExtra("FOOD_PRICE", food.getPrice());
            if (food.getDiscountPrice() != null) {
                intent.putExtra("FOOD_DISCOUNT_PRICE", food.getDiscountPrice());
            }
            intent.putExtra("FOOD_IMAGE_URL", food.getImageUrl());
            intent.putExtra("FOOD_RATING", food.getRating());
            startActivity(intent);
        });

        rvCategoryFoods.setLayoutManager(new LinearLayoutManager(this));
        rvCategoryFoods.setAdapter(adapter);

        chipGroupFilter.setOnCheckedChangeListener((group, checkedId) -> {
            List<Food> filteredList = new ArrayList<>();
            if (checkedId == R.id.chipAll) {
                filteredList.addAll(allFoods);
            } else if (checkedId == R.id.chipCheap) {
                for (Food f : allFoods) {
                    double effectivePrice = f.getDiscountPrice() != null ? f.getDiscountPrice() : f.getPrice();
                    if (effectivePrice < 50000) filteredList.add(f);
                }
            } else if (checkedId == R.id.chipTopRated) {
                for (Food f : allFoods) {
                    if (f.getRating() >= 4.8) filteredList.add(f);
                }
            } else if (checkedId == R.id.chipFlashSale) {
                for (Food f : allFoods) {
                    if (f.isFlashSale()) filteredList.add(f);
                }
            } else {
                filteredList.addAll(allFoods);
            }
            
            FoodAdapter filteredAdapter = new FoodAdapter(filteredList, food -> {
                Intent intent = new Intent(CategoryFoodsActivity.this, FoodDetailActivity.class);
                intent.putExtra("FOOD_NAME", food.getName());
                intent.putExtra("FOOD_DESC", food.getDescription());
                intent.putExtra("FOOD_PRICE", food.getPrice());
                if (food.getDiscountPrice() != null) {
                    intent.putExtra("FOOD_DISCOUNT_PRICE", food.getDiscountPrice());
                }
                intent.putExtra("FOOD_IMAGE_URL", food.getImageUrl());
                intent.putExtra("FOOD_RATING", food.getRating());
                startActivity(intent);
            });
            rvCategoryFoods.setAdapter(filteredAdapter);
        });
    }
}
