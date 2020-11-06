package com.project.cashflow.domain;

import com.project.cashflow.domain.dto.PaymentInfoDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "paymentinfos")
public class PaymentInfo extends BaseEntity{

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private Member member;

    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "paymentInfo_id"), inverseJoinColumns = @JoinColumn(name = "bill_id"))
    private List<Bill> payedBills = new ArrayList<>();

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private Payment payment;

    @Positive
    @Range(min = 0, message = "Income cannot be negative")
    @Column(nullable = false, updatable = false)
    private double income = 0;

    @Column(nullable = false, updatable = false)
    @Range(min = 0, message = "Expenses cannot be less than 0")
    private double totalExpenses = 0;

    @Column(nullable = false, updatable = false)
    private double leftOver = 0;

    @Builder
    public PaymentInfo(UUID id,
                       Member member,
                       List<Bill> payedBills,
                       Payment payment,
                       double income,
                       double totalExpenses,
                       double leftOver) {
        super(id);
        this.member = member;
        this.payedBills = payedBills;
        this.payment = payment;
        this.income = income;
        this.totalExpenses = totalExpenses;
        this.leftOver = leftOver;
    }

    public PaymentInfoDto convertToDto() {
        return PaymentInfoDto.builder()
                .id(this.getId())
                .member(this.member)
                .payedBills(this.payedBills)
                .income(this.income)
                .totalExpenses(this.totalExpenses)
                .leftOver(this.leftOver)
                .build();
    }

    // might keep, adding all the totals from payed bills
/*    public double calculateTotal() {
        return this.payedBills.parallelStream().mapToDouble(Bill::getTotal).sum();
    }*/
}
