package searchengine.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "page", indexes = {@Index(name = "path_list" ,columnList = "path")})
@NoArgsConstructor
public class Page implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "site_id", referencedColumnName = "id")
    private Sites siteId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String path;

    @Column(nullable = false)
    private int code;

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    private String content;

    @OneToMany(mappedBy = "pageId", cascade = CascadeType.ALL)
    private List<IndexEntity> index = new ArrayList<>();

    public Page(Sites siteId, String path, int code, String content) {
        this.siteId = siteId;
        this.path = path;
        this.code = code;
        this.content = content;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Page page = (Page) o;
        return id == page.id && code == page.code && Objects.equals(siteId, page.siteId)
                && Objects.equals(path, page.path) && Objects.equals(content, page.content)
                && Objects.equals(index, page.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, siteId, path, code, content, index);
    }

    @Override
    public String toString() {
        return "Page{" +
                "id=" + id +
                ", siteId=" + siteId +
                ", path='" + path + '\'' +
                ", code=" + code +
                ", content='" + content + '\'' +
                ", index=" + index +
                '}';
    }





}
