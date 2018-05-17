/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yourasmusic.dbControllers;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import yourasmusic.db.Estudio;
import yourasmusic.db.Reserva;
import yourasmusic.db.Utilizador;
import yourasmusic.dbControllers.exceptions.NonexistentEntityException;
import yourasmusic.dbControllers.exceptions.PreexistingEntityException;

/**
 *
 * @author pedro
 */
public class ReservaJpaController implements Serializable {

    public ReservaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Reserva reserva) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estudio estudioId = reserva.getEstudioId();
            if (estudioId != null) {
                estudioId = em.getReference(estudioId.getClass(), estudioId.getEstudioId());
                reserva.setEstudioId(estudioId);
            }
            Utilizador requerenteId = reserva.getRequerenteId();
            if (requerenteId != null) {
                requerenteId = em.getReference(requerenteId.getClass(), requerenteId.getUtilizadorId());
                reserva.setRequerenteId(requerenteId);
            }
            em.persist(reserva);
            if (estudioId != null) {
                estudioId.getReservaList().add(reserva);
                estudioId = em.merge(estudioId);
            }
            if (requerenteId != null) {
                requerenteId.getReservaList().add(reserva);
                requerenteId = em.merge(requerenteId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findReserva(reserva.getReservaId()) != null) {
                throw new PreexistingEntityException("Reserva " + reserva + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Reserva reserva) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reserva persistentReserva = em.find(Reserva.class, reserva.getReservaId());
            Estudio estudioIdOld = persistentReserva.getEstudioId();
            Estudio estudioIdNew = reserva.getEstudioId();
            Utilizador requerenteIdOld = persistentReserva.getRequerenteId();
            Utilizador requerenteIdNew = reserva.getRequerenteId();
            if (estudioIdNew != null) {
                estudioIdNew = em.getReference(estudioIdNew.getClass(), estudioIdNew.getEstudioId());
                reserva.setEstudioId(estudioIdNew);
            }
            if (requerenteIdNew != null) {
                requerenteIdNew = em.getReference(requerenteIdNew.getClass(), requerenteIdNew.getUtilizadorId());
                reserva.setRequerenteId(requerenteIdNew);
            }
            reserva = em.merge(reserva);
            if (estudioIdOld != null && !estudioIdOld.equals(estudioIdNew)) {
                estudioIdOld.getReservaList().remove(reserva);
                estudioIdOld = em.merge(estudioIdOld);
            }
            if (estudioIdNew != null && !estudioIdNew.equals(estudioIdOld)) {
                estudioIdNew.getReservaList().add(reserva);
                estudioIdNew = em.merge(estudioIdNew);
            }
            if (requerenteIdOld != null && !requerenteIdOld.equals(requerenteIdNew)) {
                requerenteIdOld.getReservaList().remove(reserva);
                requerenteIdOld = em.merge(requerenteIdOld);
            }
            if (requerenteIdNew != null && !requerenteIdNew.equals(requerenteIdOld)) {
                requerenteIdNew.getReservaList().add(reserva);
                requerenteIdNew = em.merge(requerenteIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = reserva.getReservaId();
                if (findReserva(id) == null) {
                    throw new NonexistentEntityException("The reserva with id " + id + " no longer exists.");
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
            Reserva reserva;
            try {
                reserva = em.getReference(Reserva.class, id);
                reserva.getReservaId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reserva with id " + id + " no longer exists.", enfe);
            }
            Estudio estudioId = reserva.getEstudioId();
            if (estudioId != null) {
                estudioId.getReservaList().remove(reserva);
                estudioId = em.merge(estudioId);
            }
            Utilizador requerenteId = reserva.getRequerenteId();
            if (requerenteId != null) {
                requerenteId.getReservaList().remove(reserva);
                requerenteId = em.merge(requerenteId);
            }
            em.remove(reserva);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Reserva> findReservaEntities() {
        return findReservaEntities(true, -1, -1);
    }

    public List<Reserva> findReservaEntities(int maxResults, int firstResult) {
        return findReservaEntities(false, maxResults, firstResult);
    }

    private List<Reserva> findReservaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Reserva.class));
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

    public Reserva findReserva(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Reserva.class, id);
        } finally {
            em.close();
        }
    }

    public int getReservaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Reserva> rt = cq.from(Reserva.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
