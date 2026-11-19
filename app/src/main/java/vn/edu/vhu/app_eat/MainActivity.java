package vn.edu.vhu.app_eat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import vn.edu.vhu.app_eat.adapter.BannerAdapter;
import vn.edu.vhu.app_eat.adapter.CategoryAdapter;
import vn.edu.vhu.app_eat.adapter.FlashSaleAdapter;
import vn.edu.vhu.app_eat.adapter.FoodAdapter;
import vn.edu.vhu.app_eat.model.Banner;
import vn.edu.vhu.app_eat.model.Category;
import vn.edu.vhu.app_eat.model.Food;
import vn.edu.vhu.app_eat.ui.auth.EditProfileActivity;
import vn.edu.vhu.app_eat.ui.auth.LoginActivity;
import vn.edu.vhu.app_eat.ui.category.CategoryFoodsActivity;
import vn.edu.vhu.app_eat.ui.detail.FoodDetailActivity;
import vn.edu.vhu.app_eat.ui.tracking.LocationPickerActivity;
import vn.edu.vhu.app_eat.ui.tracking.TrackingActivity;
import vn.edu.vhu.app_eat.utils.ImageLoader;
import vn.edu.vhu.app_eat.utils.UserManager;

public class MainActivity extends AppCompatActivity {

    private FrameLayout loadingFrameLayout;
    private NestedScrollView scrollViewContent;
    private FrameLayout secondaryContainer;
    private RecyclerView rvCategories, rvFlashSale, rvBestSellers;
    private ViewPager2 vpBanners;
    private LinearLayout layoutFlashSaleSection, layoutCategorySection, layoutEmptySearch;
    private TextView tvBestSellersHeader, tvEmptySearchMessage;
    private EditText etSearch;
    private ImageView ivClearSearch;
    private ImageView ivUserAvatar;
    private TextView tvHeaderAddress;
    private BottomNavigationView bottomNavigationView;
    private int currentTabId = R.id.nav_home;

    private CountDownTimer flashSaleTimer;
    private final Handler bannerHandler = new Handler(Looper.getMainLooper());
    private Runnable bannerRunnable;

    private final List<Food> masterFoodList = new ArrayList<>();
    private final List<Food> defaultBestSellerList = new ArrayList<>();
    private final List<Food> currentBestSellerList = new ArrayList<>();
    private FoodAdapter bestSellerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);

            loadingFrameLayout = findViewById(R.id.shimmerFrameLayout);
            scrollViewContent = findViewById(R.id.scrollViewContent);
            secondaryContainer = findViewById(R.id.secondaryContainer);
            rvCategories = findViewById(R.id.rvCategories);
            rvFlashSale = findViewById(R.id.rvFlashSale);
            rvBestSellers = findViewById(R.id.rvBestSellers);
            vpBanners = findViewById(R.id.vpBanners);
            layoutFlashSaleSection = findViewById(R.id.layoutFlashSaleSection);
            layoutCategorySection = findViewById(R.id.layoutCategorySection);
            layoutEmptySearch = findViewById(R.id.layoutEmptySearch);
            tvBestSellersHeader = findViewById(R.id.tvBestSellersHeader);
            tvEmptySearchMessage = findViewById(R.id.tvEmptySearchMessage);
            etSearch = findViewById(R.id.etSearch);
            ivClearSearch = findViewById(R.id.ivClearSearch);

            ivUserAvatar = findViewById(R.id.ivUserAvatar);
            tvHeaderAddress = findViewById(R.id.tvHeaderAddress);
            bottomNavigationView = findViewById(R.id.bottomNavigationView);

            updateTopHeaderUser();

            if (tvHeaderAddress != null) {
                tvHeaderAddress.setOnClickListener(v ->
                        startActivity(new Intent(MainActivity.this, LocationPickerActivity.class))
                );
            }

            if (ivUserAvatar != null) {
                ivUserAvatar.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
                    startActivity(intent);
                });
            }

            if (loadingFrameLayout != null) {
                loadingFrameLayout.setVisibility(View.VISIBLE);
            }
            new Handler(Looper.getMainLooper()).postDelayed(this::loadMockData, 1200);

            setupAnimatedBanners();
            startFlashSaleCountdown();
            setupSearchBar();

            if (bottomNavigationView != null) {
                bottomNavigationView.setOnItemSelectedListener(item -> {
                    currentTabId = item.getItemId();
                    if (currentTabId == R.id.nav_home) {
                        if (secondaryContainer != null) secondaryContainer.setVisibility(View.GONE);
                        if (scrollViewContent != null) scrollViewContent.setVisibility(View.VISIBLE);
                        return true;
                    } else if (currentTabId == R.id.nav_orders) {
                        startActivity(new Intent(MainActivity.this, TrackingActivity.class));
                        return false; // keep previous tab selected
                    } else if (currentTabId == R.id.nav_loyalty) {
                        if (scrollViewContent != null) scrollViewContent.setVisibility(View.GONE);
                        if (secondaryContainer != null) {
                            secondaryContainer.setVisibility(View.VISIBLE);
                            loadLoyaltyView(secondaryContainer);
                        }
                        return true;
                    } else if (currentTabId == R.id.nav_profile) {
                        if (scrollViewContent != null) scrollViewContent.setVisibility(View.GONE);
                        if (secondaryContainer != null) {
                            secondaryContainer.setVisibility(View.VISIBLE);
                            loadProfileView(secondaryContainer);
                        }
                        return true;
                    }
                    return false;
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTopHeaderUser();
        if (currentTabId == R.id.nav_profile && secondaryContainer != null && secondaryContainer.getVisibility() == View.VISIBLE) {
            loadProfileView(secondaryContainer);
        } else if (currentTabId == R.id.nav_loyalty && secondaryContainer != null && secondaryContainer.getVisibility() == View.VISIBLE) {
            loadLoyaltyView(secondaryContainer);
        }
    }

    private void updateTopHeaderUser() {
        if (ivUserAvatar != null) {
            ImageLoader.loadAvatar(this, UserManager.getUserAvatar(this), ivUserAvatar);
        }
        if (tvHeaderAddress != null) {
            tvHeaderAddress.setText(UserManager.getUserAddress(this));
        }
    }

    private void loadProfileView(FrameLayout container) {
        if (container == null) return;
        container.removeAllViews();
        View profileView = LayoutInflater.from(this).inflate(R.layout.activity_profile, container, true);

        ImageView ivAvatar = profileView.findViewById(R.id.ivProfileAvatar);
        TextView tvName = profileView.findViewById(R.id.tvProfileName);
        TextView tvPhone = profileView.findViewById(R.id.tvProfilePhone);
        TextView tvEmail = profileView.findViewById(R.id.tvProfileEmail);
        TextView tvRankPoints = profileView.findViewById(R.id.tvProfileRankPoints);
        TextView tvLoyaltyBannerPoints = profileView.findViewById(R.id.tvLoyaltyBannerPoints);
        TextView tvLoyaltyBannerRank = profileView.findViewById(R.id.tvLoyaltyBannerRank);

        String name = UserManager.getUserName(this);
        String phone = UserManager.getUserPhone(this);
        String email = UserManager.getUserEmail(this);
        String address = UserManager.getUserAddress(this);
        String avatar = UserManager.getUserAvatar(this);
        int points = UserManager.getUserPoints(this);
        String rank = UserManager.getRankTitle(points);

        if (tvName != null) tvName.setText(name);
        if (tvPhone != null) tvPhone.setText("📞 " + phone);
        if (tvEmail != null) tvEmail.setText("✉️ " + email);
        if (tvRankPoints != null) tvRankPoints.setText("🌟 Hạng " + rank + " (" + points + " điểm thưởng)");
        if (tvLoyaltyBannerRank != null) tvLoyaltyBannerRank.setText("Hạng " + rank + " - Điểm Tích Lũy");
        if (tvLoyaltyBannerPoints != null) tvLoyaltyBannerPoints.setText("Bạn đang có " + points + " điểm thưởng • Bấm để đổi voucher");

        if (ivAvatar != null) {
            ImageLoader.loadAvatar(this, avatar, ivAvatar);
        }

        View btnEditHeader = profileView.findViewById(R.id.btnEditProfileHeader);
        View btnEditOption = profileView.findViewById(R.id.btnOptionEditProfile);
        View.OnClickListener editListener = v -> {
            Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
            startActivity(intent);
        };
        if (btnEditHeader != null) btnEditHeader.setOnClickListener(editListener);
        if (btnEditOption != null) btnEditOption.setOnClickListener(editListener);

        View btnViewLoyalty = profileView.findViewById(R.id.btnViewLoyalty);
        if (btnViewLoyalty != null) {
            btnViewLoyalty.setOnClickListener(v -> {
                if (bottomNavigationView != null) {
                    bottomNavigationView.setSelectedItemId(R.id.nav_loyalty);
                }
            });
        }
        if (tvRankPoints != null) {
            tvRankPoints.setOnClickListener(v -> {
                if (bottomNavigationView != null) {
                    bottomNavigationView.setSelectedItemId(R.id.nav_loyalty);
                }
            });
        }

        View btnAddress = profileView.findViewById(R.id.btnAddressManagement);
        if (btnAddress != null) {
            btnAddress.setOnClickListener(v ->
                    startActivity(new Intent(MainActivity.this, LocationPickerActivity.class))
            );
        }

        View btnHistory = profileView.findViewById(R.id.btnOrderHistory);
        if (btnHistory != null) {
            btnHistory.setOnClickListener(v ->
                    startActivity(new Intent(MainActivity.this, TrackingActivity.class))
            );
        }

        View btnPass = profileView.findViewById(R.id.btnChangePassword);
        if (btnPass != null) {
            btnPass.setOnClickListener(v ->
                    Toast.makeText(this, "Mã OTP đặt lại mật khẩu đã được gửi về: " + email, Toast.LENGTH_SHORT).show()
            );
        }

        setupDailyCheckin(profileView, container);

        Button btnLogout = profileView.findViewById(R.id.btnLogout);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                Toast.makeText(this, "Đã đăng xuất tài khoản thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            });
        }
    }

    private void setupDailyCheckin(View profileView, FrameLayout container) {
        View btnOptionCheckin = profileView.findViewById(R.id.btnOptionDailyCheckin);
        TextView tvBadge = profileView.findViewById(R.id.tvOptionCheckinBadge);
        TextView tvSub = profileView.findViewById(R.id.tvOptionDailyCheckinSub);
        if (btnOptionCheckin == null) return;

        boolean checkedToday = UserManager.hasCheckedInToday(this);
        int currentStreak = UserManager.getCheckinStreak(this);
        int nextDayIndex = checkedToday ? currentStreak : ((currentStreak % 7) + 1);
        int reward = UserManager.getStreakReward(nextDayIndex);

        if (tvBadge != null) {
            if (checkedToday) {
                tvBadge.setText("✅ Chuỗi " + currentStreak + " ngày");
                tvBadge.setTextColor(Color.parseColor("#008B3E"));
                tvBadge.setBackgroundColor(Color.parseColor("#E8F8EE"));
            } else {
                tvBadge.setText("🎁 Nhận +" + reward + "đ");
                tvBadge.setTextColor(Color.parseColor("#E65100"));
                tvBadge.setBackgroundColor(Color.parseColor("#FFF3E0"));
            }
        }

        if (tvSub != null) {
            if (checkedToday) {
                tvSub.setText("Đã điểm danh hôm nay • Tiếp tục vào ngày mai nhé!");
            } else {
                tvSub.setText("Đăng nhập nhận +" + reward + " điểm & quà VIP ngay");
            }
        }

        btnOptionCheckin.setOnClickListener(v -> showDailyCheckinDialog(container));
    }

    private void showDailyCheckinDialog(FrameLayout container) {
        android.app.Dialog dialog = new android.app.Dialog(this);
        dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_daily_checkin);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(
                    (int) (getResources().getDisplayMetrics().widthPixels * 0.92),
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }

        View btnClose = dialog.findViewById(R.id.btnCloseCheckinDialog);
        if (btnClose != null) btnClose.setOnClickListener(v -> dialog.dismiss());

        TextView tvStreak = dialog.findViewById(R.id.tvDialogCheckinStreak);
        com.google.android.material.button.MaterialButton btnCheckin = dialog.findViewById(R.id.btnDailyCheckin);

        boolean checkedToday = UserManager.hasCheckedInToday(this);
        int currentStreak = UserManager.getCheckinStreak(this);
        int nextDayIndex = checkedToday ? currentStreak : ((currentStreak % 7) + 1);

        if (tvStreak != null) {
            tvStreak.setText("🔥 Chuỗi " + currentStreak + " ngày liên tiếp");
        }

        int[] cardIds = {R.id.cardDay1, R.id.cardDay2, R.id.cardDay3, R.id.cardDay4, R.id.cardDay5, R.id.cardDay6, R.id.cardDay7};
        int[] iconIds = {R.id.tvDayIcon1, R.id.tvDayIcon2, R.id.tvDayIcon3, R.id.tvDayIcon4, R.id.tvDayIcon5, R.id.tvDayIcon6, R.id.tvDayIcon7};
        int[] pointsIds = {R.id.tvDayPoints1, R.id.tvDayPoints2, R.id.tvDayPoints3, R.id.tvDayPoints4, R.id.tvDayPoints5, R.id.tvDayPoints6, R.id.tvDayPoints7};

        int activeStreakInCycle = currentStreak % 7;
        if (activeStreakInCycle == 0 && currentStreak > 0) activeStreakInCycle = 7;

        for (int i = 0; i < 7; i++) {
            com.google.android.material.card.MaterialCardView card = dialog.findViewById(cardIds[i]);
            TextView tvIcon = dialog.findViewById(iconIds[i]);
            TextView tvPoints = dialog.findViewById(pointsIds[i]);
            int dayNumber = i + 1;

            if (card != null && tvIcon != null && tvPoints != null) {
                if (dayNumber <= activeStreakInCycle) {
                    card.setCardBackgroundColor(Color.parseColor("#E8F8EE"));
                    card.setStrokeColor(Color.parseColor("#00B14F"));
                    tvIcon.setText("✅");
                    tvPoints.setText("Đã nhận");
                    tvPoints.setTextColor(Color.parseColor("#008B3E"));
                } else if (!checkedToday && dayNumber == activeStreakInCycle + 1) {
                    card.setCardBackgroundColor(Color.parseColor("#FFF3E0"));
                    card.setStrokeColor(Color.parseColor("#FF9800"));
                    tvIcon.setText(dayNumber == 7 ? "🎁" : "✨");
                    tvPoints.setText("+" + UserManager.getStreakReward(dayNumber) + "đ");
                    tvPoints.setTextColor(Color.parseColor("#E65100"));
                }
            }
        }

        if (btnCheckin != null) {
            if (checkedToday) {
                btnCheckin.setText("✅ Bạn đã điểm danh hôm nay");
                btnCheckin.setEnabled(false);
                btnCheckin.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#A5D6A7")));
            } else {
                int reward = UserManager.getStreakReward(nextDayIndex);
                btnCheckin.setText("🎁 Điểm Danh Nhận +" + reward + " Điểm Ngay");
                btnCheckin.setEnabled(true);
                btnCheckin.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#00B14F")));
                btnCheckin.setOnClickListener(v -> {
                    int claimedReward = UserManager.claimDailyReward(MainActivity.this);
                    dialog.dismiss();
                    if (claimedReward > 0) {
                        new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this)
                                .setTitle("🎉 Điểm Danh Thành Công!")
                                .setMessage("Bạn vừa nhận được +" + claimedReward + " điểm thưởng tích lũy.\nHãy duy trì chuỗi điểm danh mỗi ngày nhé!")
                                .setPositiveButton("Tuyệt vời", (d, which) -> {
                                    d.dismiss();
                                    loadProfileView(container);
                                })
                                .setCancelable(false)
                                .show();
                    } else {
                        loadProfileView(container);
                    }
                });
            }
        }

        dialog.show();
    }

    private void loadLoyaltyView(FrameLayout container) {
        if (container == null) return;
        container.removeAllViews();
        View loyaltyView = LayoutInflater.from(this).inflate(R.layout.activity_loyalty, container, true);

        TextView tvRank = loyaltyView.findViewById(R.id.tvLoyaltyRank);
        TextView tvPoints = loyaltyView.findViewById(R.id.tvLoyaltyPoints);
        ProgressBar pbProgress = loyaltyView.findViewById(R.id.pbTierProgress);
        TextView tvNextNotice = loyaltyView.findViewById(R.id.tvNextTierNotice);
        Button btnRedeem20k = loyaltyView.findViewById(R.id.btnRedeem20k);
        Button btnRedeemFreeship = loyaltyView.findViewById(R.id.btnRedeemFreeship);

        refreshLoyaltyTexts(tvRank, tvPoints, pbProgress, tvNextNotice);

        if (btnRedeem20k != null) {
            btnRedeem20k.setOnClickListener(v -> {
                if (UserManager.redeemPoints(this, 100)) {
                    Toast.makeText(this, "🎉 Đã đổi thành công Voucher 20.000đ (Mã: MAMZI20K)!", Toast.LENGTH_SHORT).show();
                    refreshLoyaltyTexts(tvRank, tvPoints, pbProgress, tvNextNotice);
                } else {
                    Toast.makeText(this, "Bạn cần tối thiểu 100 điểm để đổi voucher này!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (btnRedeemFreeship != null) {
            btnRedeemFreeship.setOnClickListener(v -> {
                if (UserManager.redeemPoints(this, 150)) {
                    Toast.makeText(this, "🎉 Đã đổi thành công Mã Freeship 0đ (Mã: FREESHIP0D)!", Toast.LENGTH_SHORT).show();
                    refreshLoyaltyTexts(tvRank, tvPoints, pbProgress, tvNextNotice);
                } else {
                    Toast.makeText(this, "Bạn cần tối thiểu 150 điểm để đổi mã Freeship!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void refreshLoyaltyTexts(TextView tvRank, TextView tvPoints, ProgressBar pbProgress, TextView tvNextNotice) {
        int points = UserManager.getUserPoints(this);
        String rank = UserManager.getRankTitle(points);

        if (tvRank != null) tvRank.setText("🌟 " + rank);
        if (tvPoints != null) tvPoints.setText(String.valueOf(points));

        int max = 1000;
        int nextPoints = Math.max(0, max - points);
        if (pbProgress != null) {
            pbProgress.setMax(max);
            pbProgress.setProgress(Math.min(points, max));
        }
        if (tvNextNotice != null) {
            if (nextPoints > 0) {
                tvNextNotice.setText("Tích thêm " + nextPoints + " điểm để thăng hạng VÀNG (GOLD)");
            } else {
                tvNextNotice.setText("🎉 Bạn đã đạt cấp bậc thành viên ưu tú!");
            }
        }
    }

    private void setupSearchBar() {
        if (etSearch == null) return;

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s != null ? s.toString().trim() : "";
                filterFoods(query);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        if (ivClearSearch != null) {
            ivClearSearch.setOnClickListener(v -> {
                etSearch.setText("");
                etSearch.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                }
            });
        }
    }

    private void filterFoods(String query) {
        if (query.isEmpty()) {
            if (ivClearSearch != null) ivClearSearch.setVisibility(View.GONE);
            if (vpBanners != null) vpBanners.setVisibility(View.VISIBLE);
            if (layoutFlashSaleSection != null) layoutFlashSaleSection.setVisibility(View.VISIBLE);
            if (layoutCategorySection != null) layoutCategorySection.setVisibility(View.VISIBLE);
            if (layoutEmptySearch != null) layoutEmptySearch.setVisibility(View.GONE);
            if (rvBestSellers != null) rvBestSellers.setVisibility(View.VISIBLE);
            if (tvBestSellersHeader != null) tvBestSellersHeader.setText("🔥 Món Bán Chạy Nhất (Best Sellers)");

            currentBestSellerList.clear();
            currentBestSellerList.addAll(defaultBestSellerList);
            if (bestSellerAdapter != null) {
                bestSellerAdapter.notifyDataSetChanged();
            }
            return;
        }

        if (ivClearSearch != null) ivClearSearch.setVisibility(View.VISIBLE);
        if (vpBanners != null) vpBanners.setVisibility(View.GONE);
        if (layoutFlashSaleSection != null) layoutFlashSaleSection.setVisibility(View.GONE);
        if (layoutCategorySection != null) layoutCategorySection.setVisibility(View.GONE);

        String normalizedQuery = removeAccent(query.toLowerCase());

        List<Food> filteredList = new ArrayList<>();
        for (Food food : masterFoodList) {
            String nameNorm = removeAccent(food.getName().toLowerCase());
            String descNorm = removeAccent(food.getDescription().toLowerCase());
            if (nameNorm.contains(normalizedQuery) || descNorm.contains(normalizedQuery)) {
                filteredList.add(food);
            }
        }

        currentBestSellerList.clear();
        currentBestSellerList.addAll(filteredList);
        if (bestSellerAdapter != null) {
            bestSellerAdapter.notifyDataSetChanged();
        }

        if (filteredList.isEmpty()) {
            if (tvBestSellersHeader != null) tvBestSellersHeader.setText("🔍 Kết quả tìm kiếm (0 món)");
            if (rvBestSellers != null) rvBestSellers.setVisibility(View.GONE);
            if (layoutEmptySearch != null) {
                layoutEmptySearch.setVisibility(View.VISIBLE);
                if (tvEmptySearchMessage != null) {
                    tvEmptySearchMessage.setText("Không tìm thấy món ăn nào cho \"" + query + "\"");
                }
            }
        } else {
            if (tvBestSellersHeader != null) {
                tvBestSellersHeader.setText("🔍 Kết quả tìm kiếm (" + filteredList.size() + " món)");
            }
            if (rvBestSellers != null) rvBestSellers.setVisibility(View.VISIBLE);
            if (layoutEmptySearch != null) layoutEmptySearch.setVisibility(View.GONE);
        }
    }

    private static String removeAccent(String s) {
        if (s == null) return "";
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replace('đ', 'd').replace('Đ', 'D');
    }

    private void setupAnimatedBanners() {
        if (vpBanners == null) return;

        List<Banner> bannerList = new ArrayList<>();
        bannerList.add(new Banner("Thưởng Thức Món Ngon", "Đặt đơn đầu tiên giảm 30k", "⚡ Ưu đãi đến 50%", "food_burger", 0xFF00B14F));
        bannerList.add(new Banner("Trà Sữa Mua 1 Tặng 1", "Topping ngập tràn sảng khoái", "🎉 Mua 1 Tặng 1", "food_tra_sua", 0xFFFF6B6B));
        bannerList.add(new Banner("Miễn Phí Giao Hàng 0đ", "Áp dụng cho mọi đơn từ 50k", "🚀 FREESHIP 0Đ", "food_com_tam", 0xFF008B3E));

        BannerAdapter bannerAdapter = new BannerAdapter(bannerList);
        vpBanners.setAdapter(bannerAdapter);

        bannerRunnable = new Runnable() {
            @Override
            public void run() {
                if (isFinishing() || isDestroyed() || vpBanners == null) return;
                try {
                    if (vpBanners.isShown()) {
                        int currentItem = vpBanners.getCurrentItem();
                        int totalItems = bannerList.size();
                        if (totalItems > 0) {
                            int nextItem = (currentItem + 1) % totalItems;
                            vpBanners.setCurrentItem(nextItem, true);
                        }
                    }
                } catch (Exception ignored) {}
                bannerHandler.postDelayed(this, 3500);
            }
        };
        bannerHandler.postDelayed(bannerRunnable, 3500);
    }

    private void startFlashSaleCountdown() {
        TextView tvTimer = findViewById(R.id.tvTimer);
        if (tvTimer == null) return;

        long durationMillis = 2 * 3600 * 1000 + 45 * 60 * 1000 + 12 * 1000;
        flashSaleTimer = new CountDownTimer(durationMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (isFinishing() || isDestroyed() || tvTimer == null) return;
                long hours = (millisUntilFinished / (1000 * 60 * 60)) % 24;
                long minutes = (millisUntilFinished / (1000 * 60)) % 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                tvTimer.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds));
            }

            @Override
            public void onFinish() {
                if (tvTimer != null) {
                    tvTimer.setText("00:00:00");
                }
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (flashSaleTimer != null) {
            flashSaleTimer.cancel();
        }
        if (bannerHandler != null && bannerRunnable != null) {
            bannerHandler.removeCallbacks(bannerRunnable);
        }
    }

    private void loadMockData() {
        if (isFinishing() || isDestroyed()) return;

        if (loadingFrameLayout != null) {
            loadingFrameLayout.setVisibility(View.GONE);
        }
        if (scrollViewContent != null) {
            scrollViewContent.setVisibility(View.VISIBLE);
        }

        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category(1, "Fast Food", "https://cdn-icons-png.flaticon.com/512/1046/1046784.png"));
        categoryList.add(new Category(2, "Trà Sữa", "https://cdn-icons-png.flaticon.com/512/920/920534.png"));
        categoryList.add(new Category(3, "Cơm Tấm", "https://cdn-icons-png.flaticon.com/512/3076/3076137.png"));
        categoryList.add(new Category(4, "Đồ Ăn Vặt", "https://cdn-icons-png.flaticon.com/512/3132/3132693.png"));

        if (rvCategories != null) {
            CategoryAdapter categoryAdapter = new CategoryAdapter(categoryList, category -> {
                Intent intent = new Intent(MainActivity.this, CategoryFoodsActivity.class);
                intent.putExtra("CATEGORY_NAME", category.getName());
                startActivity(intent);
            });
            rvCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            rvCategories.setAdapter(categoryAdapter);
        }

        List<Food> flashSaleList = new ArrayList<>();
        flashSaleList.add(new Food(1, "Burger Bò Phô Mai", "Thịt bò Úc nướng khói, phô mai tan chảy", 65000.0, 49000.0, "food_burger", 4.9, 1280, true, true));
        flashSaleList.add(new Food(2, "Trà Sữa Trân Châu", "Hồng trà sữa truyền thống trân châu dẻo", 45000.0, 35000.0, "food_tra_sua", 4.9, 3200, true, true));
        flashSaleList.add(new Food(3, "Tokbokki Phô Mai", "Bánh gạo cay Hàn Quốc phủ phô mai", 50000.0, 39000.0, "food_tokbokki", 4.7, 740, true, false));

        if (rvFlashSale != null) {
            FlashSaleAdapter flashSaleAdapter = new FlashSaleAdapter(flashSaleList, food -> {
                Intent intent = new Intent(MainActivity.this, FoodDetailActivity.class);
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
            rvFlashSale.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            rvFlashSale.setAdapter(flashSaleAdapter);
        }

        // Master Food Dataset with 100% matching images and authentic details
        masterFoodList.clear();
        masterFoodList.add(new Food(4, "Cơm Tấm Sườn Bì Chả", "Sườn nướng mật ong thơm lừng, chả trứng, bì giòn đậm đà", 55000.0, null, "food_com_tam", 4.9, 2100, false, true));
        masterFoodList.add(new Food(1, "Burger Bò Phô Mai Thượng Hạng", "Thịt bò Úc nguyên chất nướng khói sốt đặc biệt MamZi", 65000.0, 49000.0, "food_burger", 4.9, 1280, true, true));
        masterFoodList.add(new Food(2, "Gà Rán Sốt Cay Hàn Quốc", "Gà giòn rụm phủ sốt cay ngọt chuẩn vị Seoul", 75000.0, null, "food_ga_ran", 4.8, 950, false, true));
        masterFoodList.add(new Food(5, "Pizza Phô Mai Hải Sản", "Pizza đế mỏng giòn rụm topping tôm mực phô mai mozzarella ngập tràn", 129000.0, 99000.0, "food_pizza", 4.9, 1850, true, true));
        masterFoodList.add(new Food(6, "Mì Trộn Trứng Lòng Đào", "Mì cay sốt sa tế xá xíu kết hợp trứng lòng đào béo ngậy", 45000.0, 38000.0, "food_mi_tron", 4.8, 1420, true, false));
        masterFoodList.add(new Food(7, "Trà Sữa Trân Châu Hoàng Gia", "Hồng trà sữa đậm vị kết hợp trân châu dai giòn thơm ngon", 45000.0, 35000.0, "food_tra_sua", 4.9, 3200, true, true));
        masterFoodList.add(new Food(8, "Tokbokki Phô Mai Hàn Quốc", "Bánh gạo dẻo dai đẫm sốt tương ớt Gochujang phủ phô mai béo ngậy", 50000.0, 39000.0, "food_tokbokki", 4.7, 740, true, false));
        masterFoodList.add(new Food(9, "Cơm Gà Xối Mỡ Giòn Rụm", "Đùi gà góc tư chiên giòn ăn kèm cơm chiên đỏ và dưa leo cà chua", 52000.0, 45000.0, "food_com_ga", 4.8, 1600, true, true));
        masterFoodList.add(new Food(10, "Bánh Tráng Trộn Sốt Bơ", "Bánh tráng dẻo dai trộn bò khô, trứng cút, tép khô, xoài xanh sốt bơ béo", 25000.0, 19000.0, "food_banh_trang", 4.9, 4300, true, true));
        masterFoodList.add(new Food(11, "Khoai Tây Chiên Lắc Phô Mai", "Khoai tây chiên giòn rụm phủ bột phô mai mặn ngọt thơm lừng", 35000.0, 29000.0, "food_khoai_tay", 4.7, 1850, true, false));
        masterFoodList.add(new Food(12, "Cơm Bò Lúc Lắc Hạt Điều", "Thịt bò mềm mọng thơm đậm đà kèm cơm hạt dẻo thơm nức mũi", 68000.0, 58000.0, "food_bo_luc_lac", 4.9, 920, true, false));
        masterFoodList.add(new Food(13, "Hồng Trà Kem Cheese", "Hồng trà thơm mát phủ lớp kem cheese sánh mịn béo ngậy", 48000.0, null, "food_hong_tra", 4.8, 1450, false, true));

        // Default Best Sellers (first 6 popular dishes)
        defaultBestSellerList.clear();
        defaultBestSellerList.add(masterFoodList.get(0)); // Cơm tấm
        defaultBestSellerList.add(masterFoodList.get(1)); // Burger
        defaultBestSellerList.add(masterFoodList.get(2)); // Gà rán
        defaultBestSellerList.add(masterFoodList.get(3)); // Pizza
        defaultBestSellerList.add(masterFoodList.get(4)); // Mì trộn
        defaultBestSellerList.add(masterFoodList.get(5)); // Trà sữa

        currentBestSellerList.clear();
        currentBestSellerList.addAll(defaultBestSellerList);

        if (rvBestSellers != null) {
            bestSellerAdapter = new FoodAdapter(currentBestSellerList, food -> {
                Intent intent = new Intent(MainActivity.this, FoodDetailActivity.class);
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
            rvBestSellers.setLayoutManager(new LinearLayoutManager(this));
            rvBestSellers.setAdapter(bestSellerAdapter);
        }
    }
}

