package searchengine.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.dto.statistics.response.FalseResponse;
import searchengine.dto.statistics.response.TrueResponse;
import searchengine.repository.SiteRepository;
import searchengine.services.IndexingService;
import searchengine.services.StatisticsService;


@RestController
@RequestMapping("/api")
@Slf4j
public class ApiController {

    private final StatisticsService statisticsService;
    private final IndexingService indexingService;
    private final SiteRepository siteRepository;


    public ApiController(StatisticsService statisticsService, IndexingService indexingService,
                         SiteRepository siteRepository) {
        this.statisticsService = statisticsService;
        this.indexingService = indexingService;
        this.siteRepository = siteRepository;

    }


    @GetMapping("/startIndexing")
    public ResponseEntity<Object> startIndexing() {
        if (indexingService.indexingAll()) {
            return new ResponseEntity<>(new TrueResponse(true), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new FalseResponse(false,
                    "Индексация уже запущена"), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/stopIndexing")
    public ResponseEntity<Object> stopIndexing() {
        if (indexingService.stopIndexing()) {
            return new ResponseEntity<>(new TrueResponse(true), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new FalseResponse(false,
                    "Индексация не запущена"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("indexPage")
    public ResponseEntity<Object> indexPage(@RequestParam(name = "url") String url) {

        if (url.isEmpty()) {
            log.info("Страница не указана");
        } else if (indexingService.urlIndexing(url)) {
            return new ResponseEntity<>(new TrueResponse(true), HttpStatus.OK);

        }
            log.info("Данная страница находится за пределами сайтов, указанных в конфигурационном файле");
            return new ResponseEntity<>(new FalseResponse(false,
                    "Данная страница находится за пределами сайтов, указанных в конфигурационном файле"),
                    HttpStatus.BAD_REQUEST);


    }


    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }
}
