package searchengine.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.services.StatisticsService;



@RestController
@RequestMapping("/api")
public class ApiController {

    private final StatisticsService statisticsService;


    public ApiController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;

    }


    @GetMapping("/startIndexing")
    public ResponseEntity startIndexing () {
            return ResponseEntity.status(HttpStatus.OK).body("'result' : true");

    }

    @GetMapping("/stopIndexing")
    public ResponseEntity stopIndexing () {
        return ResponseEntity.status(HttpStatus.OK).body("'result' : true ");
    }


    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }
}
