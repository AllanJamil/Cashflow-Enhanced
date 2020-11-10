package com.project.cashflow.controller;

import com.project.cashflow.domain.Bill;
import com.project.cashflow.domain.dto.BillDto;
import com.project.cashflow.exception.EntityNotFoundException;
import com.project.cashflow.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/bills")
public class BillController {

    private final BillService billService;

    @PostMapping("add")
    public ResponseEntity<?> addBill(@RequestParam String memberId, @Valid @RequestBody BillDto billDto) {
        try {
            UUID memberUUID = UUID.fromString(memberId);
            Bill bill = this.billService.createBill(memberUUID, billDto.convertToEntity());
            return ResponseEntity.ok(bill.convertToDto());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id");
        }
        catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PatchMapping("change")
    public ResponseEntity<?> changeBill(@Valid @RequestBody BillDto billDto) {

        try {
            Bill bill = this.billService.updateBillById(billDto.convertToEntity());
            return ResponseEntity.ok(bill.convertToDto());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("delete/{memberId}/{billId}")
    public ResponseEntity<?> removeBill(@PathVariable String memberId, @PathVariable String billId) {

        try {
            UUID memberUUID = UUID.fromString(memberId);
            UUID billUUID = UUID.fromString(billId);
            this.billService.deleteBill(memberUUID, billUUID);
            return ResponseEntity.ok("Bill deleted.");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }
}
