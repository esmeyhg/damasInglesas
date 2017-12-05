package controladores;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author yamii
 */
public class PuntajesJpaController implements Serializable {

  public PuntajesJpaController() {
    this.emf = Persistence.createEntityManagerFactory("damasInglesasServerPU");
  }
  private EntityManagerFactory emf = null;

  public EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  public void create(entidades.Puntajes puntajes) 
      throws controladores.exceptions.PreexistingEntityException, Exception {
    if (puntajes.getPuntajesPK() == null) {
      puntajes.setPuntajesPK(new entidades.PuntajesPK());
    }
    puntajes.getPuntajesPK().setUsuarioidUsuario(puntajes.getUsuario().getIdUsuario());
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      entidades.Usuario usuario = puntajes.getUsuario();
      if (usuario != null) {
        usuario = em.getReference(usuario.getClass(), usuario.getIdUsuario());
        puntajes.setUsuario(usuario);
      }
      em.persist(puntajes);
      if (usuario != null) {
        usuario.getPuntajesList().add(puntajes);
        usuario = em.merge(usuario);
      }
      em.getTransaction().commit();
    } catch (Exception ex) {
      if (findPuntajes(puntajes.getPuntajesPK()) != null) {
        throw new controladores.exceptions.PreexistingEntityException
    ("Puntajes " + puntajes + " already exists.", ex);
      }
      throw ex;
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void edit(entidades.Puntajes puntajes) 
      throws controladores.exceptions.NonexistentEntityException, Exception {
    puntajes.getPuntajesPK().setUsuarioidUsuario(puntajes.getUsuario().getIdUsuario());
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      entidades.Puntajes persistentPuntajes = 
          em.find(entidades.Puntajes.class, puntajes.getPuntajesPK());
      entidades.Usuario usuarioOld = persistentPuntajes.getUsuario();
      entidades.Usuario usuarioNew = puntajes.getUsuario();
      if (usuarioNew != null) {
        usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getIdUsuario());
        puntajes.setUsuario(usuarioNew);
      }
      puntajes = em.merge(puntajes);
      if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
        usuarioOld.getPuntajesList().remove(puntajes);
        usuarioOld = em.merge(usuarioOld);
      }
      if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
        usuarioNew.getPuntajesList().add(puntajes);
        usuarioNew = em.merge(usuarioNew);
      }
      em.getTransaction().commit();
    } catch (Exception ex) {
      String msg = ex.getLocalizedMessage();
      if (msg == null || msg.length() == 0) {
        entidades.PuntajesPK id = puntajes.getPuntajesPK();
        if (findPuntajes(id) == null) {
          throw new controladores.exceptions.NonexistentEntityException
    ("The puntajes with id " + id + " no longer exists.");
        }
      }
      throw ex;
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void destroy(entidades.PuntajesPK id) 
      throws controladores.exceptions.NonexistentEntityException {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      entidades.Puntajes puntajes;
      try {
        puntajes = em.getReference(entidades.Puntajes.class, id);
        puntajes.getPuntajesPK();
      } catch (EntityNotFoundException enfe) {
        throw new controladores.exceptions.NonexistentEntityException
    ("The puntajes with id " + id + " no longer exists.", enfe);
      }
      entidades.Usuario usuario = puntajes.getUsuario();
      if (usuario != null) {
        usuario.getPuntajesList().remove(puntajes);
        usuario = em.merge(usuario);
      }
      em.remove(puntajes);
      em.getTransaction().commit();
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public List<entidades.Puntajes> findPuntajesEntities() {
    return findPuntajesEntities(true, -1, -1);
  }

  public List<entidades.Puntajes> findPuntajesEntities(int maxResults, int firstResult) {
    return findPuntajesEntities(false, maxResults, firstResult);
  }

  private List<entidades.Puntajes> findPuntajesEntities(boolean all, int maxResults, int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      cq.select(cq.from(entidades.Puntajes.class));
      Query q = em.createQuery(cq);
      if (!all) {
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
      }
      return q.getResultList();
    } finally {
      em.close();
    }
  }

  public entidades.Puntajes findPuntajes(entidades.PuntajesPK id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(entidades.Puntajes.class, id);
    } finally {
      em.close();
    }
  }

  public int getPuntajesCount() {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      Root<entidades.Puntajes> rt = cq.from(entidades.Puntajes.class);
      cq.select(em.getCriteriaBuilder().count(rt));
      Query q = em.createQuery(cq);
      return ((Long) q.getSingleResult()).intValue();
    } finally {
      em.close();
    }
  }
  
}
