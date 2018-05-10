/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yourasmusic.dbControllers;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import yourasmusic.db.Utilizador;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import yourasmusic.db.Editora;
import yourasmusic.dbControllers.exceptions.IllegalOrphanException;
import yourasmusic.dbControllers.exceptions.NonexistentEntityException;
import yourasmusic.dbControllers.exceptions.PreexistingEntityException;

/**
 *
 * @author pedro
 */
public class EditoraJpaController implements Serializable {

    public EditoraJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Editora editora) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Utilizador utilizadorOrphanCheck = editora.getUtilizador();
        if (utilizadorOrphanCheck != null) {
            Editora oldEditoraOfUtilizador = utilizadorOrphanCheck.getEditora();
            if (oldEditoraOfUtilizador != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Utilizador " + utilizadorOrphanCheck + " already has an item of type Editora whose utilizador column cannot be null. Please make another selection for the utilizador field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Utilizador utilizador = editora.getUtilizador();
            if (utilizador != null) {
                utilizador = em.getReference(utilizador.getClass(), utilizador.getUtilizadorId());
                editora.setUtilizador(utilizador);
            }
            em.persist(editora);
            if (utilizador != null) {
                utilizador.setEditora(editora);
                utilizador = em.merge(utilizador);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEditora(editora.getEditoraId()) != null) {
                throw new PreexistingEntityException("Editora " + editora + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Editora editora) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Editora persistentEditora = em.find(Editora.class, editora.getEditoraId());
            Utilizador utilizadorOld = persistentEditora.getUtilizador();
            Utilizador utilizadorNew = editora.getUtilizador();
            List<String> illegalOrphanMessages = null;
            if (utilizadorNew != null && !utilizadorNew.equals(utilizadorOld)) {
                Editora oldEditoraOfUtilizador = utilizadorNew.getEditora();
                if (oldEditoraOfUtilizador != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Utilizador " + utilizadorNew + " already has an item of type Editora whose utilizador column cannot be null. Please make another selection for the utilizador field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (utilizadorNew != null) {
                utilizadorNew = em.getReference(utilizadorNew.getClass(), utilizadorNew.getUtilizadorId());
                editora.setUtilizador(utilizadorNew);
            }
            editora = em.merge(editora);
            if (utilizadorOld != null && !utilizadorOld.equals(utilizadorNew)) {
                utilizadorOld.setEditora(null);
                utilizadorOld = em.merge(utilizadorOld);
            }
            if (utilizadorNew != null && !utilizadorNew.equals(utilizadorOld)) {
                utilizadorNew.setEditora(editora);
                utilizadorNew = em.merge(utilizadorNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = editora.getEditoraId();
                if (findEditora(id) == null) {
                    throw new NonexistentEntityException("The editora with id " + id + " no longer exists.");
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
            Editora editora;
            try {
                editora = em.getReference(Editora.class, id);
                editora.getEditoraId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The editora with id " + id + " no longer exists.", enfe);
            }
            Utilizador utilizador = editora.getUtilizador();
            if (utilizador != null) {
                utilizador.setEditora(null);
                utilizador = em.merge(utilizador);
            }
            em.remove(editora);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Editora> findEditoraEntities() {
        return findEditoraEntities(true, -1, -1);
    }

    public List<Editora> findEditoraEntities(int maxResults, int firstResult) {
        return findEditoraEntities(false, maxResults, firstResult);
    }

    private List<Editora> findEditoraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Editora.class));
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

    public Editora findEditora(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Editora.class, id);
        } finally {
            em.close();
        }
    }

    public int getEditoraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Editora> rt = cq.from(Editora.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
