package controladores;

import controladores.exceptions.IllegalOrphanException;
import controladores.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Puntajes;
import entidades.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author yamii
 */
public class UsuarioJpaController implements Serializable {
  
  public UsuarioJpaController () {
    this.emf = Persistence.createEntityManagerFactory("damasInglesasServerPU");
  }

  public UsuarioJpaController(EntityManagerFactory emf) {
    this.emf = emf;
  }
  private EntityManagerFactory emf = null;

  public EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  public void create(Usuario usuario) {
    if (usuario.getPuntajesList() == null) {
      usuario.setPuntajesList(new ArrayList<Puntajes>());
    }
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      List<Puntajes> attachedPuntajesList = new ArrayList<Puntajes>();
      for (Puntajes puntajesListPuntajesToAttach : usuario.getPuntajesList()) {
        puntajesListPuntajesToAttach = em.getReference(puntajesListPuntajesToAttach.getClass(), puntajesListPuntajesToAttach.getPuntajesPK());
        attachedPuntajesList.add(puntajesListPuntajesToAttach);
      }
      usuario.setPuntajesList(attachedPuntajesList);
      em.persist(usuario);
      for (Puntajes puntajesListPuntajes : usuario.getPuntajesList()) {
        Usuario oldUsuarioOfPuntajesListPuntajes = puntajesListPuntajes.getUsuario();
        puntajesListPuntajes.setUsuario(usuario);
        puntajesListPuntajes = em.merge(puntajesListPuntajes);
        if (oldUsuarioOfPuntajesListPuntajes != null) {
          oldUsuarioOfPuntajesListPuntajes.getPuntajesList().remove(puntajesListPuntajes);
          oldUsuarioOfPuntajesListPuntajes = em.merge(oldUsuarioOfPuntajesListPuntajes);
        }
      }
      em.getTransaction().commit();
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdUsuario());
      List<Puntajes> puntajesListOld = persistentUsuario.getPuntajesList();
      List<Puntajes> puntajesListNew = usuario.getPuntajesList();
      List<String> illegalOrphanMessages = null;
      for (Puntajes puntajesListOldPuntajes : puntajesListOld) {
        if (!puntajesListNew.contains(puntajesListOldPuntajes)) {
          if (illegalOrphanMessages == null) {
            illegalOrphanMessages = new ArrayList<String>();
          }
          illegalOrphanMessages.add("You must retain Puntajes " + puntajesListOldPuntajes + " since its usuario field is not nullable.");
        }
      }
      if (illegalOrphanMessages != null) {
        throw new IllegalOrphanException(illegalOrphanMessages);
      }
      List<Puntajes> attachedPuntajesListNew = new ArrayList<Puntajes>();
      for (Puntajes puntajesListNewPuntajesToAttach : puntajesListNew) {
        puntajesListNewPuntajesToAttach = em.getReference(puntajesListNewPuntajesToAttach.getClass(), puntajesListNewPuntajesToAttach.getPuntajesPK());
        attachedPuntajesListNew.add(puntajesListNewPuntajesToAttach);
      }
      puntajesListNew = attachedPuntajesListNew;
      usuario.setPuntajesList(puntajesListNew);
      usuario = em.merge(usuario);
      for (Puntajes puntajesListNewPuntajes : puntajesListNew) {
        if (!puntajesListOld.contains(puntajesListNewPuntajes)) {
          Usuario oldUsuarioOfPuntajesListNewPuntajes = puntajesListNewPuntajes.getUsuario();
          puntajesListNewPuntajes.setUsuario(usuario);
          puntajesListNewPuntajes = em.merge(puntajesListNewPuntajes);
          if (oldUsuarioOfPuntajesListNewPuntajes != null && !oldUsuarioOfPuntajesListNewPuntajes.equals(usuario)) {
            oldUsuarioOfPuntajesListNewPuntajes.getPuntajesList().remove(puntajesListNewPuntajes);
            oldUsuarioOfPuntajesListNewPuntajes = em.merge(oldUsuarioOfPuntajesListNewPuntajes);
          }
        }
      }
      em.getTransaction().commit();
    } catch (Exception ex) {
      String msg = ex.getLocalizedMessage();
      if (msg == null || msg.length() == 0) {
        Integer id = usuario.getIdUsuario();
        if (findUsuario(id) == null) {
          throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
        }
      }
      throw ex;
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      Usuario usuario;
      try {
        usuario = em.getReference(Usuario.class, id);
        usuario.getIdUsuario();
      } catch (EntityNotFoundException enfe) {
        throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
      }
      List<String> illegalOrphanMessages = null;
      List<Puntajes> puntajesListOrphanCheck = usuario.getPuntajesList();
      for (Puntajes puntajesListOrphanCheckPuntajes : puntajesListOrphanCheck) {
        if (illegalOrphanMessages == null) {
          illegalOrphanMessages = new ArrayList<String>();
        }
        illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Puntajes " + puntajesListOrphanCheckPuntajes + " in its puntajesList field has a non-nullable usuario field.");
      }
      if (illegalOrphanMessages != null) {
        throw new IllegalOrphanException(illegalOrphanMessages);
      }
      em.remove(usuario);
      em.getTransaction().commit();
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public List<Usuario> findUsuarioEntities() {
    return findUsuarioEntities(true, -1, -1);
  }

  public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
    return findUsuarioEntities(false, maxResults, firstResult);
  }

  private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      cq.select(cq.from(Usuario.class));
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

  public Usuario findUsuario(Integer id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(Usuario.class, id);
    } finally {
      em.close();
    }
  }

  public int getUsuarioCount() {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      Root<Usuario> rt = cq.from(Usuario.class);
      cq.select(em.getCriteriaBuilder().count(rt));
      Query q = em.createQuery(cq);
      return ((Long) q.getSingleResult()).intValue();
    } finally {
      em.close();
    }
  }
  
}
