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
@Table(name = "lemma")
@NoArgsConstructor
public class Lemma implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", referencedColumnName = "id", nullable = false)
    private Sites sitesId;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String lemma;

    @Column(nullable = false)
    private int frequency;

    @OneToMany(mappedBy = "lemmaId", cascade = CascadeType.ALL)
    private List<ListIndex> index = new ArrayList<>();


    public Lemma(Sites sitesId, String lemma, int frequency) {
        this.sitesId = sitesId;
        this.lemma = lemma;
        this.frequency = frequency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lemma lemma1 = (Lemma) o;
        return id == lemma1.id && frequency == lemma1.frequency && Objects.equals(sitesId, lemma1.sitesId)
                && Objects.equals(lemma, lemma1.lemma) && Objects.equals(index, lemma1.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sitesId, lemma, frequency, index);
    }
    @Override
    public String toString() {
        return "Lemma{" +
                "id=" + id +
                ", sitesId=" + sitesId +
                ", lemma='" + lemma + '\'' +
                ", frequency=" + frequency +
                ", index=" + index +
                '}';
    }
}
