package kr.ac.dankook.CareerApplication.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import kr.ac.dankook.CareerApplication.document.ChecklistHistory;
import kr.ac.dankook.CareerApplication.dto.request.CheckListResultRequest;
import kr.ac.dankook.CareerApplication.dto.request.CheckListFormRequest;
import kr.ac.dankook.CareerApplication.dto.request.DownloadCheckListRequest;
import kr.ac.dankook.CareerApplication.entity.Member;
import kr.ac.dankook.CareerApplication.exception.ApiErrorCode;
import kr.ac.dankook.CareerApplication.exception.ApiException;
import kr.ac.dankook.CareerApplication.repository.ChecklistHistoryRepository;
import kr.ac.dankook.CareerApplication.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckListService {

    private final ChecklistHistoryRepository checklistHistoryRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<ChecklistHistory> getAllChecklistHistoryProcess(String memberId) {
        return checklistHistoryRepository.findByMemberId(memberId);
    }
    @Transactional
    public boolean deleteChecklistHistoryProcess(String checklistId){
        if (checklistHistoryRepository.existsById(checklistId)){
            checklistHistoryRepository.deleteById(checklistId);
            return true;
        }
        return false;
    }
    @Transactional
    public boolean saveChecklistHistoryProcess(String memberId,DownloadCheckListRequest downloadCheckListRequest){
        List<ChecklistHistory> checklistHistories = checklistHistoryRepository.findByMemberId(memberId);
        CheckListFormRequest checkListFormRequest = downloadCheckListRequest.getCheckListForm();
        for(ChecklistHistory checklistHistory : checklistHistories){
            String targetMemberId = checklistHistory.getMemberId();
            CheckListFormRequest targetForms = checklistHistory.getForms();
            if (memberId.equals(targetMemberId) && targetForms.getType().equals(checkListFormRequest.getType()) &&
                    targetForms.getJobType().equals(checkListFormRequest.getJobType()) &&
                    targetForms.getSubJobType().equals(checkListFormRequest.getSubJobType()) &&
                    targetForms.getReason().equals(checkListFormRequest.getReason()) &&
                    targetForms.getDifficulty().equals(checkListFormRequest.getDifficulty()) &&
                    targetForms.getRiskLevel().equals(checkListFormRequest.getRiskLevel()) &&
                    targetForms.getWorkTime() == checkListFormRequest.getWorkTime()
            ) return false;
        }
        ChecklistHistory checklistHistory = ChecklistHistory.builder()
                .checklist(downloadCheckListRequest.getCheckListResultList())
                .forms(downloadCheckListRequest.getCheckListForm())
                .memberId(memberId)
                .createdAt(LocalDateTime.now())
                .build();
        checklistHistoryRepository.save(checklistHistory);
        return true;
    }

    public void generateDocumentService(
            Long memberId, DownloadCheckListRequest request, OutputStream outputStream)
            throws IOException, DocumentException {
        Optional<Member> member = memberRepository.findById(memberId);
        if (member.isEmpty()) throw new ApiException(ApiErrorCode.MEMBER_NOT_FOUND);
        Member targetMember = member.get();
        targetMember.setDownloadCount(targetMember.getDownloadCount()+1);
        memberRepository.save(targetMember);

        List<CheckListResultRequest> results = request.getCheckListResultList();
        CheckListFormRequest form = request.getCheckListForm();

        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        InputStream fontStream = getClass().getClassLoader().getResourceAsStream("NanumGothic-Bold.ttf");
        assert fontStream != null;
        BaseFont baseFont = BaseFont.createFont("NanumGothic-Bold.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, fontStream.readAllBytes(), null);
        Font titleFont = new Font(baseFont, 16, Font.BOLD);
        Font headerFont = new Font(baseFont, 13, Font.BOLD);
        Font cellFont = new Font(baseFont, 11);

        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Paragraph title = new Paragraph("예방 조치 체크리스트 (" + today + ")", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20f);
        document.add(title);

        addChecklistTable(document, "작업 전", results, baseFont);
        addChecklistTable(document, "작업 중", results, baseFont);
        addChecklistTable(document, "작업 후", results, baseFont);

        document.newPage();
        document.add(Chunk.NEWLINE);
        Paragraph infoTitle = new Paragraph("■ 작업 정보", headerFont);
        infoTitle.setSpacingBefore(10f);
        document.add(infoTitle);

        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidths(new int[]{2, 5});
        infoTable.setSpacingBefore(10f);
        infoTable.setWidthPercentage(100);

        addInfoRow(infoTable, "공종", form.getType(), cellFont);
        addInfoRow(infoTable, "작업명", form.getJobType(), cellFont);
        addInfoRow(infoTable, "단위 작업명", form.getSubJobType(), cellFont);
        addInfoRow(infoTable, "사고 키워드", form.getReason(), cellFont);
        addInfoRow(infoTable, "작업 난이도", form.getDifficulty(), cellFont);
        addInfoRow(infoTable, "위험도", form.getRiskLevel(), cellFont);
        addInfoRow(infoTable, "예상 작업 시간", form.getWorkTime()+"시간", cellFont);

        document.add(infoTable);
        document.close();
    }

    private void addChecklistTable(Document doc, String stage, List<CheckListResultRequest> items, BaseFont bf) throws DocumentException {
        Font headerFont = new Font(bf, 13, Font.BOLD);
        Font tableHeaderFont = new Font(bf, 10, Font.BOLD);
        Font cellFont = new Font(bf, 9);

        List<CheckListResultRequest> sectionItems = items.stream()
                .filter(i -> i.getStage().equals(stage))
                .toList();

        if (sectionItems.isEmpty()) return;

        Paragraph sectionTitle = new Paragraph("■ " + stage + " 체크리스트", headerFont);
        sectionTitle.setSpacingBefore(15f);
        sectionTitle.setSpacingAfter(10f);
        doc.add(sectionTitle);

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1f, 10f, 2f});
        table.setSpacingAfter(10f);

        addTableHeader(table, tableHeaderFont, "", "내용", "중요도");

        for (CheckListResultRequest item : sectionItems) {
            table.addCell(createCheckCell(cellFont));
            table.addCell(createCell(item.getContent(), cellFont,"Normal"));
            table.addCell(createCell(item.getImportance(), cellFont,"Center"));
        }
        doc.add(table);
    }
    private PdfPCell createCell(String text, Font font,String type) {
        Paragraph paragraph = new Paragraph(text, font);
        paragraph.setLeading(15f);

        PdfPCell cell = new PdfPCell(paragraph);
        if (type.equals("Center")) {
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        }
        cell.setPadding(6f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setNoWrap(false);
        cell.setMinimumHeight(20f);
        return cell;
    }

    private PdfPCell createCheckCell(Font font) {
        PdfPCell cell = new PdfPCell(new Phrase("□", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(6f);
        return cell;
    }
    private void addTableHeader(PdfPTable table, Font font, String... headers) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, font));
            cell.setPadding(8f);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
    }

    private void addInfoRow(PdfPTable table, String title, String value, Font font) {
        PdfPCell cell1 = new PdfPCell(new Phrase(title, font));
        cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell1.setPadding(5);
        table.addCell(cell1);

        PdfPCell cell2 = new PdfPCell(new Phrase(value, font));
        cell2.setPadding(5);
        table.addCell(cell2);
    }

}
