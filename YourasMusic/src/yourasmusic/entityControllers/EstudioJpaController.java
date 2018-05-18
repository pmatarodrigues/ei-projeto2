/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yourasmusic.entityControllers;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import yourasmusic.entities.DirEstudio;
import yourasmusic.entities.Reserva;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import yourasmusic.entities.Estudio;
import yourasmusic.entityControllers.exceptions.NonexistentEntityException;
import yourasmusic.entityControllers.exceptions.PreexistingEntityException;

/**
 *
 * @author pedro
 */
public class EstudioJpaController implements Serializable {

    public EstudioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Estudio estudio) throws PreexistingEntityException, Exception {
        if (estudio.getReservaList() == null) {
            estudio.setReservaList(new ArrayList<Reserva>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DirEstudio diretorId = estudio.getDiretorId();
            if (diretorId != null) {
                diretorId = em.getReference(diretorId.getClass(), diretorId.getDirEstudioId());
                estudio.setDiretorId(diretorId);
            }
            List<Reserva> attachedReservaList = new ArrayList<Reserva>();
            for (Reserva reservaListReservaToAttach : estudio.getReservaList()) {
                reservaListReservaToAttach = em.getReference(reservaListReservaToAttach.getClass(), reservaListReservaToAttach.getReservaId());
                attachedReservaList.add(reservaListReservaToAttach);
            }
            estudio.setReservaList(attachedReservaList);
            em.persist(estudio);
            if (diretorId != null) {
                diretorId.getEstudioList().add(estudio);
                diretorId = em.merge(diretorId);
            }
            for (Reserva reservaListReserva : estudio.getReservaList()) {
                Estudio oldEstudioIdOfReservaListReserva = reservaListReserva.getEstudioId();
                reservaListReserva.setEstudioId(estudio);
                reservaListReserva = em.merge(reservaListReserva);
                if (oldEstudioIdOfReservaListReserva != null) {
                    oldEstudioIdOfReservaListReserva.getReservaList().remove(reservaListReserva);
                    oldEstudioIdOfReservaListReserva = em.merge(oldEstudioIdOfReservaListReserva);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEstudio(estudio.getEstudioId()) != null) {
                throw new PreexistingEntityException("Estudio " + estudio + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Estudio estudio) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estudio persistentEstudio = em.find(Estudio.class, estudio.getEstudioId());
            DirEstudio diretorIdOld = persistentEstudio.getDiretorId();
            DirEstudio diretorIdNew = estudio.getDiretorId();
            List<Reserva> reservaListOld = persistentEstudio.getReservaList();
            List<Reserva> reservaListNew = estudio.getReservaList();
            if (diretorIdNew != null) {
                diretorIdNew = em.getReference(diretorIdNew.getClass(), diretorIdNew.getDirEstudioId());
                estudio.setDiretorId(diretorIdNew);
            }
            List<Reserva> attachedReservaListNew = new ArrayList<Reserva>();
            for (Reserva reservaListNewReservaToAttach : reservaListNew) {
                reservaListNewReservaToAttach = em.getReference(reservaListNewReservaToAttach.getClass(), reservaListNewReservaToAttach.getReservaId());
                attachedReservaListNew.add(reservaListNewReservaToAttach);
            }
            reservaListNew = attachedReservaListNew;
            estudio.setReservaList(reservaListNew);
            estudio = em.merge(estudio);
            if (diretorIdOld != null && !diretorIdOld.equals(diretorIdNew)) {
                diretorIdOld.getEstudioList().remove(estudio);
                diretorIdOld = em.merge(diretorIdOld);
            }
            if (diretorIdNew != null && !diretorIdNew.equals(diretorIdOld)) {
                diretorIdNew.getEstudioList().add(estudio);
                diretorIdNew = em.merge(diretorIdNew);
            }
            for (Reserva reservaListOldReserva : reservaListOld) {
                if (!reservaListNew.contains(reservaListOldReserva)) {
                    reservaListOldReserva.setEstudioId(null);
                    reservaListOldReserva = em.merge(reservaListOldReserva);
                }
            }
            for (Reserva reservaListNewReserva : reservaListNew) {
                if (!reservaListOld.contains(reservaListNewReserva)) {
                    Estudio oldEstudioIdOfReservaListNewReserva = reservaListNewReserva.getEstudioId();
                    reservaListNewReserva.setEstudioId(estudio);
                    reservaListNewReserva = em.merge(reservaListNewReserva);
                    if (oldEstudioIdOfReservaListNewReserva != null && !oldEstudioIdOfReservaListNewReserva.equals(estudio)) {
                        oldEstudioIdOfReservaListNewReserva.getReservaList().remove(reservaListNewReserva);
                        oldEstudioIdOfReservaListNewReserva = em.merge(oldEstudioIdOfReservaListNewReserva);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = estudio.getEstudioId();
                if (findEstudio(id) == null) {
                    throw new NonexistentEntityException("The estudio with id " + id + " no longer exists.");
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
            Estudio estudio;
            try {
                estudio = em.getReference(Estudio.class, id);
                estudio.getEstudioId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estudio with id " + id + " no longer exists.", enfe);
            }
            DirEstudio diretorId = estudio.getDiretorId();
            if (diretorId != null) {
                diretorId.getEstudioList().remove(estudio);
                diretorId = em.merge(diretorId);
            }
            List<Reserva> reservaList = estudio.getReservaList();
            for (Reserva reservaListReserva : reservaList) {
                reservaListReserva.setEstudioId(null);
                reservaListReserva = em.merge(reservaListReserva);
            }
            em.remove(estudio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Estudio> findEstudioEntities() {
        return findEstudioEntities(true, -1, -1);
    }

    public List<Estudio> findEstudioEntities(int maxResults, int firstResult) {
        return findEstudioEntities(false, maxResults, firstResult);
    }

    private List<Estudio> findEstudioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Estudio.class));
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

    public Estudio findEstudio(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Estudio.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstudioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Estudio> rt = cq.from(Estudio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
