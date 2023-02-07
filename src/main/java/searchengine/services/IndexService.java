package searchengine.services;

import java.util.concurrent.atomic.AtomicBoolean;


public interface IndexService {

    void startIndex(AtomicBoolean indexing);
}
