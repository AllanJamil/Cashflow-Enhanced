package com.project.cashflow.service;

import com.project.cashflow.domain.*;
import com.project.cashflow.exception.EntityNotFoundException;
import com.project.cashflow.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;
    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final PaymentInfoRepository paymentInfoRepository;


    public Payment createPayment(@Valid List<PaymentInfo> paymentInfos) throws EntityNotFoundException {
        String email = CurrentRequest.getUserEmail();

        //foreach paymentInfo we have to check if
        //every member truly is connected to the user
        //then check if bills is truly connected to the member
        double HOUSEHOLD_EXPENSES = 0;
        for (PaymentInfo paymentInfo: paymentInfos) {

            UUID memberId = paymentInfo.getMember().getId();
            Member member = memberRepository.findByIdAndUser_Email(memberId, email)
                    .orElseThrow(() -> new EntityNotFoundException("Cannot find member with id:" + memberId));

            for (Bill bill: paymentInfo.getPayedBills()) {
                billRepository.findByIdAndMember_Id(bill.getId(), memberId)
                        .orElseThrow(() -> new EntityNotFoundException("Cannot find bill with id:" + bill.getId()));
            }

            double myExpenses = paymentInfo.getPayedBills().stream()
                    .mapToDouble(Bill::getTotal)
                    .sum();

            paymentInfo.setMyExpenses(myExpenses);

            if (paymentInfo.getMember().getName().equalsIgnoreCase("HOUSEHOLD")) {
                paymentInfo.setTotalExpenses(paymentInfo.getMyExpenses());
                paymentInfo.setMyExpenses(0);
                HOUSEHOLD_EXPENSES = paymentInfo.getTotalExpenses() / ((double) paymentInfos.size() - 1);
            }
        }


        double sharedExpense = HOUSEHOLD_EXPENSES;
        paymentInfos.forEach(paymentInfo -> {
            if (!paymentInfo.getMember().getName().equalsIgnoreCase("HOUSEHOLD")) {
                paymentInfo.setSharedExpenses(sharedExpense);
                paymentInfo.setTotalExpenses(paymentInfo.getMyExpenses() + sharedExpense);
                paymentInfo.setLeftOver(paymentInfo.getIncome() - paymentInfo.getTotalExpenses());
            }
        });

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find user."));

        Payment payment = Payment.builder()
                .user(user)
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        paymentInfos.forEach(paymentInfo -> paymentInfo.setPayment(savedPayment));

        double totalIncome = paymentInfos.stream()
                .filter(paymentInfo ->
            !paymentInfo.getMember().getName().equalsIgnoreCase("HOUSEHOLD")
        ).mapToDouble(PaymentInfo::getIncome).sum();

        double totalExpenses = paymentInfos.stream()
                .filter(paymentInfo ->
                !paymentInfo.getMember().getName().equalsIgnoreCase("HOUSEHOLD")
        ).mapToDouble(PaymentInfo::getTotalExpenses).sum();

        double totalLeftOver = paymentInfos.stream()
                .filter(paymentInfo ->
                !paymentInfo.getMember().getName().equalsIgnoreCase("HOUSEHOLD")
        ).mapToDouble(PaymentInfo::getLeftOver).sum();

        savedPayment.setTotalIncome(totalIncome);
        savedPayment.setTotalExpenses(totalExpenses);
        savedPayment.setTotalLeftOver(totalLeftOver);

        List<PaymentInfo> paymentInfosFromDB = new ArrayList<>();
        paymentInfos.forEach(paymentInfo -> {
            PaymentInfo piSaved = paymentInfoRepository.save(paymentInfo);
            paymentInfosFromDB.add(piSaved);
        });


        Payment paymentUpdated = paymentRepository.save(savedPayment);
        paymentUpdated.setPaymentInfoList(paymentInfosFromDB);
        return paymentUpdated;
    }

    public List<Payment> getAllPaymentsByUser() {
        String email = CurrentRequest.getUserEmail();

        return paymentRepository.findAllByUser_Email(email);
    }

}
