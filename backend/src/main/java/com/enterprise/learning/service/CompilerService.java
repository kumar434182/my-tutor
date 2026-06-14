package com.enterprise.learning.service;

import com.enterprise.learning.dto.CodeExecutionRequest;
import com.enterprise.learning.dto.CodeExecutionResponse;
import com.enterprise.learning.entity.CodeSubmission;
import com.enterprise.learning.repository.CodeSubmissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CompilerService {

    private final CodeSubmissionRepository codeSubmissionRepository;
    private static final long EXECUTION_TIMEOUT = 10;
    private static final String TEMP_DIR = "/tmp/compiler";

    public CodeExecutionResponse executeCode(CodeExecutionRequest request) {
        try {
            // Create temporary directory
            Files.createDirectories(Paths.get(TEMP_DIR));

            CodeExecutionResponse response = new CodeExecutionResponse();
            response.setLanguage(request.getLanguage());

            switch (request.getLanguage()) {
                case JAVA:
                    response = executeJava(request);
                    break;
                case PYTHON:
                    response = executePython(request);
                    break;
                case JAVASCRIPT:
                    response = executeJavaScript(request);
                    break;
                default:
                    response.setStatus(CodeSubmission.ExecutionStatus.COMPILATION_ERROR);
                    response.setErrorMessage("Language not supported");
            }

            return response;
        } catch (Exception e) {
            log.error("Error executing code", e);
            CodeExecutionResponse response = new CodeExecutionResponse();
            response.setStatus(CodeSubmission.ExecutionStatus.RUNTIME_ERROR);
            response.setErrorMessage(e.getMessage());
            return response;
        }
    }

    private CodeExecutionResponse executeJava(CodeExecutionRequest request) throws Exception {
        CodeExecutionResponse response = new CodeExecutionResponse();
        String className = "Solution";
        String fileName = className + ".java";
        Path filePath = Paths.get(TEMP_DIR, fileName);

        // Write source code to file
        Files.write(filePath, request.getSourceCode().getBytes());

        try {
            // Compile
            ProcessBuilder compileBuilder = new ProcessBuilder("javac", filePath.toString());
            Process compileProcess = compileBuilder.start();
            boolean compiled = compileProcess.waitFor(5, TimeUnit.SECONDS);

            if (!compiled) {
                response.setStatus(CodeSubmission.ExecutionStatus.TIMEOUT);
                response.setErrorMessage("Compilation timeout");
                return response;
            }

            if (compileProcess.exitValue() != 0) {
                String error = new String(compileProcess.getErrorStream().readAllBytes());
                response.setStatus(CodeSubmission.ExecutionStatus.COMPILATION_ERROR);
                response.setErrorMessage(error);
                return response;
            }

            // Execute
            ProcessBuilder runBuilder = new ProcessBuilder("java", "-cp", TEMP_DIR, className);
            Process runProcess = runBuilder.start();
            boolean executed = runProcess.waitFor(EXECUTION_TIMEOUT, TimeUnit.SECONDS);

            if (!executed) {
                runProcess.destroy();
                response.setStatus(CodeSubmission.ExecutionStatus.TIMEOUT);
                response.setErrorMessage("Execution timeout");
                return response;
            }

            String output = new String(runProcess.getInputStream().readAllBytes());
            String error = new String(runProcess.getErrorStream().readAllBytes());

            if (runProcess.exitValue() == 0) {
                response.setStatus(CodeSubmission.ExecutionStatus.SUCCESS);
                response.setOutput(output);
            } else {
                response.setStatus(CodeSubmission.ExecutionStatus.RUNTIME_ERROR);
                response.setErrorMessage(error);
            }

        } finally {
            // Cleanup
            Files.deleteIfExists(filePath);
            Files.deleteIfExists(Paths.get(TEMP_DIR, className + ".class"));
        }

        return response;
    }

    private CodeExecutionResponse executePython(CodeExecutionRequest request) throws Exception {
        CodeExecutionResponse response = new CodeExecutionResponse();
        String fileName = "solution.py";
        Path filePath = Paths.get(TEMP_DIR, fileName);

        Files.write(filePath, request.getSourceCode().getBytes());

        try {
            ProcessBuilder builder = new ProcessBuilder("python3", filePath.toString());
            Process process = builder.start();
            boolean executed = process.waitFor(EXECUTION_TIMEOUT, TimeUnit.SECONDS);

            if (!executed) {
                process.destroy();
                response.setStatus(CodeSubmission.ExecutionStatus.TIMEOUT);
                response.setErrorMessage("Execution timeout");
                return response;
            }

            String output = new String(process.getInputStream().readAllBytes());
            String error = new String(process.getErrorStream().readAllBytes());

            if (process.exitValue() == 0) {
                response.setStatus(CodeSubmission.ExecutionStatus.SUCCESS);
                response.setOutput(output);
            } else {
                response.setStatus(CodeSubmission.ExecutionStatus.RUNTIME_ERROR);
                response.setErrorMessage(error);
            }
        } finally {
            Files.deleteIfExists(filePath);
        }

        return response;
    }

    private CodeExecutionResponse executeJavaScript(CodeExecutionRequest request) throws Exception {
        CodeExecutionResponse response = new CodeExecutionResponse();
        String fileName = "solution.js";
        Path filePath = Paths.get(TEMP_DIR, fileName);

        Files.write(filePath, request.getSourceCode().getBytes());

        try {
            ProcessBuilder builder = new ProcessBuilder("node", filePath.toString());
            Process process = builder.start();
            boolean executed = process.waitFor(EXECUTION_TIMEOUT, TimeUnit.SECONDS);

            if (!executed) {
                process.destroy();
                response.setStatus(CodeSubmission.ExecutionStatus.TIMEOUT);
                response.setErrorMessage("Execution timeout");
                return response;
            }

            String output = new String(process.getInputStream().readAllBytes());
            String error = new String(process.getErrorStream().readAllBytes());

            if (process.exitValue() == 0) {
                response.setStatus(CodeSubmission.ExecutionStatus.SUCCESS);
                response.setOutput(output);
            } else {
                response.setStatus(CodeSubmission.ExecutionStatus.RUNTIME_ERROR);
                response.setErrorMessage(error);
            }
        } finally {
            Files.deleteIfExists(filePath);
        }

        return response;
    }
}
