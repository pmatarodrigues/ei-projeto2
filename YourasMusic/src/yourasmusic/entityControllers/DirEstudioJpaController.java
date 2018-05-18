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
import yourasmusic.entities.Utilizador;
import yourasmusic.entities.Estudio;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import yourasmusic.entities.DirEstudio;
import yourasmusic.entityControllers.exceptions.IllegalOrphanException;
import yourasmusic.entityControllers.exceptions.NonexistentEntityException;
import yourasmusic.entityControllers.exceptions.PreexistingEntityException;

/**
 *
 * @author pedro
 */
public class DirEstudioJpaController implements Serializable {

    public DirEstudioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DirEstudio dirEstudio) throws IllegalOrphanException, PreexistingEntityException, Exception {
        if (dirEstudio.getEstudioList() == null) {
            dirEstudio.setEstudioList(new ArrayList<Estudio>());
        }
        List<String> illegalOrphanMessages = null;
        Utilizador utilizadorOrphanCheck = dirEstudio.getUtilizador();
        if (utilizadorOrphanCheck != null) {
            DirEstudio oldDirEstudioOfUtilizador = utilizadorOrphanCheck.getDirEstudio();
            if (oldDirEstudioOfUtilizador != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Utilizador " + utilizadorOrphanCheck + " already has an item of type DirEstudio whose utilizador column cannot be null. Please make another selection for the utilizador field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Utilizador utilizador = dirEstudio.getUtilizador();
            if (utilizador != null) {
                utilizador = em.getReference(utilizador.getClass(), utilizador.getUtilizadorId());
                dirEstudio.setUtilizador(utilizador);
            }
            List<Estudio> attachedEstudioList = new ArrayList<Estudio>();
            for (Estudio estudioListEstudioToAttach : dirEstudio.getEstudioList()) {
                estudioListEstudioToAttach = em.getReference(estudioListEstudioToAttach.getClass(), estudioListEstudioToAttach.getEstudioId());
                attachedEstudioList.add(estudioListEstudioToAttach);
            }
            dirEstudio.setEstudioList(attachedEstudioList);
            em.persist(dirEstudio);
            if (utilizador != null) {
                utilizador.setDirEstudio(dirEstudio);
                utilizador = em.merge(utilizador);
            }
            for (Estudio estudioListEstudio : dirEstudio.getEstudioList()) {
                DirEstudio oldDiretorIdOfEstudioListEstudio = estudioListEstudio.getDiretorId();
                estudioListEstudio.setDiretorId(dirEstudio);
                estudioListEstudio = em.merge(estudioListEstudio);
                if (oldDiretorIdOfEstudioListEstudio != null) {
                    oldDiretorIdOfEstudioListEstudio.getEstudioList().remove(estudioListEstudio);
                    oldDiretorIdOfEstudioListEstudio = em.merge(oldDiretorIdOfEstudioListEstudio);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDirEstudio(dirEstudio.getDirEstudioId()) != null) {
                throw new PreexistingEntityException("DirEstudio " + dirEstudio + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DirEstudio dirEstudio) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DirEstudio persistentDirEstudio = em.find(DirEstudio.class, dirEstudio.getDirEstudioId());
            Utilizador utilizadorOld = persistentDirEstudio.getUtilizador();
            Utilizador utilizadorNew = dirEstudio.getUtilizador();
            List<Estudio> estudioListOld = persistentDirEstudio.getEstudioList();
            List<Estudio> estudioListNew = dirEstudio.getEstudioList();
            List<String> illegalOrphanMessages = null;
            if (utilizadorNew != null && !utilizadorNew.equals(utilizadorOld)) {
                DirEstudio oldDirEstudioOfUtilizador = utilizadorNew.getDirEstudio();
                if (oldDirEstudioOfUtilizador != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Utilizador " + utilizadorNew + " already has an item of type DirEstudio whose utilizador column cannot be null. Please make another selection for the utilizador field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (utilizadorNew != null) {
                utilizadorNew = em.getReference(utilizadorNew.getClass(), utilizadorNew.getUtilizadorId());
                dirEstudio.setUtilizador(utilizadorNew);
            }
            List<Estudio> attachedEstudioListNew = new ArrayList<Estudio>();
            for (Estudio estudioListNewEstudioToAttach : estudioListNew) {
                estudioListNewEstudioToAttach = em.getReference(estudioListNewEstudioToAttach.getClass(), estudioListNewEstudioToAttach.getEstudioId());
                attachedEstudioListNew.add(estudioListNewEstudioToAttach);
            }
            estudioListNew = attachedEstudioListNew;
            dirEstudio.setEstudioList(estudioListNew);
            dirEstudio = em.merge(dirEstudio);
            if (utilizadorOld != null && !utilizadorOld.equals(utilizadorNew)) {
                utilizadorOld.setDirEstudio(null);
                utilizadorOld = em.merge(utilizadorOld);
            }
            if (utilizadorNew != null && !utilizadorNew.equals(utilizadorOld)) {
                utilizadorNew.setDirEstudio(dirEstudio);
                utilizadorNew = em.merge(utilizadorNew);
            }
            for (Estudio estudioListOldEstudio : estudioListOld) {
                if (!estudioListNew.contains(estudioListOldEstudio)) {
                    estudioListOldEstudio.setDiretorId(null);
                    estudioListOldEstudio = em.merge(estudioListOldEstudio);
                }
            }
            for (Estudio estudioListNewEstudio : estudioListNew) {
                if (!estudioListOld.contains(estudioListNewEstudio)) {
                    DirEstudio oldDiretorIdOfEstudioListNewEstudio = estudioListNewEstudio.getDiretorId();
                    estudioListNewEstudio.setDiretorId(dirEstudio);
                    estudioListNewEstudio = em.merge(estudioListNewEstudio);
                    if (oldDiretorIdOfEstudioListNewEstudio != null && !oldDiretorIdOfEstudioListNewEstudio.equals(dirEstudio)) {
                        oldDiretorIdOfEstudioListNewEstudio.getEstudioList().remove(estudioListNewEstudio);
                        oldDiretorIdOfEstudioListNewEstudio = em.merge(oldDiretorIdOfEstudioListNewEstudio);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = dirEstudio.getDirEstudioId();
                if (findDirEstudio(id) == null) {
                    throw new NonexistentEntityException("The dirEstudio with id " + id + " no longer exists.");
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
            DirEstudio dirEstudio;
            try {
                dirEstudio = em.getReference(DirEstudio.class, id);
                dirEstudio.getDirEstudioId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dirEstudio with id " + id + " no longer exists.", enfe);
            }
            Utilizador utilizador = dirEstudio.getUtilizador();
            if (utilizador != null) {
                utilizador.setDirEstudio(null);
                utilizador = em.merge(utilizador);
            }
            List<Estudio> estudioList = dirEstudio.getEstudioList();
            for (Estudio estudioListEstudio : estudioList) {
                estudioListEstudio.setDiretorId(null);
                estudioListEstudio = em.merge(estudioListEstudio);
            }
            em.remove(dirEstudio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DirEstudio> findDirEstudioEntities() {
        return findDirEstudioEntities(true, -1, -1);
    }

    public List<DirEstudio> findDirEstudioEntities(int maxResults, int firstResult) {
        return findDirEstudioEntities(false, maxResults, firstResult);
    }

    private List<DirEstudio> findDirEstudioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DirEstudio.class));
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

    public DirEstudio findDirEstudio(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DirEstudio.class, id);
        } finally {
            em.close();
        }
    }

    public int getDirEstudioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DirEstudio> rt = cq.from(DirEstudio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
