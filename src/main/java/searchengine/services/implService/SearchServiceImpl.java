package searchengine.services.implService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import searchengine.dto.statistics.SearchData;
import searchengine.model.Lemma;
import searchengine.model.ListIndex;
import searchengine.model.Page;
import searchengine.model.Sites;
import searchengine.morph.Morphology;
import searchengine.parsers.ClearHTML;
import searchengine.repository.IndexRepository;
import searchengine.repository.LemmaRepository;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;
import searchengine.services.SearchService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchServiceImpl implements SearchService {

    private final Morphology morphology;
    private final LemmaRepository lemmaRepository;
    private final PageRepository pageRepository;
    private final IndexRepository indexRepository;
    private final SiteRepository siteRepository;



    @Override
    public List<SearchData> allSiteSearch(String text, int offset, int limit) {
        log.info("Ищем: " + text);
        List<Sites> siteList = siteRepository.findAll();
        List<SearchData> searchDataList = new ArrayList<>();
        List<Lemma> lemmaList = new ArrayList<>();
        List<String> textLemma = getLemmaOnSiteSearch(text);
        for (Sites site : siteList) {
            lemmaList.addAll(getLemmaOnSite(textLemma, site));
        }

        List<SearchData> searchData = new ArrayList<>();
        for (Lemma lemma : lemmaList) {
            if (lemma.getLemma().equals(text)) {
                searchData = new ArrayList<>(searchDataList(lemmaList, textLemma, offset, limit));
                searchData.sort((o1, o2) -> Float.compare(o2.getRelevance(), o1.getRelevance()));
                if (searchData.size() > limit) {
                    for (int i = offset; i < limit; i++) {
                        searchDataList.add(searchData.get(i));
                    }
                    return searchDataList;
                }
            } else {
                try {
                    throw  new Exception();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        log.info("Ответ получен");

        List<SearchData> res = new ArrayList<>();
        for (SearchData ser : searchData) {
            res.add(ser);
            log.info(res.toString());
        }

        return searchData;
    }

    @Override
    public List<SearchData> siteSearch(String searchText, String url, int offset, int limit) {
        log.info("Ищем: " + searchText + " на сайте " + url);
        Sites site = siteRepository.findByUrl(url);
        List<String> textLemmas = getLemmaOnSiteSearch(searchText);
        List<Lemma> lemmaList = getLemmaOnSite(textLemmas, site);
        log.info("Ответ получен");
        log.info(searchDataList(lemmaList, textLemmas, offset, limit).toString());

        return searchDataList(lemmaList, textLemmas, offset, limit);
    }

    private List<Lemma> getLemmaOnSite(List<String> lemmas, Sites site) {
        lemmaRepository.flush();
        List<Lemma> lemmaList = lemmaRepository.findLemmaBySite(lemmas, site);
        List<Lemma> resultsLemma = new ArrayList<>(lemmaList);
        resultsLemma.sort(Comparator.comparingInt(Lemma::getFrequency));
        return resultsLemma;
    }

    private List<String> getLemmaOnSiteSearch (String searchInfo) {
        String [] words = searchInfo.toLowerCase(Locale.ROOT).split(" ");
        List<String> lemmaList = new ArrayList<>();
        for (String lemma : words) {
            List<String> list = morphology.lemmas(lemma);
            lemmaList.addAll(list);
        }
        return lemmaList;
    }

    private List<SearchData> getSearchData (Hashtable<Page, Float> pageList, List<String> lemmaList) {

        List<SearchData> result = new ArrayList<>();

        for (Page page : pageList.keySet()) {
            String uri = page.getPath();
            String content = page.getContent();
            Sites pageSite = page.getSiteId();
            String site = pageSite.getUrl();
            String siteName = pageSite.getName();
            Float relevance = pageList.get(page);

            StringBuilder allContent = new StringBuilder();
            String title = ClearHTML.clearCode(content, "title");
            String body = ClearHTML.clearCode(content, "body");
            allContent.append(title).append(" ").append(body);
            String snippet = getSnippet(allContent.toString(), lemmaList);
            result.add(new SearchData(site, siteName, uri, title, snippet, relevance));
        }
        return result;
    }

    private String getSnippet(String content, List<String> lemmaList) {

        List<Integer> lemmaIndex = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for (String lemma : lemmaList) {
            lemmaIndex.addAll(morphology.indexLemmaInText(content, lemma));
        }
        Collections.sort(lemmaIndex);
        List<String> wordsList = getWordsOnContent(content, lemmaIndex);
        for (int i = 0; i <wordsList.size(); i++) {
            builder.append(wordsList.get(i)).append("... ");
            if (i > 3) {
                break;
            }
        }
        return builder.toString();

    }

    private List<String> getWordsOnContent (String content, List<Integer> lemmaIndex) {
    List<String> result = new ArrayList<>();
    for (int i = 0; i < lemmaIndex.size(); i++) {
        int start = lemmaIndex.get(i);
        int end = content.indexOf(" ", start);
        int nextGet = i + 1;
        while (nextGet < lemmaIndex.size() && lemmaIndex.get(nextGet) - end > 0
        && lemmaIndex.get(nextGet) - end < 5) {
            end = content.indexOf(" ", lemmaIndex.get(nextGet));
            nextGet += 1;
        }
        i = nextGet - 1;
        String text = getWordsOnIndex(start, end, content);
        result.add(text);
    }
    result.sort(Comparator.comparingInt(String::length).reversed());
    return result;
    }

    private String getWordsOnIndex (int start, int end, String content) {
        String word = content.substring(start, end);
        int point;
        int lastPoint;
        if (content.lastIndexOf(" ", start) != -1) {
            point = content.lastIndexOf(" ", start);
        } else point = start;
        if (content.indexOf(" ", end + 30) != -1) {
            lastPoint = content.indexOf(" ", end + 30);
        } else lastPoint = content.indexOf(" ", end);
        String text = content.substring(point, lastPoint);
        try {
           text = text.replaceAll(word, "<b>" + word + "</b>");
        }catch (Exception e) {
            log.error("Что то пошло не так " + e.getMessage());
        }
        return text;
    }

    private List<SearchData> searchDataList (List<Lemma> lemmaList, List<String> textLemmaList, int offset, int limit) {
        List<SearchData> resultList = new ArrayList<>();
        pageRepository.flush();
        if (lemmaList.size() >= textLemmaList.size()) {
            List<Page> pageList = pageRepository.findLemmaList(lemmaList);
            indexRepository.flush();
            List<ListIndex> indexList = indexRepository.findPagesAndLemmas(lemmaList, pageList);
            Hashtable<Page, Float> sortedPageByRelevance = getPageRelevance(pageList, indexList);
            List<SearchData> dataList = getSearchData(sortedPageByRelevance, textLemmaList);

            if (offset > dataList.size()) {
                return new ArrayList<>();
            }
            if (dataList.size() > limit) {
                for (int i = offset; i < limit; i++) {
                    resultList.add(dataList.get(i));
                }
                return resultList;
            } else return dataList;
        } else return resultList;
    }

    private Hashtable<Page, Float> getPageRelevance (List<Page> pageList, List<ListIndex> indexList) {
        HashMap<Page, Float> relevancePage = new HashMap<>();
        for (Page page : pageList) {
            float relev = 0;
            for (ListIndex index : indexList) {
                if (index.getPageId() == page) {
                    relev += index.getRank();
                }
            }
            relevancePage.put(page, relev);
        }
        HashMap<Page, Float> absRelevance = new HashMap<>();
        for (Page page : relevancePage.keySet()) {
            float absRelev = relevancePage.get(page) / Collections.max(relevancePage.values());
            absRelevance.put(page, absRelev);
        }
        return absRelevance.entrySet().stream().sorted(Map.Entry.comparingByValue(
                Comparator.reverseOrder())).collect(Collectors.toMap(Map.Entry::getKey,
                Map.Entry::getValue, (e1, e2) -> e1, Hashtable::new));
    }
}
