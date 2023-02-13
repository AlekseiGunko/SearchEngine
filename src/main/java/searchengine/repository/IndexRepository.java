package searchengine.repository;

import org.springframework.data.repository.CrudRepository;
import searchengine.model.IndexEntity;

public interface IndexRepository  extends CrudRepository<IndexEntity, Integer> {
}
