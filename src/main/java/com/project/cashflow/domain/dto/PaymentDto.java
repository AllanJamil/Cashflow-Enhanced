package com.project.cashflow.domain.dto;

import com.project.cashflow.domain.Payment;
import com.project.cashflow.domain.PaymentDetails;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class PaymentDto {

    private UUID id;

    private List<PaymentDetailsDto> paymentDetailsList;

    @Positive
    @Range(min = 0, message = "Income cannot be negative")
    private double totalIncome;

    @Range(min = 0, message = "Expenses cannot be less than 0")
    private double totalExpenses;

    private double totalLeftOver;

    @Builder
    public PaymentDto(UUID id, List<PaymentDetails> paymentDetailsList,
                      double totalIncome,
                      double totalExpenses,
                      double totalLeftOver) {
        this.id = id;
        this.paymentDetailsList = paymentDetailsList
                .stream()
                .map(PaymentDetails::convertToDto)
                .collect(Collectors.toList());
        this.totalIncome = totalIncome;
        this.totalExpenses = totalExpenses;
        this.totalLeftOver = totalLeftOver;
    }

    public Payment convertToEntity() {
        return Payment.builder()
                .id(this.id)
                .paymentDetailsList(this.paymentDetailsList
                        .stream()
                        .map(PaymentDetailsDto::convertToEntity)
                        .collect(Collectors.toList())
                )
                .totalIncome(this.totalIncome)
                .totalExpenses(this.totalExpenses)
                .totalLeftOver(this.totalLeftOver)
                .build();
    }
}
