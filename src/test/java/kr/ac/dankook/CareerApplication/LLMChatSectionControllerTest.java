//package kr.ac.dankook.CareerApplication;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.transaction.Transactional;
//import kr.ac.dankook.CareerApplication.config.converter.MemberEntityConverter;
//import kr.ac.dankook.CareerApplication.controller.LLMChatSectionController;
//import kr.ac.dankook.CareerApplication.dto.response.llm.LLMCreateNewSectionResponse;
//import kr.ac.dankook.CareerApplication.entity.Member;
//import kr.ac.dankook.CareerApplication.repository.MemberRepository;
//import kr.ac.dankook.CareerApplication.service.LLMChatSectionService;
//import kr.ac.dankook.CareerApplication.util.EncryptionUtil;
//import org.junit.jupiter.api.*;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class LLMChatSectionControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private Long realMemberId;
//
//    @TestConfiguration
//    static class MockConfig {
//
//        @Bean
//        public LLMChatSectionService llmChatSectionService() {
//            return Mockito.mock(LLMChatSectionService.class);
//        }
//
//        @Bean
//        public MemberEntityConverter memberEntityConverter() {
//            return Mockito.mock(MemberEntityConverter.class);
//        }
//    }
//    @BeforeAll
//    void setup() {
//        Member member = Member.builder()
//                .email("test@dankook.ac.kr")
//                .name("Test")
//                .build();
//        memberRepository.save(member);
//        realMemberId = member.getId();
//    }
//
//    @Test
//    @DisplayName("[ Test Save ChatSection ]")
//    @Transactional
//    void testSaveChatSection_withDecryptId() throws Exception {
//        mockMvc.perform(post("/llm/chat-section/{memberId}", realMemberId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.data.chatSectionId").isNumber());
//    }
//    @AfterAll
//    void cleanup() {
//        memberRepository.deleteById(realMemberId);
//    }
//}
