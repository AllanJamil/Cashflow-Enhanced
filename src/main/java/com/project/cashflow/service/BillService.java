package com.project.cashflow.service;

import com.project.cashflow.domain.Bill;
import com.project.cashflow.domain.Member;
import com.project.cashflow.exception.EntityNotFoundException;
import com.project.cashflow.repository.BillRepository;
import com.project.cashflow.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillService {

    private final BillRepository billRepository;
    private final MemberRepository memberRepository;

    public Bill createBill(UUID memberId, @Valid Bill bill) throws EntityNotFoundException {
        String email = CurrentRequest.getUserEmail();

        Member member = this.memberRepository.findByIdAndUser_Email(memberId, email)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find member with id: " + memberId));

        bill.setName(
                bill
                .getName()
                .trim()
                .toUpperCase()
        );
        bill.setMember(member);
        return this.billRepository.save(bill);

    }

    public Bill updateBillById(@Valid Bill bill) throws EntityNotFoundException {
        String email = CurrentRequest.getUserEmail();

        Bill billToUpdate = this.billRepository.findByIdAndMember_User_Email(bill.getId(), email)
                .orElseThrow(() -> new EntityNotFoundException("Bill not found"));

        billToUpdate.setName(bill.getName().trim().toUpperCase());
        billToUpdate.setTotal(bill.getTotal());
        billToUpdate.setNote(bill.getNote());
        billToUpdate.setOcr(bill.getOcr());
        billToUpdate.setBankNo(bill.getBankNo());
        billToUpdate.setBillType(bill.getBillType());

        return this.billRepository.save(billToUpdate);
    }

    public void deleteBill(UUID memberId ,UUID billId) throws EntityNotFoundException {
        String email = CurrentRequest.getUserEmail();

        Member member = this.memberRepository.findByIdAndUser_Email(memberId, email)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find member with id: " + memberId));

        Bill billToDelete = this.billRepository.findByIdAndMember_User_Email(billId, email)
                .orElseThrow(() -> new EntityNotFoundException("Bill not found"));


        member.getBills().remove(billToDelete);
        this.billRepository.delete(billToDelete);
    }

}
