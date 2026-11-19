package vn.edu.vhu.app_eat.utils;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import vn.edu.vhu.app_eat.model.OrderReceipt;

public class OrderManager {

    private static OrderReceipt currentReceipt = null;

    public static OrderReceipt getOrCreateCurrentReceipt(Context context) {
        if (currentReceipt != null) {
            return currentReceipt;
        }

        // Create default receipt if none created yet
        OrderReceipt receipt = new OrderReceipt();
        receipt.setOrderId("MZ-" + (100000 + new Random().nextInt(900000)));
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy", Locale.getDefault());
        receipt.setOrderTime(sdf.format(new Date()));

        receipt.setCustomerName(UserManager.getUserName(context));
        receipt.setCustomerPhone(UserManager.getUserPhone(context));
        receipt.setDeliveryAddress(UserManager.getUserAddress(context));

        receipt.setPaymentMethod("Tiền mặt khi nhận hàng (COD)");
        receipt.setPaymentStatus("🟡 Chờ thu tiền khi giao (COD)");

        receipt.addItem(new OrderReceipt.ReceiptItem("Burger Bò Phô Mai Thượng Hạng", 2, 49000.0));
        receipt.addItem(new OrderReceipt.ReceiptItem("Trà Sữa Trân Châu Hoàng Gia", 1, 35000.0));

        double subtotal = 49000.0 * 2 + 35000.0;
        receipt.setSubtotal(subtotal);
        receipt.setShippingFee(15000.0);
        receipt.setDiscount(15000.0); // Freeship
        receipt.setFinalTotal(subtotal + 15000.0 - 15000.0);

        currentReceipt = receipt;
        return currentReceipt;
    }

    public static void setCurrentReceipt(OrderReceipt receipt) {
        currentReceipt = receipt;
    }

    public static OrderReceipt getCurrentReceipt() {
        return currentReceipt;
    }
}
