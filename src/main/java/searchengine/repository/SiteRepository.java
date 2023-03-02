package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.SiteEntity;
@Repository
public interface SiteRepository extends JpaRepository<SiteEntity, Integer> {
    SiteEntity findByUrl(String url);
    SiteEntity findByUrl(long id);
    SiteEntity findByUrl(SiteEntity site);

}
