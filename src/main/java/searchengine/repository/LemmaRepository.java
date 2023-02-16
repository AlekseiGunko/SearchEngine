package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import searchengine.model.Lemma;
import searchengine.model.Sites;

import java.util.List;

@Repository
public interface LemmaRepository  extends JpaRepository<Lemma, Integer> {

    int countBySitesId(Sites site);

    List<Lemma> findBySitesId(Sites sitesId);

    @Query(value = "SELECT l.* FROM Lemma l WHERE l.lemma IN :lemmas AND l.site_id = :site", nativeQuery = true)
    List<Lemma> findLemmaBySite(@Param("lemmas") List<String> lemmaList, @Param("site") Sites site);

    @Query(value = "SELECT l.* FROM Lemma l WHERE l.lemma = :lemma ORDER BY frequency ASC", nativeQuery = true)
    List<Lemma> findByLemma(@Param("lemma") String lemma);
}
