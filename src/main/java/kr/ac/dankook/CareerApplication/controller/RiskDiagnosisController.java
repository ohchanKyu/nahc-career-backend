package kr.ac.dankook.CareerApplication.controller;

import kr.ac.dankook.CareerApplication.document.DiagnosisHistory;
import kr.ac.dankook.CareerApplication.dto.request.DiagnosisSaveRequest;
import kr.ac.dankook.CareerApplication.dto.response.ApiResponse;
import kr.ac.dankook.CareerApplication.dto.response.RiskDiagnosisResultResponse;
import kr.ac.dankook.CareerApplication.exception.ApiErrorCode;
import kr.ac.dankook.CareerApplication.exception.ValidationException;
import kr.ac.dankook.CareerApplication.service.RiskDiagnosisService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diagnosis")
public class RiskDiagnosisController {

    private final RiskDiagnosisService riskDiagnosisService;

    @GetMapping("/{memberId}")
    public ResponseEntity<ApiResponse<List<DiagnosisHistory>>> getDiagnosisResult(
            @PathVariable String memberId
    ){
        return ResponseEntity.ok(new ApiResponse<>(200,
                riskDiagnosisService.getAllDiagnosisResultProcess(memberId)));
    }
    @PostMapping("/save/{memberId}")
    public ResponseEntity<ApiResponse<Boolean>> saveDiagnosisResult(
            @PathVariable String memberId,
            @RequestBody DiagnosisSaveRequest diagnosisSaveRequest,
            BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            validateBindingResult(bindingResult);
        }
        return ResponseEntity.ok(new ApiResponse<>(200,riskDiagnosisService.saveDiagnosisProcess(
                memberId,diagnosisSaveRequest
        )));
    }
    @DeleteMapping("/{diagnosisId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteDiagnosisResult(
            @PathVariable String diagnosisId){
        return ResponseEntity.ok(new ApiResponse<>(200,
                riskDiagnosisService.deleteDiagnosisResultProcess(diagnosisId)));
    }

    @PostMapping("/{jobType}")
    public ResponseEntity<ApiResponse<RiskDiagnosisResultResponse>> fullDiagnosis(
            @PathVariable String jobType,
            @RequestBody String description,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            validateBindingResult(bindingResult);
        }

        RiskDiagnosisResultResponse result = riskDiagnosisService.fullDiagnosisProcess(jobType, description);
        return ResponseEntity.ok(new ApiResponse<>(200,result));
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
