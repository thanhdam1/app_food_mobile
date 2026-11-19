package vn.edu.vhu.app_eat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import vn.edu.vhu.app_eat.R;
import vn.edu.vhu.app_eat.model.Banner;
import vn.edu.vhu.app_eat.utils.ImageLoader;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {

    private final List<Banner> bannerList;

    public BannerAdapter(List<Banner> bannerList) {
        this.bannerList = bannerList;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner_slide, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        Banner banner = bannerList.get(position);
        holder.tvTag.setText(banner.getTag());
        holder.tvTitle.setText(banner.getTitle());
        holder.tvSubtitle.setText(banner.getSubtitle());
        holder.cardBanner.setCardBackgroundColor(banner.getBackgroundColor());

        ImageLoader.loadFoodImage(holder.itemView.getContext(), banner.getImageUrl(), holder.ivBannerImage);
    }

    @Override
    public int getItemCount() {
        return bannerList.size();
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardBanner;
        TextView tvTag, tvTitle, tvSubtitle;
        ImageView ivBannerImage;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            cardBanner = itemView.findViewById(R.id.cardBanner);
            tvTag = itemView.findViewById(R.id.tvBannerTag);
            tvTitle = itemView.findViewById(R.id.tvBannerTitle);
            tvSubtitle = itemView.findViewById(R.id.tvBannerSubtitle);
            ivBannerImage = itemView.findViewById(R.id.ivBannerImage);
        }
    }
}
