package com.project.cashflow.domain;

import com.project.cashflow.domain.dto.PaymentDetailsDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "paymentinfos")
public class PaymentDetails extends BaseEntity{

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private Member member;

    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "paymentInfo_id"), inverseJoinColumns = @JoinColumn(name = "bill_id"))
    private List<Bill> payedBills = new ArrayList<>();

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private Payment payment;


    @Column(nullable = false, updatable = false)
    private double income = 0;

    @Column(nullable = false, updatable = false)
    private double totalExpenses = 0;

    @Column(nullable = false, updatable = false)
    private double sharedExpenses = 0;

    @Column(nullable = false, updatable = false)
    private double leftOver = 0;

    @Column(nullable = false, updatable = false)
    private double myExpenses;

    @Builder
    public PaymentDetails(UUID id,
                          Member member,
                          List<Bill> payedBills,
                          Payment payment,
                          double income,
                          double totalExpenses,
                          double leftOver,
                          double sharedExpenses,
                          double myExpenses) {
        super(id);
        this.member = member;
        this.payedBills = payedBills;
        this.payment = payment;
        this.income = income;
        this.totalExpenses = totalExpenses;
        this.leftOver = leftOver;
        this.sharedExpenses = sharedExpenses;
        this.myExpenses = myExpenses;
    }

    public PaymentDetailsDto convertToDto() {
        return PaymentDetailsDto.builder()
                .id(this.getId())
                .member(this.member)
                .payedBills(this.payedBills)
                .income(this.income)
                .myExpenses(this.myExpenses)
                .sharedExpenses(this.sharedExpenses)
                .totalExpenses(this.totalExpenses)
                .leftOver(this.leftOver)
                .build();
    }

    // might keep, adding all the totals from payed bills
/*    public double calculateTotal() {
        return this.payedBills.parallelStream().mapToDouble(Bill::getTotal).sum();
    }*/
}
