package com.project.cashflow.controller;

import com.project.cashflow.domain.Payment;
import com.project.cashflow.domain.PaymentInfo;
import com.project.cashflow.domain.dto.PaymentDto;
import com.project.cashflow.domain.dto.PaymentInfoDto;
import com.project.cashflow.exception.EntityNotFoundException;
import com.project.cashflow.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public List<PaymentDto> getPayments() {
        return paymentService.getAllPaymentsByUser().stream()
                .map(Payment::convertToDto)
                .collect(Collectors.toList());
    }

    @PostMapping("create-payment")
    public PaymentDto makePayment(@Valid @RequestBody List<PaymentInfoDto> paymentInfoDtos) {
        List<PaymentInfo> payments = paymentInfoDtos.stream()
                .map(PaymentInfoDto::convertToEntity)
                .collect(Collectors.toList());

        try {
             return paymentService.createPayment(payments)
                     .convertToDto();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
