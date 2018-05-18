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
import yourasmusic.entities.Album;
import yourasmusic.entities.Musica;
import yourasmusic.entities.Utilizador;
import yourasmusic.entityControllers.exceptions.NonexistentEntityException;
import yourasmusic.entityControllers.exceptions.PreexistingEntityException;

/**
 *
 * @author pedro
 */
public class MusicaJpaController implements Serializable {

    public MusicaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Musica musica) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Album albumId = musica.getAlbumId();
            if (albumId != null) {
                albumId = em.getReference(albumId.getClass(), albumId.getAlbumId());
                musica.setAlbumId(albumId);
            }
            Utilizador artistaId = musica.getArtistaId();
            if (artistaId != null) {
                artistaId = em.getReference(artistaId.getClass(), artistaId.getUtilizadorId());
                musica.setArtistaId(artistaId);
            }
            em.persist(musica);
            if (albumId != null) {
                albumId.getMusicaList().add(musica);
                albumId = em.merge(albumId);
            }
            if (artistaId != null) {
                artistaId.getMusicaList().add(musica);
                artistaId = em.merge(artistaId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMusica(musica.getMusicaId()) != null) {
                throw new PreexistingEntityException("Musica " + musica + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Musica musica) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Musica persistentMusica = em.find(Musica.class, musica.getMusicaId());
            Album albumIdOld = persistentMusica.getAlbumId();
            Album albumIdNew = musica.getAlbumId();
            Utilizador artistaIdOld = persistentMusica.getArtistaId();
            Utilizador artistaIdNew = musica.getArtistaId();
            if (albumIdNew != null) {
                albumIdNew = em.getReference(albumIdNew.getClass(), albumIdNew.getAlbumId());
                musica.setAlbumId(albumIdNew);
            }
            if (artistaIdNew != null) {
                artistaIdNew = em.getReference(artistaIdNew.getClass(), artistaIdNew.getUtilizadorId());
                musica.setArtistaId(artistaIdNew);
            }
            musica = em.merge(musica);
            if (albumIdOld != null && !albumIdOld.equals(albumIdNew)) {
                albumIdOld.getMusicaList().remove(musica);
                albumIdOld = em.merge(albumIdOld);
            }
            if (albumIdNew != null && !albumIdNew.equals(albumIdOld)) {
                albumIdNew.getMusicaList().add(musica);
                albumIdNew = em.merge(albumIdNew);
            }
            if (artistaIdOld != null && !artistaIdOld.equals(artistaIdNew)) {
                artistaIdOld.getMusicaList().remove(musica);
                artistaIdOld = em.merge(artistaIdOld);
            }
            if (artistaIdNew != null && !artistaIdNew.equals(artistaIdOld)) {
                artistaIdNew.getMusicaList().add(musica);
                artistaIdNew = em.merge(artistaIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = musica.getMusicaId();
                if (findMusica(id) == null) {
                    throw new NonexistentEntityException("The musica with id " + id + " no longer exists.");
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
            Musica musica;
            try {
                musica = em.getReference(Musica.class, id);
                musica.getMusicaId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The musica with id " + id + " no longer exists.", enfe);
            }
            Album albumId = musica.getAlbumId();
            if (albumId != null) {
                albumId.getMusicaList().remove(musica);
                albumId = em.merge(albumId);
            }
            Utilizador artistaId = musica.getArtistaId();
            if (artistaId != null) {
                artistaId.getMusicaList().remove(musica);
                artistaId = em.merge(artistaId);
            }
            em.remove(musica);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Musica> findMusicaEntities() {
        return findMusicaEntities(true, -1, -1);
    }

    public List<Musica> findMusicaEntities(int maxResults, int firstResult) {
        return findMusicaEntities(false, maxResults, firstResult);
    }

    private List<Musica> findMusicaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Musica.class));
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

    public Musica findMusica(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Musica.class, id);
        } finally {
            em.close();
        }
    }

    public int getMusicaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Musica> rt = cq.from(Musica.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
