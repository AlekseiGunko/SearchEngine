package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import searchengine.model.Lemma;
import searchengine.model.ListIndex;
import searchengine.model.Page;

import java.util.List;


@Repository
public interface IndexRepository  extends JpaRepository<ListIndex, Integer> {

    @Query(value = "SELECT i.* FROM Words_index i WHERE i.lemma_id IN :lemmas AND i.page_id IN :pages",
            nativeQuery = true)
    List<ListIndex> findPagesAndLemmas(@Param("lemmas") List<Lemma> lemmaListId,
                                       @Param("pages") List<Page> pageListId);

    List<ListIndex> findByLemmaId (int lemmaId);
    List<ListIndex> findByPageId (int pageId);
    ListIndex findByLemmaIdAndPageId (int lemmaId, int pageId);
}
