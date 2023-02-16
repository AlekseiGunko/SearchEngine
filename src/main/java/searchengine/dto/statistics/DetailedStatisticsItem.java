package searchengine.dto.statistics;

import lombok.Data;
import lombok.Value;
import searchengine.model.StatusType;

import java.time.LocalDateTime;

@Value
@Data
public class DetailedStatisticsItem {
    String url;
    String name;
    StatusType status;
    LocalDateTime statusTime;
    String error;
    int pages;
    int lemmas;
}
