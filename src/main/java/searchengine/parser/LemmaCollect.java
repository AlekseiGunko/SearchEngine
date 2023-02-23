package searchengine.parser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import searchengine.dto.statistics.LemmaDto;
import searchengine.model.PageEntity;
import searchengine.model.SiteEntity;
import searchengine.morph.Morphology;
import searchengine.repository.PageRepository;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class LemmaCollect implements LemmaParser {
    private final PageRepository pageRepository;
    private final Morphology morphology;
    private List<LemmaDto> lemmaDtoList;

    @Override
    public void run(SiteEntity site) {
        lemmaDtoList = new CopyOnWriteArrayList<>();
        Iterable<PageEntity> pageList = pageRepository.findAll();
        TreeMap<String, Integer> lemmaList = new TreeMap<>();
        for (PageEntity page : pageList) {
            String content = page.getContent();
            String title = ClearHtml.clearCode(content, "title");
            String body = ClearHtml.clearCode(content, "body");
            HashMap<String, Integer> titleList = morphology.getLemmaList(title);
            HashMap<String, Integer> bodyList = morphology.getLemmaList(body);
            Set<String> allWords = new HashSet<>();
            allWords.addAll(titleList.keySet());
            allWords.addAll(bodyList.keySet());
            for (String word : allWords) {
                int frequency = lemmaList.getOrDefault(word, 0) + 1;
                lemmaList.put(word, frequency);
            }
        }
        for (String lemma : lemmaList.keySet()) {
            Integer frequency = lemmaList.get(lemma);
            lemmaDtoList.add(new LemmaDto(lemma, frequency));
        }
    }

    @Override
    public List<LemmaDto> getLemmaDtoList() {
        return lemmaDtoList;
    }
}
