package model;

import java.sql.Timestamp;

public class BankTransferPayment {
    private String paymentMethodId;
    private String bankName;
    private Timestamp transactionTime;
    private String senderAccountNumber;
    private String qrCode;
    private int isDeleted;

    public BankTransferPayment() {}

    public BankTransferPayment(String paymentMethodId, String bankName, Timestamp transactionTime, 
                               String senderAccountNumber, String qrCode, int isDeleted) {
        this.paymentMethodId = paymentMethodId;
        this.bankName = bankName;
        this.transactionTime = transactionTime;
        this.senderAccountNumber = senderAccountNumber;
        this.qrCode = qrCode;
        this.isDeleted = isDeleted;
    }

    // Getters and Setters...
}