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
import yourasmusic.entities.Artista;
import yourasmusic.entityControllers.exceptions.IllegalOrphanException;
import yourasmusic.entityControllers.exceptions.NonexistentEntityException;
import yourasmusic.entityControllers.exceptions.PreexistingEntityException;

/**
 *
 * @author pedro
 */
public class ArtistaJpaController implements Serializable {

    public ArtistaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Artista artista) throws IllegalOrphanException, PreexistingEntityException, Exception {
        if (artista.getAlbumList() == null) {
            artista.setAlbumList(new ArrayList<Album>());
        }
        List<String> illegalOrphanMessages = null;
        Utilizador utilizadorOrphanCheck = artista.getUtilizador();
        if (utilizadorOrphanCheck != null) {
            Artista oldArtistaOfUtilizador = utilizadorOrphanCheck.getArtista();
            if (oldArtistaOfUtilizador != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Utilizador " + utilizadorOrphanCheck + " already has an item of type Artista whose utilizador column cannot be null. Please make another selection for the utilizador field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Utilizador utilizador = artista.getUtilizador();
            if (utilizador != null) {
                utilizador = em.getReference(utilizador.getClass(), utilizador.getUtilizadorId());
                artista.setUtilizador(utilizador);
            }
            List<Album> attachedAlbumList = new ArrayList<Album>();
            for (Album albumListAlbumToAttach : artista.getAlbumList()) {
                albumListAlbumToAttach = em.getReference(albumListAlbumToAttach.getClass(), albumListAlbumToAttach.getAlbumId());
                attachedAlbumList.add(albumListAlbumToAttach);
            }
            artista.setAlbumList(attachedAlbumList);
            em.persist(artista);
            if (utilizador != null) {
                utilizador.setArtista(artista);
                utilizador = em.merge(utilizador);
            }
            for (Album albumListAlbum : artista.getAlbumList()) {
                Artista oldArtistaIdOfAlbumListAlbum = albumListAlbum.getArtistaId();
                albumListAlbum.setArtistaId(artista);
                albumListAlbum = em.merge(albumListAlbum);
                if (oldArtistaIdOfAlbumListAlbum != null) {
                    oldArtistaIdOfAlbumListAlbum.getAlbumList().remove(albumListAlbum);
                    oldArtistaIdOfAlbumListAlbum = em.merge(oldArtistaIdOfAlbumListAlbum);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findArtista(artista.getArtistaId()) != null) {
                throw new PreexistingEntityException("Artista " + artista + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Artista artista) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Artista persistentArtista = em.find(Artista.class, artista.getArtistaId());
            Utilizador utilizadorOld = persistentArtista.getUtilizador();
            Utilizador utilizadorNew = artista.getUtilizador();
            List<Album> albumListOld = persistentArtista.getAlbumList();
            List<Album> albumListNew = artista.getAlbumList();
            List<String> illegalOrphanMessages = null;
            if (utilizadorNew != null && !utilizadorNew.equals(utilizadorOld)) {
                Artista oldArtistaOfUtilizador = utilizadorNew.getArtista();
                if (oldArtistaOfUtilizador != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Utilizador " + utilizadorNew + " already has an item of type Artista whose utilizador column cannot be null. Please make another selection for the utilizador field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (utilizadorNew != null) {
                utilizadorNew = em.getReference(utilizadorNew.getClass(), utilizadorNew.getUtilizadorId());
                artista.setUtilizador(utilizadorNew);
            }
            List<Album> attachedAlbumListNew = new ArrayList<Album>();
            for (Album albumListNewAlbumToAttach : albumListNew) {
                albumListNewAlbumToAttach = em.getReference(albumListNewAlbumToAttach.getClass(), albumListNewAlbumToAttach.getAlbumId());
                attachedAlbumListNew.add(albumListNewAlbumToAttach);
            }
            albumListNew = attachedAlbumListNew;
            artista.setAlbumList(albumListNew);
            artista = em.merge(artista);
            if (utilizadorOld != null && !utilizadorOld.equals(utilizadorNew)) {
                utilizadorOld.setArtista(null);
                utilizadorOld = em.merge(utilizadorOld);
            }
            if (utilizadorNew != null && !utilizadorNew.equals(utilizadorOld)) {
                utilizadorNew.setArtista(artista);
                utilizadorNew = em.merge(utilizadorNew);
            }
            for (Album albumListOldAlbum : albumListOld) {
                if (!albumListNew.contains(albumListOldAlbum)) {
                    albumListOldAlbum.setArtistaId(null);
                    albumListOldAlbum = em.merge(albumListOldAlbum);
                }
            }
            for (Album albumListNewAlbum : albumListNew) {
                if (!albumListOld.contains(albumListNewAlbum)) {
                    Artista oldArtistaIdOfAlbumListNewAlbum = albumListNewAlbum.getArtistaId();
                    albumListNewAlbum.setArtistaId(artista);
                    albumListNewAlbum = em.merge(albumListNewAlbum);
                    if (oldArtistaIdOfAlbumListNewAlbum != null && !oldArtistaIdOfAlbumListNewAlbum.equals(artista)) {
                        oldArtistaIdOfAlbumListNewAlbum.getAlbumList().remove(albumListNewAlbum);
                        oldArtistaIdOfAlbumListNewAlbum = em.merge(oldArtistaIdOfAlbumListNewAlbum);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = artista.getArtistaId();
                if (findArtista(id) == null) {
                    throw new NonexistentEntityException("The artista with id " + id + " no longer exists.");
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
            Artista artista;
            try {
                artista = em.getReference(Artista.class, id);
                artista.getArtistaId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The artista with id " + id + " no longer exists.", enfe);
            }
            Utilizador utilizador = artista.getUtilizador();
            if (utilizador != null) {
                utilizador.setArtista(null);
                utilizador = em.merge(utilizador);
            }
            List<Album> albumList = artista.getAlbumList();
            for (Album albumListAlbum : albumList) {
                albumListAlbum.setArtistaId(null);
                albumListAlbum = em.merge(albumListAlbum);
            }
            em.remove(artista);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Artista> findArtistaEntities() {
        return findArtistaEntities(true, -1, -1);
    }

    public List<Artista> findArtistaEntities(int maxResults, int firstResult) {
        return findArtistaEntities(false, maxResults, firstResult);
    }

    private List<Artista> findArtistaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Artista.class));
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

    public Artista findArtista(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Artista.class, id);
        } finally {
            em.close();
        }
    }

    public int getArtistaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Artista> rt = cq.from(Artista.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
