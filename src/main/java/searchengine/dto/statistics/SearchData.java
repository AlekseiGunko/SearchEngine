package searchengine.dto.statistics;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Data
public class SearchData {

    private String site;
    private String siteName;
    private String uri;
    private String title;
    private String snippet;
    private Float relevance;

    public SearchData(String site, String siteName, String uri, String title, String snippet, Float relevance) {
        this.site = site;
        this.siteName = siteName;
        this.uri = uri;
        this.title = title;
        this.snippet = snippet;
        this.relevance = relevance;
    }
}
