package searchengine.parsers;

import searchengine.dto.statistics.LemmaData;
import searchengine.model.Sites;

import java.util.List;

public interface LemmaParser {

    void run (Sites site);
    List<LemmaData> getLemmaDataList();

}

