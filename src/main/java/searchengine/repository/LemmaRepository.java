package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.Lemma;
import searchengine.model.Sites;

import java.util.List;

@Repository
public interface LemmaRepository  extends JpaRepository<Lemma, Integer> {

    int countBySitesId(Sites site);

    List<Lemma> findBySitesId(Sites sitesId);
}
