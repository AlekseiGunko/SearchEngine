package searchengine.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class IndexServiceImpl implements IndexService {

    private final Logger logger = LoggerFactory.getLogger(IndexServiceImpl.class);
    private AtomicBoolean indexing;
    private final PageRepository pageRepository;
    private final SiteRepository siteRepository;

    @Override
    public void startIndex(AtomicBoolean indexing) {
        this.indexing = indexing;
        try {
            deletedSiteAndPage();

        } catch (Exception e) {
            logger.error("Error: " , e);
        }

    }

    public void deletedSiteAndPage () {
        siteRepository.deleteAll();
        pageRepository.deleteAll();
    }

}
