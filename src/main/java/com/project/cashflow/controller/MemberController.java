package com.project.cashflow.controller;

import com.project.cashflow.domain.Member;
import com.project.cashflow.domain.dto.MemberDto;
import com.project.cashflow.exception.EntityExistsException;
import com.project.cashflow.exception.EntityNotFoundException;
import com.project.cashflow.exception.IllegalMemberException;
import com.project.cashflow.exception.TooManyMembersException;
import com.project.cashflow.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public List<MemberDto> getAllMembers() {
        return memberService.findAllMembersByUser()
                .stream()
                .map(Member::convertToDto)
                .collect(Collectors.toList());
    }

    @PostMapping("add")
    public ResponseEntity<?> addMember(@Valid @RequestBody MemberDto memberDto) {
        try {
            Member member = memberService.createMember(memberDto.convertToEntity());
            return ResponseEntity.ok(member.convertToDto());
        } catch (EntityExistsException | TooManyMembersException | EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PatchMapping("change")
    public ResponseEntity<?> updateMember(@Valid @RequestBody MemberDto memberDto) {
        try {
            Member member = memberService.changeMemberName(memberDto.convertToEntity());
            return ResponseEntity.ok(member.convertToDto());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (IllegalMemberException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @DeleteMapping("delete")
    public ResponseEntity<?> deleteMember(@RequestParam String id) {

        try {
            UUID memberUUID = UUID.fromString(id);
            String name = memberService.deleteMemberById(memberUUID);
            return ResponseEntity.ok("Member " + name + " deleted.");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID.");
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalMemberException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }
}
