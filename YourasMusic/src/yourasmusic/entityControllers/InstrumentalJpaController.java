/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yourasmusic.entityControllers;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import yourasmusic.entities.Instrumental;
import yourasmusic.entityControllers.exceptions.NonexistentEntityException;
import yourasmusic.entityControllers.exceptions.PreexistingEntityException;

/**
 *
 * @author pedro
 */
public class InstrumentalJpaController implements Serializable {

    public InstrumentalJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Instrumental instrumental) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(instrumental);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findInstrumental(instrumental.getInstrumentalId()) != null) {
                throw new PreexistingEntityException("Instrumental " + instrumental + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Instrumental instrumental) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            instrumental = em.merge(instrumental);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = instrumental.getInstrumentalId();
                if (findInstrumental(id) == null) {
                    throw new NonexistentEntityException("The instrumental with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Instrumental instrumental;
            try {
                instrumental = em.getReference(Instrumental.class, id);
                instrumental.getInstrumentalId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The instrumental with id " + id + " no longer exists.", enfe);
            }
            em.remove(instrumental);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Instrumental> findInstrumentalEntities() {
        return findInstrumentalEntities(true, -1, -1);
    }

    public List<Instrumental> findInstrumentalEntities(int maxResults, int firstResult) {
        return findInstrumentalEntities(false, maxResults, firstResult);
    }

    private List<Instrumental> findInstrumentalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Instrumental.class));
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

    public Instrumental findInstrumental(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Instrumental.class, id);
        } finally {
            em.close();
        }
    }

    public int getInstrumentalCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Instrumental> rt = cq.from(Instrumental.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
