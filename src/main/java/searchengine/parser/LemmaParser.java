package searchengine.parser;

import searchengine.dto.statistics.LemmaDto;
import searchengine.model.SiteEntity;

import java.util.List;

public interface LemmaParser {
    void run(SiteEntity site);
    List<LemmaDto> getLemmaDtoList();
}
