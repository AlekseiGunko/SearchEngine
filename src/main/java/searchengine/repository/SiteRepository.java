package searchengine.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.Sites;


@Repository
public interface SiteRepository extends CrudRepository <Sites, Integer> {

}
