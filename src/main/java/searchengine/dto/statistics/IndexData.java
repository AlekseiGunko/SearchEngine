package searchengine.dto.statistics;

import lombok.Data;
import lombok.Value;

@Value
@Data
public class IndexData {
    Integer pageId;
    Integer lemmaId;
    Float rank;
}
