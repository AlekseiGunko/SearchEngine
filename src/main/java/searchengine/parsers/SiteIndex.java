package searchengine.parsers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.dto.statistics.IndexData;
import searchengine.dto.statistics.LemmaData;
import searchengine.dto.statistics.PageData;
import searchengine.model.*;
import searchengine.repository.IndexRepository;
import searchengine.repository.LemmaRepository;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ForkJoinPool;

@RequiredArgsConstructor
@Slf4j
public class SiteIndex implements  Runnable{

    private static final int CORE_COUNT = Runtime.getRuntime().availableProcessors();
    private final PageRepository pageRepository;
    private final SiteRepository siteRepository;
    private final LemmaRepository lemmaRepository;
    private final IndexRepository indexRepository;
    private final LemmaParser lemmaParser;
    private final IndexParser indexParser;
    private final String url;
    private final SitesList sitesList;

    @Override
    public void run() {

    if (siteRepository.findByUrl(url) != null) {
        deletedDataFromSite();
    }
    saveDataSite();
    try {

        List<PageData> pageDataList = getPageDataList();
        saveToBasePages(pageDataList);
        getLemmasPage();
        indexWords();

    }catch (InterruptedException e) {
        log.error("Ошибка индексации " + url);
        errorSiteIndex();

    }


    }

    private List<PageData> getPageDataList () throws InterruptedException {

        if (!Thread.interrupted()) {
            List<PageData> pageDataVector = new Vector<>();
            List<String> urlList = new Vector<>();
            ForkJoinPool forkJoinPool = new ForkJoinPool(CORE_COUNT);
            List<PageData> pages = forkJoinPool.invoke(new PageUrlParser(url, urlList, pageDataVector));
            return new CopyOnWriteArrayList<>(pages);

        }else throw new InterruptedException();
    }

    private void saveToBasePages (List<PageData> pages) throws InterruptedException {

        if (!Thread.interrupted()) {
            List<Page> pageList = new CopyOnWriteArrayList<>();
            Sites site = siteRepository.findByUrl(url);

            for (PageData page : pages) {
                int start = page.getUrl().indexOf(url) + url.length();
                String pageFormat = page.getUrl().substring(start);
                pageList.add(new Page(site, pageFormat, page.getCode(), page.getContent()));
            }
            pageRepository.flush();
            pageRepository.saveAll(pageList);
        } else {
            throw new InterruptedException();
        }

    }

    private void getLemmasPage () {

        if (!Thread.interrupted()) {
            Sites site = siteRepository.findByUrl(url);
            site.setStatusTime(LocalDateTime.now());
            lemmaParser.run(site);
            List<LemmaData> lemmaDataList = lemmaParser.getLemmaDataList();
            List<Lemma> lemmaList = new CopyOnWriteArrayList<>();

            for (LemmaData lemmaData : lemmaDataList) {
                lemmaList.add(new Lemma(site, lemmaData.getLemma(), lemmaData.getFrequency()));
            }
            lemmaRepository.flush();
            lemmaRepository.saveAll(lemmaList);
        } else {
            throw new RuntimeException();
        }

    }


    private void indexWords () throws InterruptedException {

        if (!Thread.interrupted()) {
            Sites site = siteRepository.findByUrl(url);
            indexParser.run(site);
            List<IndexData> indexDataList = new CopyOnWriteArrayList<>(indexParser.getIndexList());
            List<ListIndex> indexList = new CopyOnWriteArrayList<>();
            site.setStatusTime(LocalDateTime.now());
            for (IndexData indexData : indexDataList) {
                Page page = pageRepository.getReferenceById(indexData.getPageId());
                Lemma lemma = lemmaRepository.getReferenceById(indexData.getLemmaId());
                indexList.add(new ListIndex(page, lemma, indexData.getRank()));

            }
            indexRepository.flush();
            indexRepository.saveAll(indexList);
            site.setStatusTime(LocalDateTime.now());
            site.setStatus(StatusType.INDEXED);
            siteRepository.save(site);

        }else {
            throw new InterruptedException();
        }

    }


    private void deletedDataFromSite () {
        Sites site = siteRepository.findByUrl(url);
        site.setStatus(StatusType.INDEXING);
        site.setName(getName());
        site.setStatusTime(LocalDateTime.now());
        siteRepository.save(site);
        siteRepository.flush();
        siteRepository.delete(site);
    }

    private void saveDataSite () {
        Sites site = new Sites();
        site.setUrl(url);
        site.setName(getName());
        site.setStatus(StatusType.INDEXING);
        site.setStatusTime(LocalDateTime.now());
        siteRepository.flush();
        siteRepository.save(site);
    }

    private void errorSiteIndex () {
        Sites site = new Sites();
        site.setLastError("Ошибка индексации сайта");
        site.setStatus(StatusType.FAILED);
        site.setStatusTime(LocalDateTime.now());
        siteRepository.save(site);
    }

    private String getName () {
        List<Site> sites = sitesList.getSites();
        for (Site site : sites) {
            if (site.getUrl().equals(url)) {
                return site.getName();
            }
        }
        return "";
    }


}
