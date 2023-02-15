package searchengine.services.implService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.model.Sites;
import searchengine.model.StatusType;
import searchengine.parsers.IndexParser;
import searchengine.parsers.LemmaParser;
import searchengine.parsers.SiteIndex;
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

        if (urlUp(url)) {
            log.info("Идет переиндексацция сайта " + url);
            executorService = Executors.newFixedThreadPool(CORE_COUNT);
            executorService.submit(new SiteIndex(pageRepository, siteRepository, lemmaRepository,
                    indexRepository, lemmaParser, indexParser, url, sitesList));
            executorService.shutdown();

            return true;
        } else {

            return false;
        }


    }

    @Override
    public boolean indexingAll() {

        if (indexActive()) {
            log.info("Индексация запущена");
            return false;
        } else {
            List<Site> siteList = sitesList.getSites();
            executorService = Executors.newFixedThreadPool(CORE_COUNT);
            for (Site site : siteList) {
                String url = site.getUrl();
                Sites sites = new Sites();
                sites.setName(site.getName());
                log.info("Начат парсинг с сайта " + url);
                executorService.submit(new SiteIndex(pageRepository, siteRepository, lemmaRepository,
                        indexRepository, lemmaParser, indexParser, url, sitesList));
            }
            executorService.shutdown();
        }

        return true;
    }

    @Override
    public boolean stopIndexing() {

        if (indexActive()) {
            log.info("Индексация остановлена");
            executorService.shutdown();
            return true;
        } else {
            log.info("Индексация ранее не была запущенна");
            return false;

        }


    }

    private boolean indexActive() {
        siteRepository.flush();
        Iterable<Sites> sitesList = siteRepository.findAll();
        for (Sites sites : sitesList) {
            if (sites.getStatus() == StatusType.INDEXING) {
                return true;
            }
        }
        return false;
    }

    private boolean urlUp (String url) {

        List<Site> urlList = sitesList.getSites();
        for (Site site : urlList) {
            if (site.getUrl().equals(url)) {
                return true;
            }
        }
        return false;
    }
}
