package searchengine.dto.statistics;


import lombok.Value;

@Value
public class LemmaData {

    String lemma;
    int frequency;
}
