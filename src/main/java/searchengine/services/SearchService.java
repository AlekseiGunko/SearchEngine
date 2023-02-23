package searchengine.services;

import searchengine.dto.statistics.SearchDto;

import java.util.List;

public interface SearchService {
    List<SearchDto> allSiteSearch(String text, int offset, int limit);
    List<SearchDto> siteSearch(String searchText, String url, int offset, int limit);
}
