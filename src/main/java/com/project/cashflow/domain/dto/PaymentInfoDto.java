package com.project.cashflow.domain.dto;

import com.project.cashflow.domain.Bill;
import com.project.cashflow.domain.Member;
import com.project.cashflow.domain.PaymentInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PaymentInfoDto {

    private UUID id;

    private Member member;

    private List<Bill> payedBills;

    @Positive
    @Range(min = 0, message = "Income cannot be negative")
    private double income = 0;

    @Range(min = 0, message = "Expenses cannot be less than 0")
    private double totalExpenses = 0;

    private double leftOver = 0;

    @Builder
    public PaymentInfoDto(UUID id, Member member,
                          List<Bill> payedBills,
                          double income,
                          double totalExpenses,
                          double leftOver) {
        this.id = id;
        this.member = member;
        this.payedBills = payedBills;
        this.income = income;
        this.totalExpenses = totalExpenses;
        this.leftOver = leftOver;
    }

    public PaymentInfo convertToEntity() {
        return PaymentInfo.builder()
                .id(this.id)
                .member(this.member)
                .payedBills(this.payedBills)
                .income(this.income)
                .totalExpenses(this.totalExpenses)
                .leftOver(this.leftOver)
                .build();
    }
}
