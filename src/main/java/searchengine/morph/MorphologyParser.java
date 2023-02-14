package searchengine.morph;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;



@Slf4j
@Component
public class MorphologyParser implements Morphology{

    private static RussianLuceneMorphology russianMorphology;
    private final static String regex = "\\p{Punct}|[0-9]|@|©|◄|»|«|—|-|№|…";
    private final static Logger logger = LogManager.getLogger(LuceneMorphology.class);
    private static final Marker INVALID_SYMBOL_MARKER = MarkerManager.getMarker("INVALID_SYMBOL");

    static {
        try {
            russianMorphology = new RussianLuceneMorphology();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public HashMap<String, Integer> lemmasList(String content) {

        content = content.toLowerCase(Locale.ROOT).replaceAll(regex, " ");
        HashMap<String, Integer> lemmaList = new HashMap<>();
        String [] elements = content.toLowerCase(Locale.ROOT).split("\\s+");
        for (String e : elements) {
            List<String> wordsList = lemmas(e);
            for (String word : wordsList) {
                int count = lemmaList.getOrDefault(word, 0);
                lemmaList.put(word, count + 1);
            }
        }

        return lemmaList;
    }

    @Override
    public List<String> lemmas(String word) {
        List<String> lemmasList = new ArrayList<>();
        try {
            List<String> initialForm = russianMorphology.getNormalForms(word);
            if (!serviceWord(word)) {
                lemmasList.addAll(initialForm);
            }

        } catch (Exception e) {
            logger.debug(INVALID_SYMBOL_MARKER, "Слово не найдено - " + word);
        }
        return lemmasList;
    }

    @Override
    public List<Integer> indexLemmaInText(String content, String lemma) {

        List<Integer> indexLemmaList = new ArrayList<>();
        String [] elements = content.toLowerCase(Locale.ROOT).split("\\p{Punct}|\\s");
        int index = 0;
        for (String e : elements) {
            List<String> lemmas = lemmas(e);
            for (String lem : lemmas) {
                if (lem.equals(lemma)) {
                    indexLemmaList.add(index);
                }
            }
            index = e.length() + 1;
        }

        return indexLemmaList;
    }

    private boolean serviceWord (String word) {

        List<String> morphForm = russianMorphology.getMorphInfo(word);
        for (String lem : morphForm) {
            if (lem.contains("ПРЕДЛ")
                    || lem.contains("СОЮЗ")
                    || lem.contains("МЕЖД")
                    || lem.contains("МС")
                    || lem.contains("ЧАСТ")
                    || lem.length() <= 3) {
                return true;
            }
        }

     return false;
    }
}
