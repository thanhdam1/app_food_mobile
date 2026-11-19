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

public class FlashSaleAdapter extends RecyclerView.Adapter<FlashSaleAdapter.FlashSaleViewHolder> {

    private final List<Food> flashSaleList;
    private final OnFlashSaleClickListener listener;
    private final DecimalFormat formatter = new DecimalFormat("#,###");

    public interface OnFlashSaleClickListener {
        void onFlashSaleClick(Food food);
    }

    public FlashSaleAdapter(List<Food> flashSaleList, OnFlashSaleClickListener listener) {
        this.flashSaleList = flashSaleList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FlashSaleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flash_sale, parent, false);
        return new FlashSaleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashSaleViewHolder holder, int position) {
        Food food = flashSaleList.get(position);
        holder.tvName.setText(food.getName());

        if (food.getDiscountPrice() != null) {
            holder.tvDiscountPrice.setText(formatter.format(food.getDiscountPrice()) + " đ");
            holder.tvOriginalPrice.setText(formatter.format(food.getPrice()) + " đ");
            holder.tvOriginalPrice.setPaintFlags(holder.tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            
            int discountPercent = (int) Math.round((1.0 - (food.getDiscountPrice() / food.getPrice())) * 100);
            holder.tvBadge.setText("-" + discountPercent + "%");
        } else {
            holder.tvDiscountPrice.setText(formatter.format(food.getPrice()) + " đ");
            holder.tvOriginalPrice.setVisibility(View.GONE);
            holder.tvBadge.setVisibility(View.GONE);
        }

        ImageLoader.loadFoodImage(holder.itemView.getContext(), food.getImageUrl(), holder.ivImage);

        holder.itemView.setOnClickListener(v -> listener.onFlashSaleClick(food));
    }

    @Override
    public int getItemCount() {
        return flashSaleList.size();
    }

    static class FlashSaleViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName, tvDiscountPrice, tvOriginalPrice, tvBadge;

        public FlashSaleViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivFlashImage);
            tvName = itemView.findViewById(R.id.tvFlashName);
            tvDiscountPrice = itemView.findViewById(R.id.tvFlashDiscountPrice);
            tvOriginalPrice = itemView.findViewById(R.id.tvFlashOriginalPrice);
            tvBadge = itemView.findViewById(R.id.tvDiscountBadge);
        }
    }
}
