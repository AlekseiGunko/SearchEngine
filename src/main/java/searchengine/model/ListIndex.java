package searchengine.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "words_index")
public class ListIndex implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id", referencedColumnName = "id", nullable = false)
    private Page pageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lemma_id", referencedColumnName = "id", nullable = false)
    private Lemma lemmaId;

    @Column(nullable = false, name = "index_rank")
    private Float rank;

    public ListIndex(Page pageId, Lemma lemmaId, Float rank) {
        this.pageId = pageId;
        this.lemmaId = lemmaId;
        this.rank = rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListIndex indexes = (ListIndex) o;
        return id == indexes.id && Objects.equals(pageId, indexes.pageId) &&
                Objects.equals(lemmaId, indexes.lemmaId) && Objects.equals(rank, indexes.rank);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pageId, lemmaId, rank);
    }

    @Override
    public String toString() {
        return "Indexes{" +
                "id=" + id +
                ", pageId=" + pageId +
                ", lemmaId=" + lemmaId +
                ", rank=" + rank +
                '}';
    }


}
