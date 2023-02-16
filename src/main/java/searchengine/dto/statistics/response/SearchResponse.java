package searchengine.dto.statistics.response;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import searchengine.dto.statistics.SearchData;

import java.util.List;

@Getter
@Setter
@Data
public class SearchResponse {
    private boolean result;
    private int count;
    private List<SearchData> searchData;

    public SearchResponse(boolean result) {
        this.result = result;
    }

    public SearchResponse(boolean result, int count, List<SearchData> searchData) {
        this.result = result;
        this.count = count;
        this.searchData = searchData;
    }
}


