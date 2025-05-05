package kr.ac.dankook.CareerApplication.controller;

import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import kr.ac.dankook.CareerApplication.document.ChecklistHistory;
import kr.ac.dankook.CareerApplication.dto.request.DownloadCheckListRequest;
import kr.ac.dankook.CareerApplication.dto.response.ApiResponse;
import kr.ac.dankook.CareerApplication.exception.ApiErrorCode;
import kr.ac.dankook.CareerApplication.exception.ValidationException;
import kr.ac.dankook.CareerApplication.service.CheckListService;
import kr.ac.dankook.CareerApplication.util.DecryptId;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/checklist")
public class CheckListController {

    private final CheckListService checkListService;

    @PostMapping("/{memberId}")
    public ResponseEntity<ApiResponse<Boolean>> saveChecklist(
            @PathVariable String memberId,
            @RequestBody DownloadCheckListRequest downloadCheckListRequest,
            BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            validateBindingResult(bindingResult);
        }
        return ResponseEntity.ok(new ApiResponse<>(200,
                checkListService.saveChecklistHistoryProcess(memberId,downloadCheckListRequest)));
    }
    @GetMapping("/{memberId}")
    public ResponseEntity<ApiResponse<List<ChecklistHistory>>> getAllChecklist(
            @PathVariable String memberId
    ){
        return ResponseEntity.ok(new ApiResponse<>(200,
                checkListService.getAllChecklistHistoryProcess(memberId)));
    }
    @DeleteMapping("/{checklistId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteChecklist(
            @PathVariable String checklistId){
        return ResponseEntity.ok(new ApiResponse<>(200,
                checkListService.deleteChecklistHistoryProcess(checklistId)));
    }

    @PostMapping("/download/pdf/{memberId}")
    public void downloadPdf(
            @PathVariable @DecryptId Long memberId,
            @RequestBody DownloadCheckListRequest downloadCheckListRequest,
            BindingResult bindingResult,
            HttpServletResponse response) throws IOException, DocumentException {
        if (bindingResult.hasErrors()) {
            validateBindingResult(bindingResult);
        }
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=checklist.pdf");
        checkListService.generateDocumentService(memberId,
                downloadCheckListRequest, response.getOutputStream());
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