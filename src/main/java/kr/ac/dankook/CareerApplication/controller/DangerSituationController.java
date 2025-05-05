package kr.ac.dankook.CareerApplication.controller;

import jakarta.validation.Valid;
import kr.ac.dankook.CareerApplication.document.DangerSituationData;
import kr.ac.dankook.CareerApplication.dto.request.FilterRequest;
import kr.ac.dankook.CareerApplication.dto.response.ApiResponse;
import kr.ac.dankook.CareerApplication.dto.response.KeywordListResponse;
import kr.ac.dankook.CareerApplication.dto.response.KeywordResponse;
import kr.ac.dankook.CareerApplication.exception.ApiErrorCode;
import kr.ac.dankook.CareerApplication.exception.ValidationException;
import kr.ac.dankook.CareerApplication.service.DangerSituationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/danger")
public class DangerSituationController {

    private final DangerSituationService dangerSituationService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<DangerSituationData>>> getAllDangerSituationList(){
        return ResponseEntity.ok(new ApiResponse<>(200,
                dangerSituationService.getAllDangerSituationProcess()));
    }

    @GetMapping("/type")
    public ResponseEntity<ApiResponse<List<String>>> getAllDangerSituationTypeList(){
        return ResponseEntity.ok(new ApiResponse<>(200,
                dangerSituationService.getAllTypesProcess()));
    }

    @GetMapping("/jobType/{type}")
    public ResponseEntity<ApiResponse<List<String>>> getAllDangerSituationTypeListByType(
            @PathVariable String type
    ){
        return ResponseEntity.ok(new ApiResponse<>(200,
                dangerSituationService.getJobTypesByTypeProcess(type)));
    }

    @GetMapping("/subJobType/{jobType}")
    public ResponseEntity<ApiResponse<List<String>>> getAllDangerSituationTypeListByJobType(
            @PathVariable String jobType
    ){
        return ResponseEntity.ok(new ApiResponse<>(200,
                dangerSituationService.getSubJobTypesByJobTypeProcess(jobType)));
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<ApiResponse<KeywordListResponse>> searchByKeyword(
            @PathVariable String keyword){
        List<KeywordResponse> keywordResponses = dangerSituationService.findByKeywordProcess(keyword);
        return ResponseEntity.ok(new ApiResponse<>(200,new KeywordListResponse(keywordResponses,keywordResponses.size())));
    }

    @PostMapping("/filter")
    public ResponseEntity<ApiResponse<List<DangerSituationData>>> getSituationByFilter(
            @RequestBody @Valid FilterRequest filterRequest,
            BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()){
            validateBindingResult(bindingResult);
        }
        return ResponseEntity.ok(new ApiResponse<>(200,
                dangerSituationService.findByFilterProcess(filterRequest)));
    }

    private void validateBindingResult(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(","));
            throw new ValidationException(ApiErrorCode.INVALID_REQUEST,errorMessages);
        }
    }
}
