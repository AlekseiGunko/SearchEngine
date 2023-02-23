package searchengine.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.model.SiteEntity;
import searchengine.model.Status;
import searchengine.parser.IndexParser;
import searchengine.parser.LemmaParser;
import searchengine.parser.SiteIndexed;
import searchengine.repository.IndexRepository;
import searchengine.repository.LemmaRepository;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;
import searchengine.services.IndexingService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class IndexingServiceImpl implements IndexingService {
    private static final int CORE_COUNT = Runtime.getRuntime().availableProcessors();
    private ExecutorService executorService;
    private final PageRepository pageRepository;
    private final SiteRepository siteRepository;
    private final LemmaRepository lemmaRepository;
    private final IndexRepository indexRepository;
    private final LemmaParser lemmaParser;
    private final IndexParser indexParser;
    private final SitesList sitesList;

    @Override
    public boolean urlIndexing(String url) {
        if (urlCheck(url)) {
            log.info("Начата переиндексация сайта - " + url);
            executorService = Executors.newFixedThreadPool(CORE_COUNT);
            executorService.submit(new SiteIndexed(pageRepository, siteRepository,
                    lemmaRepository, indexRepository, lemmaParser, indexParser, url, sitesList));
            executorService.shutdown();

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean indexingAll() {
        if (isIndexingActive()) {
            log.debug("Индексация уже запущена");
            return false;
        } else {
            List<Site> siteList = sitesList.getSites();
            executorService = Executors.newFixedThreadPool(CORE_COUNT);
            for (Site site : siteList) {
                String url = site.getUrl();
                SiteEntity siteEntity = new SiteEntity();
                siteEntity.setName(site.getName());
                log.info("Парсинг сайта: " + site.getName());
                executorService.submit(new SiteIndexed(pageRepository, siteRepository,
                        lemmaRepository, indexRepository, lemmaParser, indexParser, url, sitesList));
            }
            executorService.shutdown();
        }
        return true;
    }

    @Override
    public boolean stopIndexing() {
        if (isIndexingActive()) {
            log.info("Останавливаем индексацию");
            executorService.shutdownNow();
            return true;
        } else {
            log.info("Индексация не может быть остановлена т.к. не была запущена");
            return false;
        }
    }

    private boolean isIndexingActive() {
        siteRepository.flush();
        Iterable<SiteEntity> siteList = siteRepository.findAll();
        for (SiteEntity site : siteList) {
            if (site.getStatus() == Status.INDEXING) {
                return true;
            }
        }
        return false;
    }

    private boolean urlCheck(String url) {
        List<Site> urlList = sitesList.getSites();
        for (Site site : urlList) {
            if (site.getUrl().equals(url)) {
                return true;
            }
        }
        return false;
    }
}
