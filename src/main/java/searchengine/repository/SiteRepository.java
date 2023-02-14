package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.Sites;


@Repository
public interface SiteRepository extends JpaRepository<Sites, Integer> {

    Sites findByUrl(String url);
    Sites findByUrl(int id);
    Sites findByUrl(Sites site);

}
