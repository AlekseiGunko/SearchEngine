package searchengine.dto.statistics;
import lombok.Value;
import searchengine.model.Status;

import java.util.Date;

@Value
public class DetailedStatisticsItem {
    String url;
    String name;
    Status status;
    Date statusTime;
    String error;
    long pages;
    long lemmas;
}
