package kr.ac.dankook.CareerApplication.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DownloadCheckListRequest {
    private CheckListFormRequest checkListForm;
    private List<CheckListResultRequest> checkListResultList;
}
