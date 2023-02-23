package searchengine.services;

public interface IndexingService {
    boolean urlIndexing(String url);
    boolean indexingAll();
    boolean stopIndexing();
}
