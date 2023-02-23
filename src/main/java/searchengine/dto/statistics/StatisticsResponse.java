package searchengine.dto.statistics;

import lombok.Data;
import lombok.Value;

@Value
public class StatisticsResponse {
    private boolean result;
    private StatisticsData statistics;
}
