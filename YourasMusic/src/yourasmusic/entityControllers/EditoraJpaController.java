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
import yourasmusic.entities.Album;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import yourasmusic.entities.Editora;
import yourasmusic.entityControllers.exceptions.IllegalOrphanException;
import yourasmusic.entityControllers.exceptions.NonexistentEntityException;
import yourasmusic.entityControllers.exceptions.PreexistingEntityException;

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
        if (editora.getAlbumList() == null) {
            editora.setAlbumList(new ArrayList<Album>());
        }
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
            List<Album> attachedAlbumList = new ArrayList<Album>();
            for (Album albumListAlbumToAttach : editora.getAlbumList()) {
                albumListAlbumToAttach = em.getReference(albumListAlbumToAttach.getClass(), albumListAlbumToAttach.getAlbumId());
                attachedAlbumList.add(albumListAlbumToAttach);
            }
            editora.setAlbumList(attachedAlbumList);
            em.persist(editora);
            if (utilizador != null) {
                utilizador.setEditora(editora);
                utilizador = em.merge(utilizador);
            }
            for (Album albumListAlbum : editora.getAlbumList()) {
                Editora oldEditoraIdOfAlbumListAlbum = albumListAlbum.getEditoraId();
                albumListAlbum.setEditoraId(editora);
                albumListAlbum = em.merge(albumListAlbum);
                if (oldEditoraIdOfAlbumListAlbum != null) {
                    oldEditoraIdOfAlbumListAlbum.getAlbumList().remove(albumListAlbum);
                    oldEditoraIdOfAlbumListAlbum = em.merge(oldEditoraIdOfAlbumListAlbum);
                }
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
            List<Album> albumListOld = persistentEditora.getAlbumList();
            List<Album> albumListNew = editora.getAlbumList();
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
            List<Album> attachedAlbumListNew = new ArrayList<Album>();
            for (Album albumListNewAlbumToAttach : albumListNew) {
                albumListNewAlbumToAttach = em.getReference(albumListNewAlbumToAttach.getClass(), albumListNewAlbumToAttach.getAlbumId());
                attachedAlbumListNew.add(albumListNewAlbumToAttach);
            }
            albumListNew = attachedAlbumListNew;
            editora.setAlbumList(albumListNew);
            editora = em.merge(editora);
            if (utilizadorOld != null && !utilizadorOld.equals(utilizadorNew)) {
                utilizadorOld.setEditora(null);
                utilizadorOld = em.merge(utilizadorOld);
            }
            if (utilizadorNew != null && !utilizadorNew.equals(utilizadorOld)) {
                utilizadorNew.setEditora(editora);
                utilizadorNew = em.merge(utilizadorNew);
            }
            for (Album albumListOldAlbum : albumListOld) {
                if (!albumListNew.contains(albumListOldAlbum)) {
                    albumListOldAlbum.setEditoraId(null);
                    albumListOldAlbum = em.merge(albumListOldAlbum);
                }
            }
            for (Album albumListNewAlbum : albumListNew) {
                if (!albumListOld.contains(albumListNewAlbum)) {
                    Editora oldEditoraIdOfAlbumListNewAlbum = albumListNewAlbum.getEditoraId();
                    albumListNewAlbum.setEditoraId(editora);
                    albumListNewAlbum = em.merge(albumListNewAlbum);
                    if (oldEditoraIdOfAlbumListNewAlbum != null && !oldEditoraIdOfAlbumListNewAlbum.equals(editora)) {
                        oldEditoraIdOfAlbumListNewAlbum.getAlbumList().remove(albumListNewAlbum);
                        oldEditoraIdOfAlbumListNewAlbum = em.merge(oldEditoraIdOfAlbumListNewAlbum);
                    }
                }
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
            List<Album> albumList = editora.getAlbumList();
            for (Album albumListAlbum : albumList) {
                albumListAlbum.setEditoraId(null);
                albumListAlbum = em.merge(albumListAlbum);
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
