package br.com.gvp.certification.modules.students.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnswerDTO {
    private UUID questionId;
    private UUID alternativeId;
    private boolean isCorrect;
}

// 45:00