package org.tfg.grant_java.response;

import java.util.List;
import lombok.*;

import java.util.UUID;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetQuestionsResponse {
    private List<QuestionSummaryResponse> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
}
