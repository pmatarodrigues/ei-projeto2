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
import yourasmusic.db.Admin;
import yourasmusic.dbControllers.exceptions.IllegalOrphanException;
import yourasmusic.dbControllers.exceptions.NonexistentEntityException;
import yourasmusic.dbControllers.exceptions.PreexistingEntityException;

/**
 *
 * @author pedro
 */
public class AdminJpaController implements Serializable {

    public AdminJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Admin admin) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Utilizador utilizadorOrphanCheck = admin.getUtilizador();
        if (utilizadorOrphanCheck != null) {
            Admin oldAdminOfUtilizador = utilizadorOrphanCheck.getAdmin();
            if (oldAdminOfUtilizador != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Utilizador " + utilizadorOrphanCheck + " already has an item of type Admin whose utilizador column cannot be null. Please make another selection for the utilizador field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Utilizador utilizador = admin.getUtilizador();
            if (utilizador != null) {
                utilizador = em.getReference(utilizador.getClass(), utilizador.getUtilizadorId());
                admin.setUtilizador(utilizador);
            }
            em.persist(admin);
            if (utilizador != null) {
                utilizador.setAdmin(admin);
                utilizador = em.merge(utilizador);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAdmin(admin.getAdminId()) != null) {
                throw new PreexistingEntityException("Admin " + admin + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Admin admin) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Admin persistentAdmin = em.find(Admin.class, admin.getAdminId());
            Utilizador utilizadorOld = persistentAdmin.getUtilizador();
            Utilizador utilizadorNew = admin.getUtilizador();
            List<String> illegalOrphanMessages = null;
            if (utilizadorNew != null && !utilizadorNew.equals(utilizadorOld)) {
                Admin oldAdminOfUtilizador = utilizadorNew.getAdmin();
                if (oldAdminOfUtilizador != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Utilizador " + utilizadorNew + " already has an item of type Admin whose utilizador column cannot be null. Please make another selection for the utilizador field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (utilizadorNew != null) {
                utilizadorNew = em.getReference(utilizadorNew.getClass(), utilizadorNew.getUtilizadorId());
                admin.setUtilizador(utilizadorNew);
            }
            admin = em.merge(admin);
            if (utilizadorOld != null && !utilizadorOld.equals(utilizadorNew)) {
                utilizadorOld.setAdmin(null);
                utilizadorOld = em.merge(utilizadorOld);
            }
            if (utilizadorNew != null && !utilizadorNew.equals(utilizadorOld)) {
                utilizadorNew.setAdmin(admin);
                utilizadorNew = em.merge(utilizadorNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = admin.getAdminId();
                if (findAdmin(id) == null) {
                    throw new NonexistentEntityException("The admin with id " + id + " no longer exists.");
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
            Admin admin;
            try {
                admin = em.getReference(Admin.class, id);
                admin.getAdminId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The admin with id " + id + " no longer exists.", enfe);
            }
            Utilizador utilizador = admin.getUtilizador();
            if (utilizador != null) {
                utilizador.setAdmin(null);
                utilizador = em.merge(utilizador);
            }
            em.remove(admin);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Admin> findAdminEntities() {
        return findAdminEntities(true, -1, -1);
    }

    public List<Admin> findAdminEntities(int maxResults, int firstResult) {
        return findAdminEntities(false, maxResults, firstResult);
    }

    private List<Admin> findAdminEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Admin.class));
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

    public Admin findAdmin(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Admin.class, id);
        } finally {
            em.close();
        }
    }

    public int getAdminCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Admin> rt = cq.from(Admin.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
