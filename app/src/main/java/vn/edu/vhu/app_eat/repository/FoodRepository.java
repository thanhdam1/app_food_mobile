package vn.edu.vhu.app_eat.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.vhu.app_eat.model.Category;
import vn.edu.vhu.app_eat.model.Food;
import vn.edu.vhu.app_eat.network.ApiClient;

public class FoodRepository {

    public void fetchCategories(MutableLiveData<List<Category>> categoriesLiveData) {
        ApiClient.getApiService().getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoriesLiveData.setValue(response.body());
                } else {
                    categoriesLiveData.setValue(getMockCategories());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {
                categoriesLiveData.setValue(getMockCategories());
            }
        });
    }

    public void fetchFlashSales(MutableLiveData<List<Food>> flashSalesLiveData) {
        ApiClient.getApiService().getFlashSaleFoods().enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(@NonNull Call<List<Food>> call, @NonNull Response<List<Food>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    flashSalesLiveData.setValue(response.body());
                } else {
                    flashSalesLiveData.setValue(getMockFlashSales());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Food>> call, @NonNull Throwable t) {
                flashSalesLiveData.setValue(getMockFlashSales());
            }
        });
    }

    private List<Category> getMockCategories() {
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category(1, "Fast Food", "https://cdn-icons-png.flaticon.com/512/1046/1046784.png"));
        categoryList.add(new Category(2, "Trà Sữa", "https://cdn-icons-png.flaticon.com/512/920/920534.png"));
        categoryList.add(new Category(3, "Cơm Tấm", "https://cdn-icons-png.flaticon.com/512/3076/3076137.png"));
        categoryList.add(new Category(4, "Đồ Ăn Vặt", "https://cdn-icons-png.flaticon.com/512/3132/3132693.png"));
        return categoryList;
    }

    private List<Food> getMockFlashSales() {
        List<Food> flashSaleList = new ArrayList<>();
        flashSaleList.add(new Food(1, "Burger Bò Phô Mai", "Thịt bò Úc nướng khói", 65000.0, 49000.0, "food_burger", 4.9, 1280, true, true));
        flashSaleList.add(new Food(2, "Trà Sữa Trân Châu", "Hồng trà sữa trân châu", 45000.0, 35000.0, "food_tra_sua", 4.9, 3200, true, true));
        flashSaleList.add(new Food(3, "Tokbokki Phô Mai", "Bánh gạo sốt cay", 50000.0, 39000.0, "food_tokbokki", 4.6, 740, true, false));
        return flashSaleList;
    }
}
