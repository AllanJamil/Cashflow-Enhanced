package com.project.cashflow.domain;

import com.project.cashflow.domain.dto.BillDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "bills")
public class Bill extends BaseEntity{

    @Column(nullable = false)
    @Size(min = 3, max = 30, message = "Name of bill cant be less than 3 or more than 30 characters")
    private String name;

    @Column(nullable = false)
    @Range(min = 1, message = "Bill total must have a value above 0")
    private double total = 0;

    private String note;

    private String ocr;

    private String bankNo;

    @Column(nullable = false)
    private BillType billType;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Member member;

    @ManyToMany(mappedBy = "payedBills")
    private List<PaymentDetails> paymentDetailsList = new ArrayList<>();

    @Builder
    public Bill(UUID id, String name, double total, String note,
                String ocr, String bankNo, BillType billType, Member member) {
        super(id);
        this.name = name;
        this.total = total;
        this.note = note;
        this.ocr = ocr;
        this.bankNo = bankNo;
        this.billType = billType;
        this.member = member;
    }

    public BillDto convertToDto() {
        return BillDto.builder()
                .id(this.getId())
                .name(this.name)
                .total(this.total)
                .note(this.note)
                .ocr(this.ocr)
                .bankNo(this.bankNo)
                .billType(this.billType)
                .build();
    }
}
