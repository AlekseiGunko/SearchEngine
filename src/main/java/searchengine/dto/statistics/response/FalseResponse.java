package searchengine.dto.statistics.response;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Value
public class FalseResponse  {
    boolean result;
    String error;

}
