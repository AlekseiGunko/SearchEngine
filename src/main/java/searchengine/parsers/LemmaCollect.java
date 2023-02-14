package searchengine.parsers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import searchengine.dto.statistics.LemmaData;
import searchengine.model.Page;
import searchengine.model.Sites;
import searchengine.morph.Morphology;
import searchengine.repository.PageRepository;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


@Slf4j
@RequiredArgsConstructor
@Component
public class LemmaCollect implements LemmaParser{

    private final PageRepository pageRepository;
    private final Morphology morphology;
    private List<LemmaData> lemmaDataList;

    @Override
    public void run(Sites site) {

        lemmaDataList = new CopyOnWriteArrayList<>();
        Iterable<Page> pageList = pageRepository.findAll();
        TreeMap<String, Integer> lemmaList = new TreeMap<>();
        for (Page page : pageList) {
            String content = page.getContent();
            String title = ClearHTML.clearCode(content, "title");
            String body = ClearHTML.clearCode(content, "body");
            HashMap<String, Integer> titleList = morphology.lemmasList(title);
            HashMap<String, Integer> bodyList = morphology.lemmasList(body);
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
            lemmaDataList.add(new LemmaData(lemma, frequency));
        }

    }

    @Override
    public List<LemmaData> getLemmaDataList() {
        return lemmaDataList;
    }
}
