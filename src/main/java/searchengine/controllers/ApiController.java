package searchengine.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.services.IndexService;
import searchengine.services.StatisticsService;

import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final StatisticsService statisticsService;
    private final IndexService indexService;
    private final AtomicBoolean indexing = new AtomicBoolean();


    public ApiController(StatisticsService statisticsService, IndexService indexService) {
        this.statisticsService = statisticsService;
        this.indexService = indexService;
        indexing.set(false);
    }


    @GetMapping("/startIndexing")
    public ResponseEntity startIndexing () {
        if (indexing.get()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("'result' : false" +
                    "'error' : Индексация уже запущена");
        } else {
            indexing.set(true);
            Runnable startIndexing = () -> indexService.startIndex(indexing);
            new Thread(startIndexing).start();
            return ResponseEntity.status(HttpStatus.OK).body("'result' : true");
        }
    }

    @GetMapping("/stopIndexing")
    public ResponseEntity stopIndexing () {
    if (!indexing.get()) {
       return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("'result' : false, " +
               "'error' : Индексация не запущена");
    } else {
        indexing.set(false);
        return ResponseEntity.status(HttpStatus.OK).body("'result' : true ");
    }
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }
}
