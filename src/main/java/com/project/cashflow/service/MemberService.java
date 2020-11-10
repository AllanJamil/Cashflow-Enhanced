package com.project.cashflow.service;

import com.project.cashflow.domain.Member;
import com.project.cashflow.domain.User;
import com.project.cashflow.exception.EntityExistsException;
import com.project.cashflow.exception.EntityNotFoundException;
import com.project.cashflow.exception.IllegalMemberException;
import com.project.cashflow.exception.TooManyMembersException;
import com.project.cashflow.repository.MemberRepository;
import com.project.cashflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final String ILLEGAL_MEMBER = "HOUSEHOLD";

    public Member createMember(@Valid Member member) throws EntityExistsException, TooManyMembersException, EntityNotFoundException {
        String email = CurrentRequest.getUserEmail();
        member.setName(member.getName().toUpperCase().trim());

        List<Member> members = this.memberRepository.findAllByUser_Email(email);

        Optional<Member> optionalMember = members.stream()
                .filter(member1 -> member1.getName().equalsIgnoreCase(member.getName())).findAny();

        if (optionalMember.isPresent())
            throw new EntityExistsException("Cannot add a duplicate member. Name must be unique.");

        if (members.size() == 11)
            throw new TooManyMembersException();

        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));

        member.setUser(user);
        return this.memberRepository.save(member);
    }

    public Member changeMemberName(@Valid Member member) throws EntityNotFoundException,
            IllegalMemberException {

        String email = CurrentRequest.getUserEmail();

        Member memberToUpdate = this.memberRepository.findByIdAndUser_Email(member.getId(), email)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find member with ID: " + member.getId()));

        if (memberToUpdate.getName().equalsIgnoreCase(this.ILLEGAL_MEMBER))
            throw new IllegalMemberException(
                    String.format("Forbidden selection of Member ''%s'' to change", this.ILLEGAL_MEMBER)
            );

        memberToUpdate.setName(member.getName().toUpperCase().trim());
        return this.memberRepository.save(memberToUpdate);
    }

    public String deleteMemberById(UUID memberId) throws EntityNotFoundException, IllegalMemberException {
        String email = CurrentRequest.getUserEmail();

        Member memberToDelete = this.memberRepository.findByIdAndUser_Email(memberId, email)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find a member with ID: " + memberId));

        if (memberToDelete.getName().equalsIgnoreCase(this.ILLEGAL_MEMBER))
            throw new IllegalMemberException(
                    String.format("Forbidden selection of Member ''%s'' to delete", this.ILLEGAL_MEMBER)
            );

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));

        user.getMembers().remove(memberToDelete);
        memberRepository.deleteById(memberId);
        return memberToDelete.getName();
    }

    public List<Member> findAllMembersByUser() {
        String email =  CurrentRequest.getUserEmail();
        return memberRepository.findAllByUser_Email(email);
    }
}
