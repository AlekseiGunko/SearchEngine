package searchengine.parser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import searchengine.dto.statistics.IndexDto;
import searchengine.model.LemmaEntity;
import searchengine.model.PageEntity;
import searchengine.model.SiteEntity;
import searchengine.morph.Morphology;
import searchengine.repository.LemmaRepository;
import searchengine.repository.PageRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class Indexing implements IndexParser {
    private final PageRepository pageRepository;
    private final LemmaRepository lemmaRepository;
    private final Morphology morphology;
    private List<IndexDto> indexDtoList;

    @Override
    public void run(SiteEntity site) {
        Iterable<PageEntity> pageList = pageRepository.findBySiteId(site);
        List<LemmaEntity> lemmaList = lemmaRepository.findBySiteEntityId(site);
        indexDtoList = new ArrayList<>();

        for (PageEntity page : pageList) {
            if (page.getCode() < 400) {
                int pageId = page.getId();
                String content = page.getContent();
                String title = ClearHtml.clearCode(content, "title");
                String body = ClearHtml.clearCode(content, "body");
                HashMap<String, Integer> titleList = morphology.getLemmaList(title);
                HashMap<String, Integer> bodyList = morphology.getLemmaList(body);

                for (LemmaEntity lemma : lemmaList) {
                    Integer lemmaId = lemma.getId();
                    String keyWord = lemma.getLemma();
                    if (titleList.containsKey(keyWord) || bodyList.containsKey(keyWord)) {
                        float totalRank = 0.0F;
                        if (titleList.get(keyWord) != null) {
                            Float titleRank = Float.valueOf(titleList.get(keyWord));
                            totalRank += titleRank;
                        }
                        if (bodyList.get(keyWord) != null) {
                            float bodyRank = (float) (bodyList.get(keyWord) * 0.8);
                            totalRank += bodyRank;
                        }
                        indexDtoList.add(new IndexDto(pageId, lemmaId, totalRank));
                    } else {
                        log.debug("Lemma not found");
                    }
                }
            } else {
                log.debug("Bad status code - " + page.getCode());
            }
        }
    }

    @Override
    public List<IndexDto> getIndexList() {
        return indexDtoList;
    }
}
