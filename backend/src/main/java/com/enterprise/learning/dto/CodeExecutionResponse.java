package com.enterprise.learning.dto;

import com.enterprise.learning.entity.CodeSubmission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeExecutionResponse {
    private CodeSubmission.ExecutionStatus status;
    private String output;
    private String errorMessage;
    private CodeSubmission.ProgrammingLanguage language;
    private Double executionTime;
    private Integer testCasesPassed;
    private Integer totalTestCases;
}
