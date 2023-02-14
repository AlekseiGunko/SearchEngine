package searchengine.parsers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import searchengine.dto.statistics.IndexData;
import searchengine.model.Lemma;
import searchengine.model.Page;
import searchengine.model.Sites;
import searchengine.morph.Morphology;
import searchengine.repository.LemmaRepository;
import searchengine.repository.PageRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Component
public class Indexing implements IndexParser{

    private final PageRepository pageRepository;
    private final LemmaRepository lemmaRepository;
    private final Morphology morphology;
    private List<IndexData> indexDataList;


    @Override
    public void run(Sites site) {
        Iterable<Page> pageList = pageRepository.findBySiteId(site);
        List<Lemma> lemmaList = lemmaRepository.findBySitesId(site);
        indexDataList = new ArrayList<>();

        for (Page page : pageList) {
            if (page.getCode() > 400) {
                int pageId = page.getId();
                String content = page.getContent();
                String title = ClearHTML.clearCode(content, "title");
                String body = ClearHTML.clearCode(content, "body");
                HashMap<String, Integer> titleList = morphology.lemmasList(title);
                HashMap<String, Integer> bodyList = morphology.lemmasList(body);

                for (Lemma lemma : lemmaList) {
                    int lemmaId = lemma.getId();
                    String keyWord = lemma.getLemma();
                    if (titleList.containsKey(keyWord) || bodyList.containsKey(keyWord)) {
                        float totalRank = 0.0f;
                        if (titleList.get(keyWord) != null) {
                            Float titleRank = Float.valueOf(titleList.get(keyWord));
                            totalRank += titleRank;
                        }
                        if (bodyList.get(keyWord) != null) {
                            float bodyRank = (float) (bodyList.get(keyWord) * 0.8);
                            totalRank += bodyRank;
                        }
                        indexDataList.add(new IndexData(pageId, lemmaId, totalRank));
                    } else {
                        log.debug("Лемма не найдена");
                    }
                }
            } else {
                log.debug("Некорректный запрос " + page.getCode());
            }
        }


    }

    @Override
    public List<IndexData> getIndexList() {
        return indexDataList;
    }
}
