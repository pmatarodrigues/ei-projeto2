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
import yourasmusic.entities.Artista;
import yourasmusic.entities.Editora;
import yourasmusic.entities.Musica;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import yourasmusic.entities.Album;
import yourasmusic.entityControllers.exceptions.NonexistentEntityException;
import yourasmusic.entityControllers.exceptions.PreexistingEntityException;

/**
 *
 * @author pedro
 */
public class AlbumJpaController implements Serializable {

    public AlbumJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Album album) throws PreexistingEntityException, Exception {
        if (album.getMusicaList() == null) {
            album.setMusicaList(new ArrayList<Musica>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Artista artistaId = album.getArtistaId();
            if (artistaId != null) {
                artistaId = em.getReference(artistaId.getClass(), artistaId.getArtistaId());
                album.setArtistaId(artistaId);
            }
            Editora editoraId = album.getEditoraId();
            if (editoraId != null) {
                editoraId = em.getReference(editoraId.getClass(), editoraId.getEditoraId());
                album.setEditoraId(editoraId);
            }
            List<Musica> attachedMusicaList = new ArrayList<Musica>();
            for (Musica musicaListMusicaToAttach : album.getMusicaList()) {
                musicaListMusicaToAttach = em.getReference(musicaListMusicaToAttach.getClass(), musicaListMusicaToAttach.getMusicaId());
                attachedMusicaList.add(musicaListMusicaToAttach);
            }
            album.setMusicaList(attachedMusicaList);
            em.persist(album);
            if (artistaId != null) {
                artistaId.getAlbumList().add(album);
                artistaId = em.merge(artistaId);
            }
            if (editoraId != null) {
                editoraId.getAlbumList().add(album);
                editoraId = em.merge(editoraId);
            }
            for (Musica musicaListMusica : album.getMusicaList()) {
                Album oldAlbumIdOfMusicaListMusica = musicaListMusica.getAlbumId();
                musicaListMusica.setAlbumId(album);
                musicaListMusica = em.merge(musicaListMusica);
                if (oldAlbumIdOfMusicaListMusica != null) {
                    oldAlbumIdOfMusicaListMusica.getMusicaList().remove(musicaListMusica);
                    oldAlbumIdOfMusicaListMusica = em.merge(oldAlbumIdOfMusicaListMusica);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAlbum(album.getAlbumId()) != null) {
                throw new PreexistingEntityException("Album " + album + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Album album) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Album persistentAlbum = em.find(Album.class, album.getAlbumId());
            Artista artistaIdOld = persistentAlbum.getArtistaId();
            Artista artistaIdNew = album.getArtistaId();
            Editora editoraIdOld = persistentAlbum.getEditoraId();
            Editora editoraIdNew = album.getEditoraId();
            List<Musica> musicaListOld = persistentAlbum.getMusicaList();
            List<Musica> musicaListNew = album.getMusicaList();
            if (artistaIdNew != null) {
                artistaIdNew = em.getReference(artistaIdNew.getClass(), artistaIdNew.getArtistaId());
                album.setArtistaId(artistaIdNew);
            }
            if (editoraIdNew != null) {
                editoraIdNew = em.getReference(editoraIdNew.getClass(), editoraIdNew.getEditoraId());
                album.setEditoraId(editoraIdNew);
            }
            List<Musica> attachedMusicaListNew = new ArrayList<Musica>();
            for (Musica musicaListNewMusicaToAttach : musicaListNew) {
                musicaListNewMusicaToAttach = em.getReference(musicaListNewMusicaToAttach.getClass(), musicaListNewMusicaToAttach.getMusicaId());
                attachedMusicaListNew.add(musicaListNewMusicaToAttach);
            }
            musicaListNew = attachedMusicaListNew;
            album.setMusicaList(musicaListNew);
            album = em.merge(album);
            if (artistaIdOld != null && !artistaIdOld.equals(artistaIdNew)) {
                artistaIdOld.getAlbumList().remove(album);
                artistaIdOld = em.merge(artistaIdOld);
            }
            if (artistaIdNew != null && !artistaIdNew.equals(artistaIdOld)) {
                artistaIdNew.getAlbumList().add(album);
                artistaIdNew = em.merge(artistaIdNew);
            }
            if (editoraIdOld != null && !editoraIdOld.equals(editoraIdNew)) {
                editoraIdOld.getAlbumList().remove(album);
                editoraIdOld = em.merge(editoraIdOld);
            }
            if (editoraIdNew != null && !editoraIdNew.equals(editoraIdOld)) {
                editoraIdNew.getAlbumList().add(album);
                editoraIdNew = em.merge(editoraIdNew);
            }
            for (Musica musicaListOldMusica : musicaListOld) {
                if (!musicaListNew.contains(musicaListOldMusica)) {
                    musicaListOldMusica.setAlbumId(null);
                    musicaListOldMusica = em.merge(musicaListOldMusica);
                }
            }
            for (Musica musicaListNewMusica : musicaListNew) {
                if (!musicaListOld.contains(musicaListNewMusica)) {
                    Album oldAlbumIdOfMusicaListNewMusica = musicaListNewMusica.getAlbumId();
                    musicaListNewMusica.setAlbumId(album);
                    musicaListNewMusica = em.merge(musicaListNewMusica);
                    if (oldAlbumIdOfMusicaListNewMusica != null && !oldAlbumIdOfMusicaListNewMusica.equals(album)) {
                        oldAlbumIdOfMusicaListNewMusica.getMusicaList().remove(musicaListNewMusica);
                        oldAlbumIdOfMusicaListNewMusica = em.merge(oldAlbumIdOfMusicaListNewMusica);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = album.getAlbumId();
                if (findAlbum(id) == null) {
                    throw new NonexistentEntityException("The album with id " + id + " no longer exists.");
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
            Album album;
            try {
                album = em.getReference(Album.class, id);
                album.getAlbumId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The album with id " + id + " no longer exists.", enfe);
            }
            Artista artistaId = album.getArtistaId();
            if (artistaId != null) {
                artistaId.getAlbumList().remove(album);
                artistaId = em.merge(artistaId);
            }
            Editora editoraId = album.getEditoraId();
            if (editoraId != null) {
                editoraId.getAlbumList().remove(album);
                editoraId = em.merge(editoraId);
            }
            List<Musica> musicaList = album.getMusicaList();
            for (Musica musicaListMusica : musicaList) {
                musicaListMusica.setAlbumId(null);
                musicaListMusica = em.merge(musicaListMusica);
            }
            em.remove(album);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Album> findAlbumEntities() {
        return findAlbumEntities(true, -1, -1);
    }

    public List<Album> findAlbumEntities(int maxResults, int firstResult) {
        return findAlbumEntities(false, maxResults, firstResult);
    }

    private List<Album> findAlbumEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Album.class));
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

    public Album findAlbum(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Album.class, id);
        } finally {
            em.close();
        }
    }

    public int getAlbumCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Album> rt = cq.from(Album.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
