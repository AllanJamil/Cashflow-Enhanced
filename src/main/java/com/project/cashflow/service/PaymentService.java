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


    public Payment createPayment(@Valid List<PaymentDetails> paymentDetails) throws EntityNotFoundException {
        String email = CurrentRequest.getUserEmail();

        //foreach paymentInfo we have to check if
        //every member truly is connected to the user
        //then check if bills is truly connected to the member
        double HOUSEHOLD_EXPENSES = 0;
        for (PaymentDetails paymentDetail : paymentDetails) {

            UUID memberId = paymentDetail.getMember().getId();
            Member member = memberRepository.findByIdAndUser_Email(memberId, email)
                    .orElseThrow(() -> new EntityNotFoundException("Cannot find member with id:" + memberId));

            for (Bill bill: paymentDetail.getPayedBills()) {
                billRepository.findByIdAndMember_Id(bill.getId(), memberId)
                        .orElseThrow(() -> new EntityNotFoundException("Cannot find bill with id:" + bill.getId()));
            }

            double myExpenses = paymentDetail.getPayedBills().stream()
                    .mapToDouble(Bill::getTotal)
                    .sum();

            paymentDetail.setMyExpenses(myExpenses);

            if (paymentDetail.getMember().getName().equalsIgnoreCase("HOUSEHOLD")) {
                paymentDetail.setTotalExpenses(paymentDetail.getMyExpenses());
                paymentDetail.setMyExpenses(0);
                HOUSEHOLD_EXPENSES = paymentDetail.getTotalExpenses() / ((double) paymentDetails.size() - 1);
            }
        }


        double sharedExpense = HOUSEHOLD_EXPENSES;
        paymentDetails.forEach(paymentInfo -> {
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

        paymentDetails.forEach(paymentInfo -> paymentInfo.setPayment(savedPayment));

        double totalIncome = paymentDetails.stream()
                .filter(paymentInfo ->
            !paymentInfo.getMember().getName().equalsIgnoreCase("HOUSEHOLD")
        ).mapToDouble(PaymentDetails::getIncome).sum();

        double totalExpenses = paymentDetails.stream()
                .filter(paymentInfo ->
                !paymentInfo.getMember().getName().equalsIgnoreCase("HOUSEHOLD")
        ).mapToDouble(PaymentDetails::getTotalExpenses).sum();

        double totalLeftOver = paymentDetails.stream()
                .filter(paymentInfo ->
                !paymentInfo.getMember().getName().equalsIgnoreCase("HOUSEHOLD")
        ).mapToDouble(PaymentDetails::getLeftOver).sum();

        savedPayment.setTotalIncome(totalIncome);
        savedPayment.setTotalExpenses(totalExpenses);
        savedPayment.setTotalLeftOver(totalLeftOver);

        List<PaymentDetails> paymentInfosFromDB = new ArrayList<>();
        paymentDetails.forEach(paymentInfo -> {
            PaymentDetails piSaved = paymentInfoRepository.save(paymentInfo);
            paymentInfosFromDB.add(piSaved);
        });


        Payment paymentUpdated = paymentRepository.save(savedPayment);
        paymentUpdated.setPaymentDetailsList(paymentInfosFromDB);
        return paymentUpdated;
    }

    public List<Payment> getAllPaymentsByUser() {
        String email = CurrentRequest.getUserEmail();

        return paymentRepository.findAllByUser_Email(email);
    }

}
