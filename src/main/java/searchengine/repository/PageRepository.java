package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.Page;
import searchengine.model.Sites;

@Repository
public interface PageRepository extends JpaRepository<Page, Integer> {

    int countBySiteId(Sites siteId);
    Iterable<Page> findBySiteId(Sites site);
}
