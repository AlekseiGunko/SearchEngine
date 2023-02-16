package searchengine.dto.statistics;

import lombok.Data;
import lombok.Value;

@Value
@Data
public class TotalStatistics {
     Long sites;
     Long pages;
     Long lemmas;
     boolean indexing;
}
