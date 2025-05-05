package kr.ac.dankook.CareerApplication.service;

import kr.ac.dankook.CareerApplication.config.converter.MemberEntityConverter;
import kr.ac.dankook.CareerApplication.document.ChecklistHistory;
import kr.ac.dankook.CareerApplication.document.DiagnosisHistory;
import kr.ac.dankook.CareerApplication.dto.request.FindIdRequest;
import kr.ac.dankook.CareerApplication.entity.LLMChatSection;
import kr.ac.dankook.CareerApplication.entity.Member;
import kr.ac.dankook.CareerApplication.repository.ChecklistHistoryRepository;
import kr.ac.dankook.CareerApplication.repository.DiagnosisHistoryRepository;
import kr.ac.dankook.CareerApplication.repository.LLMChatSectionRepository;
import kr.ac.dankook.CareerApplication.repository.MemberRepository;
import kr.ac.dankook.CareerApplication.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final ChecklistHistoryRepository checklistHistoryRepository;
    private final DiagnosisHistoryRepository diagnosisHistoryRepository;
    private final LLMChatSectionRepository llmChatSectionRepository;
    private final LLMChatSectionService llmChatSectionService;
    private final MemberEntityConverter memberEntityConverter;

    @Transactional
    public boolean deleteMemberProcess(Long memberId){

        String encryptId = EncryptionUtil.encrypt(memberId);

        List<ChecklistHistory> checklistHistories = checklistHistoryRepository.findByMemberId(encryptId);
        checklistHistoryRepository.deleteAll(checklistHistories);
        List<DiagnosisHistory> diagnosisHistories = diagnosisHistoryRepository.findByMemberId(encryptId);
        diagnosisHistoryRepository.deleteAll(diagnosisHistories);

        List<LLMChatSection> llmChatSections = llmChatSectionRepository.findByMember(
                memberEntityConverter.getMemberByMemberId(memberId)
        );
        for(LLMChatSection llmChatSection : llmChatSections){
            llmChatSectionService.deleteChatSectionProcess(llmChatSection.getId());
        }
        memberRepository.deleteById(memberId);
        return true;
    }

    @Transactional(readOnly = true)
    public List<String> findUserIdProcess(FindIdRequest findIdRequest){
        return memberRepository.findByNameAndEmail(findIdRequest.getName(), findIdRequest.getEmail())
                .stream()
                .map(Member::getUserId)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Member findMemberByUserIdProcess(String userId){
        Optional<Member> member = memberRepository.findByUserId(userId);
        return member.orElse(null);
    }
}
