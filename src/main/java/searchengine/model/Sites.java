package searchengine.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Getter
@Setter
@Entity
@Table(name = "site")
public class Sites {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM ('INDEXING', 'INDEXED', 'FAILED')")
    private StatusType status;

    @Column(name = "status_time")
    private LocalDateTime statusTime;

    @Column(name = "last_error", columnDefinition = "TEXT")
    private String lastError;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String url;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)", unique = true)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "siteId", cascade = CascadeType.ALL)
    protected List<Page> pageList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sitesId", cascade = CascadeType.ALL)
    protected List<Lemma> lemmaList = new ArrayList<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sites sites = (Sites) o;
        return id == sites.id && status == sites.status && Objects.equals(statusTime, sites.statusTime)
                && Objects.equals(lastError, sites.lastError) && Objects.equals(url, sites.url)
                && Objects.equals(name, sites.name) && Objects.equals(pageList, sites.pageList)
                && Objects.equals(lemmaList, sites.lemmaList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, statusTime, lastError, url, name, pageList, lemmaList);
    }
    @Override
    public String toString() {
        return "Sites{" +
                "id=" + id +
                ", status=" + status +
                ", statusTime=" + statusTime +
                ", lastError='" + lastError + '\'' +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", pageList=" + pageList +
                ", lemmaList=" + lemmaList +
                '}';
    }
}
