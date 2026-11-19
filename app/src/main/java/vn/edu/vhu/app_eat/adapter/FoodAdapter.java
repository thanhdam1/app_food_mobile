package vn.edu.vhu.app_eat.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

import vn.edu.vhu.app_eat.R;
import vn.edu.vhu.app_eat.model.Food;
import vn.edu.vhu.app_eat.utils.ImageLoader;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private final List<Food> foodList;
    private final OnFoodClickListener listener;
    private final DecimalFormat formatter = new DecimalFormat("#,###");

    public interface OnFoodClickListener {
        void onFoodClick(Food food);
    }

    public FoodAdapter(List<Food> foodList, OnFoodClickListener listener) {
        this.foodList = foodList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = foodList.get(position);
        holder.tvName.setText(food.getName());
        holder.tvDesc.setText(food.getDescription());
        holder.tvRating.setText(String.valueOf(food.getRating()));

        if (food.getDiscountPrice() != null) {
            holder.tvPrice.setText(formatter.format(food.getDiscountPrice()) + " đ");
            holder.tvOriginalPrice.setText(formatter.format(food.getPrice()) + " đ");
            holder.tvOriginalPrice.setVisibility(View.VISIBLE);
            holder.tvOriginalPrice.setPaintFlags(holder.tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.tvPrice.setText(formatter.format(food.getPrice()) + " đ");
            holder.tvOriginalPrice.setVisibility(View.GONE);
        }

        ImageLoader.loadFoodImage(holder.itemView.getContext(), food.getImageUrl(), holder.ivImage);

        holder.itemView.setOnClickListener(v -> listener.onFoodClick(food));
        if (holder.btnFoodAdd != null) {
            holder.btnFoodAdd.setOnClickListener(v -> listener.onFoodClick(food));
        }
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName, tvDesc, tvPrice, tvOriginalPrice, tvRating;
        View btnFoodAdd;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivFoodImage);
            tvName = itemView.findViewById(R.id.tvFoodName);
            tvDesc = itemView.findViewById(R.id.tvFoodDesc);
            tvPrice = itemView.findViewById(R.id.tvFoodPrice);
            tvOriginalPrice = itemView.findViewById(R.id.tvOriginalPrice);
            tvRating = itemView.findViewById(R.id.tvRating);
            btnFoodAdd = itemView.findViewById(R.id.btnFoodAdd);
        }
    }
}
