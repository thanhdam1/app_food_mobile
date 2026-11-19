package vn.edu.vhu.app_eat.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import vn.edu.vhu.app_eat.model.Category;
import vn.edu.vhu.app_eat.model.Food;
import vn.edu.vhu.app_eat.repository.FoodRepository;

public class HomeViewModel extends ViewModel {

    private final FoodRepository repository;
    private final MutableLiveData<List<Category>> categoriesLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Food>> flashSalesLiveData = new MutableLiveData<>();

    public HomeViewModel() {
        repository = new FoodRepository();
    }

    public LiveData<List<Category>> getCategories() {
        repository.fetchCategories(categoriesLiveData);
        return categoriesLiveData;
    }

    public LiveData<List<Food>> getFlashSales() {
        repository.fetchFlashSales(flashSalesLiveData);
        return flashSalesLiveData;
    }
}
