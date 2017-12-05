package entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author yamii
 */
@Entity
@Table(name = "puntajes")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Puntajes.findAll", query = "SELECT p FROM Puntajes p")
  , @NamedQuery(name = "Puntajes.findByIdPuntajes", query = "SELECT p FROM Puntajes p WHERE p.puntajesPK.idPuntajes = :idPuntajes")
  , @NamedQuery(name = "Puntajes.findByPuntajes", query = "SELECT p FROM Puntajes p WHERE p.puntajes = :puntajes")
  , @NamedQuery(name = "Puntajes.findByUsuarioidUsuario", query = "SELECT p FROM Puntajes p WHERE p.puntajesPK.usuarioidUsuario = :usuarioidUsuario")})
public class Puntajes implements Serializable {

  private static final long serialVersionUID = 1L;
  @EmbeddedId
  protected PuntajesPK puntajesPK;
  @Column(name = "puntajes")
  private Integer puntajes;
  @JoinColumn(name = "Usuario_idUsuario", referencedColumnName = "idUsuario", insertable = false, updatable = false)
  @ManyToOne(optional = false)
  private Usuario usuario;

  public Puntajes() {
  }

  public Puntajes(PuntajesPK puntajesPK) {
    this.puntajesPK = puntajesPK;
  }

  public Puntajes(int idPuntajes, int usuarioidUsuario) {
    this.puntajesPK = new PuntajesPK(idPuntajes, usuarioidUsuario);
  }

  public PuntajesPK getPuntajesPK() {
    return puntajesPK;
  }

  public void setPuntajesPK(PuntajesPK puntajesPK) {
    this.puntajesPK = puntajesPK;
  }

  public Integer getPuntajes() {
    return puntajes;
  }

  public void setPuntajes(Integer puntajes) {
    this.puntajes = puntajes;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (puntajesPK != null ? puntajesPK.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Puntajes)) {
      return false;
    }
    Puntajes other = (Puntajes) object;
    if ((this.puntajesPK == null && other.puntajesPK != null) || (this.puntajesPK != null && !this.puntajesPK.equals(other.puntajesPK))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Entidades.Puntajes[ puntajesPK=" + puntajesPK + " ]";
  }
  
}
