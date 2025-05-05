package kr.ac.dankook.CareerApplication.config.converter;

import kr.ac.dankook.CareerApplication.dto.response.MemberResponse;
import kr.ac.dankook.CareerApplication.entity.Member;
import kr.ac.dankook.CareerApplication.exception.ApiErrorCode;
import kr.ac.dankook.CareerApplication.exception.ApiException;
import kr.ac.dankook.CareerApplication.repository.MemberRepository;
import kr.ac.dankook.CareerApplication.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MemberEntityConverter {

    private final MemberRepository memberRepository;

    private Member getMemberFromRepository(Long memberId){
        Optional<Member> targetMember = memberRepository.findById(memberId);
        if(targetMember.isPresent()){
            return targetMember.get();
        }
        throw new ApiException(ApiErrorCode.MEMBER_NOT_FOUND);
    }

    @Transactional(readOnly = true)
    public Member getMemberByMemberId(Long memberId){
        return getMemberFromRepository(memberId);
    }


    public MemberResponse convertMemberEntity(Member member){
        return MemberResponse.builder()
                .id(EncryptionUtil.encrypt(member.getId()))
                .name(member.getName())
                .userId(member.getUserId())
                .email(member.getEmail())
                .createTime(member.getCreatedDateTime())
                .roles(member.getRoles())
                .downloadCount(member.getDownloadCount())
                .build();
    }
}
