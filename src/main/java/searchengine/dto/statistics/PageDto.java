package searchengine.dto.statistics;

import lombok.Value;

@Value
public class PageDto {
    String url;
    String content;
    int code;
}
