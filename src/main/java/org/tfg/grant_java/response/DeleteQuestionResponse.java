package org.tfg.grant_java.response;

import lombok.*;

import java.util.UUID;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteQuestionResponse {
    private UUID id;
}
