package com.enterprise.learning.dto;

import com.enterprise.learning.entity.CodeSubmission;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeExecutionRequest {
    @NotBlank(message = "Source code cannot be blank")
    private String sourceCode;

    @NotBlank(message = "Language must be specified")
    private CodeSubmission.ProgrammingLanguage language;

    private String input;
}
