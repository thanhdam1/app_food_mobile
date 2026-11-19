package vn.edu.vhu.app_eat.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderReceipt implements Serializable {
    private String orderId;
    private String orderTime;
    private String customerName;
    private String customerPhone;
    private String deliveryAddress;
    private String paymentMethod;
    private String paymentStatus;
    private double subtotal;
    private double shippingFee;
    private double discount;
    private double finalTotal;
    private List<ReceiptItem> items = new ArrayList<>();

    public static class ReceiptItem implements Serializable {
        private String foodName;
        private int quantity;
        private double unitPrice;
        private double itemTotal;

        public ReceiptItem(String foodName, int quantity, double unitPrice) {
            this.foodName = foodName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.itemTotal = unitPrice * quantity;
        }

        public String getFoodName() { return foodName; }
        public int getQuantity() { return quantity; }
        public double getUnitPrice() { return unitPrice; }
        public double getItemTotal() { return itemTotal; }
    }

    public OrderReceipt() {}

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getOrderTime() { return orderTime; }
    public void setOrderTime(String orderTime) { this.orderTime = orderTime; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public double getShippingFee() { return shippingFee; }
    public void setShippingFee(double shippingFee) { this.shippingFee = shippingFee; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    public double getFinalTotal() { return finalTotal; }
    public void setFinalTotal(double finalTotal) { this.finalTotal = finalTotal; }

    public List<ReceiptItem> getItems() { return items; }
    public void setItems(List<ReceiptItem> items) { this.items = items; }
    public void addItem(ReceiptItem item) { this.items.add(item); }
}
