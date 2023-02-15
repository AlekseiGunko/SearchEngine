package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.ListIndex;


@Repository
public interface IndexRepository  extends JpaRepository<ListIndex, Integer> {
}
