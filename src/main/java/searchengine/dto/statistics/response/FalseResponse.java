package searchengine.dto.statistics.response;

import lombok.Data;
import lombok.Value;

@Value
@Data
public class FalseResponse {
    boolean result;
    String error;
}
