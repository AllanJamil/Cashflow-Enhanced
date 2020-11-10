package com.project.cashflow.domain.dto;

import com.project.cashflow.domain.Bill;
import com.project.cashflow.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class MemberDto {

    private UUID id;

    @Size(min = 3, max = 30, message = "Name needs to have at least 3 and maximum 30 characters")
    private String name;

    @Size(max = 50, message = "Member cannot have more than 50 bills")
    private List<BillDto> bills = new ArrayList<>();

    @Builder
    public MemberDto(UUID id, String name, List<Bill> bills) {
        this.id = id;
        this.name = name;
        this.bills = bills
                .stream()
                .map(Bill::convertToDto)
                .collect(Collectors.toList()
                );
    }

    public Member convertToEntity() {
        return Member.builder()
                .id(this.id)
                .name(this.name)
                .bills(this.bills
                        .stream()
                        .map(BillDto::convertToEntity)
                        .collect(Collectors.toList())
                )
                .build();
    }
}
