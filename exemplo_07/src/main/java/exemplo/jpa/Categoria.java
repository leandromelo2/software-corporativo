package exemplo.jpa;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

@Entity
@Table(name = "TB_CATEGORIA")
@NamedQueries(
        {
            @NamedQuery(
                    name = "Categoria.PorNome",
                    query = "SELECT c FROM Categoria c WHERE c.nome LIKE :nome ORDER BY c.id"
            )
        }
)
@NamedNativeQueries(
        {
            @NamedNativeQuery(
                    name = "Categoria.PorNomeSQL",
                    query = "SELECT ID_CATEGORIA, TXT_NOME, ID_CATEGORIA_MAE FROM TB_CATEGORIA WHERE TXT_NOME LIKE ? ORDER BY ID_CATEGORIA",
                    resultClass = Categoria.class
            ),
            @NamedNativeQuery(
                    name = "Categoria.QuantidadeItensSQL",
                    query = "SELECT c.ID_CATEGORIA, c.TXT_NOME, c.ID_CATEGORIA_MAE, count(ic.ID_ITEM) as TOTAL_ITENS from TB_CATEGORIA c, TB_ITENS_CATEGORIAS ic where c.TXT_NOME LIKE ? and c.ID_CATEGORIA = ic.ID_CATEGORIA GROUP BY c.ID_CATEGORIA",
                    resultSetMapping = "Categoria.QuantidadeItens"
            )
        }
)
@SqlResultSetMapping(
        name = "Categoria.QuantidadeItens",
        entities = {
            @EntityResult(entityClass = Categoria.class)},
        columns = {
            @ColumnResult(name = "TOTAL_ITENS", type = Long.class)}
)
public class Categoria implements Serializable {

    @Id
    @Column(name = "ID_CATEGORIA")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "TXT_NOME", length = 100, nullable = false, unique = true)
    private String nome;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "ID_CATEGORIA_MAE", referencedColumnName = "ID_CATEGORIA")
    private Categoria mae;
    @OneToMany(mappedBy = "mae", orphanRemoval = true)
    private List<Categoria> filhas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Categoria getMae() {
        return mae;
    }

    public void setMae(Categoria mae) {
        this.mae = mae;
    }

    public List<Categoria> getFilhas() {
        return filhas;
    }

    public boolean adicionar(Categoria categoria) {
        return filhas.add(categoria);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Categoria)) {
            return false;
        }
        Categoria other = (Categoria) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "exemplo.jpa.Tag[ id=" + id + ":" + nome + " ]";
    }

}
