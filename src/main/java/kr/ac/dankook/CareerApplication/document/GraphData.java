package kr.ac.dankook.CareerApplication.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;


@Document(collection = "graph_history")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GraphData {
    @Id
    private String id;
    private String category;
    private List<CategoryData> list;
}

