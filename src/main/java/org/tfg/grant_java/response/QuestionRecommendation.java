package org.tfg.grant_java.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QuestionRecommendation {
    private String id;
    private String text;
    private float score;

    public QuestionRecommendation(String id, String text, float score) {
        this.id = id;
        this.text = text;
        this.score = score;
    }
}
