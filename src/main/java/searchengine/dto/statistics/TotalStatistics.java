package searchengine.dto.statistics;

import lombok.Value;

@Value
public class TotalStatistics {
     Long sites;
     Long pages;
     Long lemmas;
     boolean indexing;
}
