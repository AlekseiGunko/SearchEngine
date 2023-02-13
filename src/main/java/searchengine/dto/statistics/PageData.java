package searchengine.dto.statistics;

import lombok.Value;

@Value
public class PageData {
    String url;
    String content;
    int code;
}
