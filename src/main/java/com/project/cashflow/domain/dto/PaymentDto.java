package com.project.cashflow.domain.dto;

import com.project.cashflow.domain.Payment;
import com.project.cashflow.domain.PaymentInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PaymentDto {

    private UUID id;

    private List<PaymentInfo> paymentInfoList;

    private double totalIncome;

    private double totalExpenses;

    private double totalLeftOver;

    @Builder
    public PaymentDto(UUID id, List<PaymentInfo> paymentInfoList,
                      double totalIncome,
                      double totalExpenses,
                      double totalLeftOver) {
        this.id = id;
        this.paymentInfoList = paymentInfoList;
        this.totalIncome = totalIncome;
        this.totalExpenses = totalExpenses;
        this.totalLeftOver = totalLeftOver;
    }

    public Payment convertToEntity() {
        return Payment.builder()
                .id(this.id)
                .paymentInfoList(this.paymentInfoList)
                .totalIncome(this.totalIncome)
                .totalExpenses(this.totalExpenses)
                .totalLeftOver(this.totalLeftOver)
                .build();
    }
}
