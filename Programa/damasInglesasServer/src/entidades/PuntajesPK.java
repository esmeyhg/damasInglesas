package entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author yamii
 */
@Embeddable
public class PuntajesPK implements Serializable {

  @Basic(optional = false)
  @Column(name = "idPuntajes")
  private int idPuntajes;
  @Basic(optional = false)
  @Column(name = "Usuario_idUsuario")
  private int usuarioidUsuario;

  public PuntajesPK() {
  }

  public PuntajesPK(int idPuntajes, int usuarioidUsuario) {
    this.idPuntajes = idPuntajes;
    this.usuarioidUsuario = usuarioidUsuario;
  }

  public int getIdPuntajes() {
    return idPuntajes;
  }

  public void setIdPuntajes(int idPuntajes) {
    this.idPuntajes = idPuntajes;
  }

  public int getUsuarioidUsuario() {
    return usuarioidUsuario;
  }

  public void setUsuarioidUsuario(int usuarioidUsuario) {
    this.usuarioidUsuario = usuarioidUsuario;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (int) idPuntajes;
    hash += (int) usuarioidUsuario;
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof PuntajesPK)) {
      return false;
    }
    PuntajesPK other = (PuntajesPK) object;
    if (this.idPuntajes != other.idPuntajes) {
      return false;
    }
    if (this.usuarioidUsuario != other.usuarioidUsuario) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Entidades.PuntajesPK[ idPuntajes=" + idPuntajes + ", usuarioidUsuario=" + usuarioidUsuario + " ]";
  }
  
}
