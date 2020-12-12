package com.project.cashflow.domain.dto;

import com.project.cashflow.domain.Bill;
import com.project.cashflow.domain.Member;
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
public class PaymentDetailsDto {

    private UUID id;

    private MemberDto member;

    private List<BillDto> payedBills;

    @Positive
    @Range(min = 0, message = "Income cannot be negative")
    private double income;

    @Range(min = 0, message = "Expenses cannot be less than 0")
    private double totalExpenses;

    private double sharedExpenses;

    private double myExpenses;

    private double leftOver;

    @Builder
    public PaymentDetailsDto(UUID id, Member member,
                             List<Bill> payedBills,
                             double income,
                             double totalExpenses,
                             double leftOver,
                             double sharedExpenses,
                             double myExpenses) {
        this.id = id;
        this.member = member.convertToDto();
        this.payedBills = payedBills.stream()
                .map(Bill::convertToDto)
                .collect(Collectors.toList());
        this.income = income;
        this.totalExpenses = totalExpenses;
        this.leftOver = leftOver;
        this.sharedExpenses = sharedExpenses;
        this.myExpenses = myExpenses;
    }

    public PaymentDetails convertToEntity() {
        return PaymentDetails.builder()
                .id(this.id)
                .member(this.member.convertToEntity())
                .payedBills(this.payedBills
                        .stream()
                        .map(BillDto::convertToEntity)
                        .collect(Collectors.toList())
                )
                .income(this.income)
                .myExpenses(this.myExpenses)
                .sharedExpenses(this.sharedExpenses)
                .totalExpenses(this.totalExpenses)
                .leftOver(this.leftOver)
                .build();
    }
}
