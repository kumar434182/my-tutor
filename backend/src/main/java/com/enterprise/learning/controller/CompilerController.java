package com.enterprise.learning.controller;

import com.enterprise.learning.dto.CodeExecutionRequest;
import com.enterprise.learning.dto.CodeExecutionResponse;
import com.enterprise.learning.service.CompilerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/compiler")
@RequiredArgsConstructor
@Tag(name = "Compiler", description = "Online compiler endpoints")
public class CompilerController {

    private final CompilerService compilerService;

    @PostMapping("/execute")
    @PreAuthorize("hasRole('LEARNER') or hasRole('TRAINER')")
    @Operation(summary = "Execute code", description = "Execute source code and return output")
    public ResponseEntity<CodeExecutionResponse> executeCode(@Valid @RequestBody CodeExecutionRequest request) {
        CodeExecutionResponse response = compilerService.executeCode(request);
        return ResponseEntity.ok(response);
    }
}
