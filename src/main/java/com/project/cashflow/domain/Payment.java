package com.project.cashflow.domain;


import com.project.cashflow.domain.dto.PaymentDto;
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
@Table(name = "payments")
public class Payment extends BaseEntity {


    @ManyToOne
    private User user;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "payment")
    private List<PaymentDetails> paymentDetailsList = new ArrayList<>();

    @Column(nullable = false)
    private double totalExpenses; //= calculateTotalExpenses();

    @Column(nullable = false)
    private double totalIncome; // = calculateTotalIncome();

    @Column(nullable = false)
    private double totalLeftOver;

    @Builder
    public Payment(UUID id, User user, List<PaymentDetails> paymentDetailsList, double totalExpenses, double totalIncome, double totalLeftOver) {
        super(id);
        this.user = user;
        this.paymentDetailsList = paymentDetailsList;
        this.totalExpenses = totalExpenses;
        this.totalIncome = totalIncome;
        this.totalLeftOver = totalLeftOver;
    }

    public PaymentDto convertToDto() {
        return PaymentDto.builder()
                .id(this.getId())
                .paymentDetailsList(this.paymentDetailsList)
                .totalIncome(this.totalIncome)
                .totalExpenses(this.totalExpenses)
                .totalLeftOver(this.totalLeftOver)
                .build();
    }

/*    private double calculateTotalExpenses() {
        if (paymentInfoList.isEmpty())
            throw new RuntimeException("Cannot calculate if list of payments dont exist");
        return this.paymentInfoList.stream().mapToDouble(PaymentInfo::getTotalExpenses).sum();
    }

    private double calculateTotalIncome() {
        if (paymentInfoList.isEmpty())
            throw new RuntimeException("Cannot calculate if list of payments dont exist");
        return this.paymentInfoList.stream().mapToDouble(PaymentInfo::getIncome).sum();
    }*/
}
