package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import searchengine.model.LemmaEntity;
import searchengine.model.SiteEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface LemmaRepository extends JpaRepository<LemmaEntity, Integer> {
  long countBySiteEntityId(SiteEntity site);

  List<LemmaEntity> findBySiteEntityId(SiteEntity siteId);
  @Query(value = "SELECT l.* FROM Lemma l WHERE l.lemma IN :lemmas AND l.site_id = :site", nativeQuery = true)
  List<LemmaEntity> findLemmaListBySite(@Param("lemmas") List<String> lemmaList,
                                  @Param("site")SiteEntity site);

  @Query(value = "SELECT l.* FROM Lemma l WHERE l.lemma = :lemma ORDER BY frequency ASC", nativeQuery = true)
  List<LemmaEntity> findByLemma(@Param("lemma") String lemma);

}
