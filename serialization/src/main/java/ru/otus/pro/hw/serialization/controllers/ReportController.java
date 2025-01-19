package ru.otus.pro.hw.serialization.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.pro.hw.serialization.models.out.ChatSessionVM;
import ru.otus.pro.hw.serialization.services.ReportService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reports")
public class ReportController {
    private final ReportService reportService;

    @GetMapping
    public List<ChatSessionVM> getAllAccountsByClientId() {
        return reportService.getReport();
    }
}
