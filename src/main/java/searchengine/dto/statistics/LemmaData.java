package searchengine.dto.statistics;


import lombok.Data;
import lombok.Value;

@Value
@Data
public class LemmaData {

    String lemma;
    int frequency;
}
