package business.sql.sales_order;

public class PaymentMethodsSql {
    public static PaymentMethodsSql getInstance() { return new PaymentMethodsSql(); }

    public int insertPaymentMethod(Object t) { return 0; }
    public int insertBankTransferInfo(Object t) { return 0; } // Cho bảng BANK_TRANSFER_PAYMENT
    public int insertCashPaymentInfo(Object t) { return 0; } // Cho bảng CASH_PAYMENT
}