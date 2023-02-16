package searchengine.dto.statistics;

import lombok.Data;
import lombok.Value;

@Value
@Data
public class StatisticsResponse {
    private boolean result;
    private StatisticsData statistics;
}
