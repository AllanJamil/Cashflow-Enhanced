package com.project.cashflow.domain.dto;

import com.project.cashflow.domain.Bill;
import com.project.cashflow.domain.BillType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@Setter
public class BillDto {

    private UUID id;

    @Size(min = 3, max = 30, message = "Name of bill cant be less than 3 or more than 30 characters")
    private String name;

    @Range(min = 1, message = "Bill total must have a value above 0")
    private double total = 0;

    private String note;

    private String ocr;

    private String bankNo;

    private BillType billType;

    @Builder
    public BillDto(UUID id, String name, double total, String note,
                   String ocr, String bankNo, BillType billType) {
        this.id = id;
        this.name = name;
        this.total = total;
        this.note = note;
        this.ocr = ocr;
        this.bankNo = bankNo;
        this.billType = billType;
    }

    public Bill convertToEntity() {
        return Bill.builder()
                .id(this.id)
                .name(this.name)
                .total(this.total)
                .note(this.note)
                .ocr(this.ocr)
                .bankNo(this.bankNo)
                .billType(this.billType)
                .build();
    }

}
