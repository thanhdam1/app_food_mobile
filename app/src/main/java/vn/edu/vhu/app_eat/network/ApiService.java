package vn.edu.vhu.app_eat.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import vn.edu.vhu.app_eat.model.Category;
import vn.edu.vhu.app_eat.model.Food;

public interface ApiService {

    @GET("api/categories")
    Call<List<Category>> getCategories();

    @GET("api/foods/flash-sale")
    Call<List<Food>> getFlashSaleFoods();

    @GET("api/foods/best-sellers")
    Call<List<Food>> getBestSellerFoods();

    @GET("api/foods/category/{categoryId}")
    Call<List<Food>> getFoodsByCategory(@Path("categoryId") int categoryId);
}
