package searchengine.services.implService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchengine.dto.statistics.DetailedStatisticsItem;
import searchengine.dto.statistics.StatisticsData;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.dto.statistics.TotalStatistics;
import searchengine.model.Sites;
import searchengine.model.StatusType;
import searchengine.repository.LemmaRepository;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;
import searchengine.services.StatisticsService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final PageRepository pageRepository;
    private final LemmaRepository lemmaRepository;
    private final SiteRepository siteRepository;


    private TotalStatistics getTotalInfo () {
        Long sites = siteRepository.count();
        Long pages = pageRepository.count();
        Long lemma = lemmaRepository.count();
        return new TotalStatistics(sites, pages, lemma, true);
    }

    private DetailedStatisticsItem getDetailed (Sites site) {
        String url = site.getUrl();
        String name = site.getName();
        StatusType statusType = site.getStatus();
        LocalDateTime statusTime = site.getStatusTime();
        String error = site.getLastError();
        int pages = pageRepository.countBySiteId(site);
        int lemmas = lemmaRepository.countBySitesId(site);
        return new DetailedStatisticsItem(url, name, statusType, statusTime, error, pages, lemmas);
    }

    private List<DetailedStatisticsItem> getListDet () {
        List<Sites> sitesList = siteRepository.findAll();
        List<DetailedStatisticsItem> itemList = new ArrayList<>();
        for (Sites site : sitesList) {
            DetailedStatisticsItem item = getDetailed(site);
            itemList.add(item);
        }
        return itemList;
    }


    @Override
    public StatisticsResponse getStatistics() {

        TotalStatistics totalStatistics = getTotalInfo();
        List<DetailedStatisticsItem> itemList = getListDet();

        return new StatisticsResponse(true, new StatisticsData(totalStatistics, itemList));
    }
}
