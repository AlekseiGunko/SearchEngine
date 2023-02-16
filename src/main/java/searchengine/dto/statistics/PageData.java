package searchengine.dto.statistics;

import lombok.Data;
import lombok.Value;

@Value
@Data
public class PageData {
    String url;
    String content;
    int code;
}
