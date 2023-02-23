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
@Table(name = "lemma", indexes = {@Index(name = "lemma_list", columnList = "lemma")})
@NoArgsConstructor
public class LemmaEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", referencedColumnName = "id", nullable = false)
    private SiteEntity siteEntityId;
    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String lemma;

    @Column(nullable = false)
    private int frequency;

    @OneToMany(mappedBy = "lemma", cascade = CascadeType.ALL)
    private List<IndexEntity> index = new ArrayList<>();


    public LemmaEntity(String lemma, int frequency, SiteEntity siteEntityId) {
        this.lemma = lemma;
        this.frequency = frequency;
        this.siteEntityId = siteEntityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LemmaEntity that = (LemmaEntity) o;
        return id == that.id && frequency == that.frequency &&
                siteEntityId.equals(that.siteEntityId) &&
                lemma.equals(that.lemma) &&
                index.equals(that.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, siteEntityId, lemma, frequency, index);
    }

    @Override
    public String toString() {
        return "LemmaEntity{" +
                "id=" + id +
                ", siteEntityId=" + siteEntityId +
                ", lemma='" + lemma + '\'' +
                ", frequency=" + frequency +
                ", index=" + index +
                '}';
    }
}
