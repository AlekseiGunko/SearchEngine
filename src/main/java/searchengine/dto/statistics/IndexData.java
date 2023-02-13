package searchengine.dto.statistics;

import lombok.Value;

@Value
public class IndexData {
    Integer pageId;
    Integer lemmaId;
    Float rank;
}
