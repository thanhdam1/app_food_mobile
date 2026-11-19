package vn.edu.vhu.app_eat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

import vn.edu.vhu.app_eat.R;
import vn.edu.vhu.app_eat.model.CartItem;
import vn.edu.vhu.app_eat.utils.ImageLoader;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public interface OnCartQuantityChangeListener {
        void onCartUpdated();
    }

    private final List<CartItem> cartItems;
    private final OnCartQuantityChangeListener listener;
    private final DecimalFormat formatter = new DecimalFormat("#,###");

    public CartAdapter(List<CartItem> cartItems, OnCartQuantityChangeListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.tvName.setText(item.getName());
        holder.tvUnitPrice.setText(formatter.format(item.getUnitPrice()) + " đ / phần");
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        holder.tvItemTotal.setText(formatter.format(item.getTotalPrice()) + " đ");

        ImageLoader.loadFoodImage(holder.itemView.getContext(), item.getImageUrl(), holder.ivFood);

        holder.btnPlus.setOnClickListener(v -> {
            int newQty = item.getQuantity() + 1;
            item.setQuantity(newQty);
            holder.tvQuantity.setText(String.valueOf(newQty));
            holder.tvItemTotal.setText(formatter.format(item.getTotalPrice()) + " đ");
            if (listener != null) listener.onCartUpdated();
        });

        holder.btnMinus.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos == RecyclerView.NO_POSITION) return;

            if (item.getQuantity() > 1) {
                int newQty = item.getQuantity() - 1;
                item.setQuantity(newQty);
                holder.tvQuantity.setText(String.valueOf(newQty));
                holder.tvItemTotal.setText(formatter.format(item.getTotalPrice()) + " đ");
                if (listener != null) listener.onCartUpdated();
            } else {
                // If 1 and pressed minus, remove item
                cartItems.remove(currentPos);
                notifyItemRemoved(currentPos);
                notifyItemRangeChanged(currentPos, cartItems.size());
                if (listener != null) listener.onCartUpdated();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFood;
        TextView tvName, tvUnitPrice, tvQuantity, tvItemTotal;
        View btnMinus, btnPlus;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFood = itemView.findViewById(R.id.ivCartFood);
            tvName = itemView.findViewById(R.id.tvCartFoodName);
            tvUnitPrice = itemView.findViewById(R.id.tvCartFoodUnitPrice);
            tvQuantity = itemView.findViewById(R.id.tvCartQuantity);
            tvItemTotal = itemView.findViewById(R.id.tvCartItemTotal);
            btnMinus = itemView.findViewById(R.id.btnCartMinus);
            btnPlus = itemView.findViewById(R.id.btnCartPlus);
        }
    }
}
