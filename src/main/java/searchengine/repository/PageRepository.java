package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import searchengine.model.Lemma;
import searchengine.model.Page;
import searchengine.model.Sites;


import java.util.Collection;
import java.util.List;

@Repository
public interface PageRepository extends JpaRepository<Page, Integer> {

    int countBySiteId(Sites siteId);
    Iterable<Page> findBySiteId(Sites site);

    @Query(value = "SELECT p.* FROM Page p JOIN Words_index i ON p.id = i.page_id WHERE i.lemma_id IN :lemmas",
            nativeQuery = true)
    List<Page> findLemmaList (@Param("lemmas")Collection<Lemma> lemmaListId);
}
