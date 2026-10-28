package nexusmarket.application.infrastructure.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.services.reports.ReportsService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReportController {

    private final ReportsService reportsService;

    @GetMapping("/reports/inventory")
    public Map<String, Long> inventorySummary() {
        return reportsService.inventorySummary();
    }

    @GetMapping("/reports/orders")
    public Map<String, Long> orderSummary() {
        return reportsService.orderSummary();
    }

    @GetMapping("/reports/audit")
    public List<String> recentAuditEvents(@RequestParam(defaultValue = "10") int limit) {
        return reportsService.recentAuditEvents(limit);
    }
}
