package searchengine.dto.statistics;

import lombok.Value;

@Value
public class LemmaDto {
    String lemma;
    int frequency;
}
