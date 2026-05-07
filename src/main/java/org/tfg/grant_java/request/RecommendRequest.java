package org.tfg.grant_java.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RecommendRequest {
    private String questionText;
    private Integer topN;

//    public RecommendRequest() {
//    }
//
//    public String getQuestionText() {
//        return questionText;
//    }
//
//    public void setQuestionText(String questionText) {
//        this.questionText = questionText;
//    }
//
//    public Integer getTopN() {
//        if(topN == null || topN <= 0) { return 5; }
//        return topN;
//    }
//
//    public void setTopN(Integer topN) {
//        this.topN = topN;
//    }
}
