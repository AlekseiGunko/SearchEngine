package searchengine.parsers;

import searchengine.dto.statistics.IndexData;
import searchengine.model.Sites;

import java.util.List;

public interface IndexParser {
    void run (Sites site);
    List<IndexData> getIndexList();
}
