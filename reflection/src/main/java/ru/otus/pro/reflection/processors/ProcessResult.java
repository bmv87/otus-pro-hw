package ru.otus.pro.reflection.processors;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ProcessResult {
    private long totalCount;
    private long successCount;
    private long failedCount;
    private long disabledCount;
}
