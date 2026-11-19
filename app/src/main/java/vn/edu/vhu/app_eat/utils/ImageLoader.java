package vn.edu.vhu.app_eat.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import vn.edu.vhu.app_eat.R;

public class ImageLoader {

    public static void loadFoodImage(Context context, String imageUrl, ImageView imageView) {
        if (context == null || imageView == null) return;

        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            imageView.setImageResource(R.drawable.logo);
            return;
        }

        imageUrl = imageUrl.trim();

        int resId = 0;
        if (!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://")) {
            resId = context.getResources().getIdentifier(imageUrl, "drawable", context.getPackageName());
        }

        if (resId != 0) {
            Glide.with(context)
                    .load(resId)
                    .centerCrop()
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .centerCrop()
                    .into(imageView);
        }
    }

    public static void loadAvatar(Context context, String avatarNameOrUrl, ImageView imageView) {
        if (context == null || imageView == null) return;

        if (avatarNameOrUrl == null || avatarNameOrUrl.trim().isEmpty()) {
            avatarNameOrUrl = "avt_1";
        }

        avatarNameOrUrl = avatarNameOrUrl.trim();

        int resId = 0;
        if (!avatarNameOrUrl.startsWith("http://") && !avatarNameOrUrl.startsWith("https://")) {
            resId = context.getResources().getIdentifier(avatarNameOrUrl, "drawable", context.getPackageName());
        }

        if (resId != 0) {
            Glide.with(context)
                    .load(resId)
                    .circleCrop()
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(avatarNameOrUrl)
                    .circleCrop()
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(imageView);
        }
    }
}
