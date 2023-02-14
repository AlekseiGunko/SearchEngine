package searchengine.morph;

import java.util.HashMap;
import java.util.List;

public interface Morphology {

    HashMap<String, Integer> lemmasList(String content);
    List<String> lemmas (String word);
    List<Integer> indexLemmaInText(String content, String lemma);

}
