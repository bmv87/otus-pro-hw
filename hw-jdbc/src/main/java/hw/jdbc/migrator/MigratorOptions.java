package hw.jdbc.migrator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MigratorOptions {
    private final String historyTableName;
    private final String shema;
    private final String migrationLogDirectory;
    private final String migrationLogFile;
}
