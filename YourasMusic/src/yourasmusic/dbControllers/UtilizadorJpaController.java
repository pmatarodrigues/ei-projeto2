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
import yourasmusic.db.Editora;
import yourasmusic.db.DirEstudio;
import yourasmusic.db.Admin;
import yourasmusic.db.Artista;
import yourasmusic.db.Musica;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import yourasmusic.db.Reserva;
import yourasmusic.db.Album;
import yourasmusic.db.Utilizador;
import yourasmusic.dbControllers.exceptions.IllegalOrphanException;
import yourasmusic.dbControllers.exceptions.NonexistentEntityException;
import yourasmusic.dbControllers.exceptions.PreexistingEntityException;

/**
 *
 * @author pedro
 */
public class UtilizadorJpaController implements Serializable {

    public UtilizadorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Utilizador utilizador) throws PreexistingEntityException, Exception {
        if (utilizador.getMusicaList() == null) {
            utilizador.setMusicaList(new ArrayList<Musica>());
        }
        if (utilizador.getReservaList() == null) {
            utilizador.setReservaList(new ArrayList<Reserva>());
        }
        if (utilizador.getAlbumList() == null) {
            utilizador.setAlbumList(new ArrayList<Album>());
        }
        if (utilizador.getAlbumList1() == null) {
            utilizador.setAlbumList1(new ArrayList<Album>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Editora editora = utilizador.getEditora();
            if (editora != null) {
                editora = em.getReference(editora.getClass(), editora.getEditoraId());
                utilizador.setEditora(editora);
            }
            DirEstudio dirEstudio = utilizador.getDirEstudio();
            if (dirEstudio != null) {
                dirEstudio = em.getReference(dirEstudio.getClass(), dirEstudio.getDirEstudioId());
                utilizador.setDirEstudio(dirEstudio);
            }
            Admin admin = utilizador.getAdmin();
            if (admin != null) {
                admin = em.getReference(admin.getClass(), admin.getAdminId());
                utilizador.setAdmin(admin);
            }
            Artista artista = utilizador.getArtista();
            if (artista != null) {
                artista = em.getReference(artista.getClass(), artista.getArtistaId());
                utilizador.setArtista(artista);
            }
            List<Musica> attachedMusicaList = new ArrayList<Musica>();
            for (Musica musicaListMusicaToAttach : utilizador.getMusicaList()) {
                musicaListMusicaToAttach = em.getReference(musicaListMusicaToAttach.getClass(), musicaListMusicaToAttach.getMusicaId());
                attachedMusicaList.add(musicaListMusicaToAttach);
            }
            utilizador.setMusicaList(attachedMusicaList);
            List<Reserva> attachedReservaList = new ArrayList<Reserva>();
            for (Reserva reservaListReservaToAttach : utilizador.getReservaList()) {
                reservaListReservaToAttach = em.getReference(reservaListReservaToAttach.getClass(), reservaListReservaToAttach.getReservaId());
                attachedReservaList.add(reservaListReservaToAttach);
            }
            utilizador.setReservaList(attachedReservaList);
            List<Album> attachedAlbumList = new ArrayList<Album>();
            for (Album albumListAlbumToAttach : utilizador.getAlbumList()) {
                albumListAlbumToAttach = em.getReference(albumListAlbumToAttach.getClass(), albumListAlbumToAttach.getAlbumId());
                attachedAlbumList.add(albumListAlbumToAttach);
            }
            utilizador.setAlbumList(attachedAlbumList);
            List<Album> attachedAlbumList1 = new ArrayList<Album>();
            for (Album albumList1AlbumToAttach : utilizador.getAlbumList1()) {
                albumList1AlbumToAttach = em.getReference(albumList1AlbumToAttach.getClass(), albumList1AlbumToAttach.getAlbumId());
                attachedAlbumList1.add(albumList1AlbumToAttach);
            }
            utilizador.setAlbumList1(attachedAlbumList1);
            em.persist(utilizador);
            if (editora != null) {
                Utilizador oldUtilizadorOfEditora = editora.getUtilizador();
                if (oldUtilizadorOfEditora != null) {
                    oldUtilizadorOfEditora.setEditora(null);
                    oldUtilizadorOfEditora = em.merge(oldUtilizadorOfEditora);
                }
                editora.setUtilizador(utilizador);
                editora = em.merge(editora);
            }
            if (dirEstudio != null) {
                Utilizador oldUtilizadorOfDirEstudio = dirEstudio.getUtilizador();
                if (oldUtilizadorOfDirEstudio != null) {
                    oldUtilizadorOfDirEstudio.setDirEstudio(null);
                    oldUtilizadorOfDirEstudio = em.merge(oldUtilizadorOfDirEstudio);
                }
                dirEstudio.setUtilizador(utilizador);
                dirEstudio = em.merge(dirEstudio);
            }
            if (admin != null) {
                Utilizador oldUtilizadorOfAdmin = admin.getUtilizador();
                if (oldUtilizadorOfAdmin != null) {
                    oldUtilizadorOfAdmin.setAdmin(null);
                    oldUtilizadorOfAdmin = em.merge(oldUtilizadorOfAdmin);
                }
                admin.setUtilizador(utilizador);
                admin = em.merge(admin);
            }
            if (artista != null) {
                Utilizador oldUtilizadorOfArtista = artista.getUtilizador();
                if (oldUtilizadorOfArtista != null) {
                    oldUtilizadorOfArtista.setArtista(null);
                    oldUtilizadorOfArtista = em.merge(oldUtilizadorOfArtista);
                }
                artista.setUtilizador(utilizador);
                artista = em.merge(artista);
            }
            for (Musica musicaListMusica : utilizador.getMusicaList()) {
                Utilizador oldArtistaIdOfMusicaListMusica = musicaListMusica.getArtistaId();
                musicaListMusica.setArtistaId(utilizador);
                musicaListMusica = em.merge(musicaListMusica);
                if (oldArtistaIdOfMusicaListMusica != null) {
                    oldArtistaIdOfMusicaListMusica.getMusicaList().remove(musicaListMusica);
                    oldArtistaIdOfMusicaListMusica = em.merge(oldArtistaIdOfMusicaListMusica);
                }
            }
            for (Reserva reservaListReserva : utilizador.getReservaList()) {
                Utilizador oldRequerenteIdOfReservaListReserva = reservaListReserva.getRequerenteId();
                reservaListReserva.setRequerenteId(utilizador);
                reservaListReserva = em.merge(reservaListReserva);
                if (oldRequerenteIdOfReservaListReserva != null) {
                    oldRequerenteIdOfReservaListReserva.getReservaList().remove(reservaListReserva);
                    oldRequerenteIdOfReservaListReserva = em.merge(oldRequerenteIdOfReservaListReserva);
                }
            }
            for (Album albumListAlbum : utilizador.getAlbumList()) {
                Utilizador oldEditoraIdOfAlbumListAlbum = albumListAlbum.getEditoraId();
                albumListAlbum.setEditoraId(utilizador);
                albumListAlbum = em.merge(albumListAlbum);
                if (oldEditoraIdOfAlbumListAlbum != null) {
                    oldEditoraIdOfAlbumListAlbum.getAlbumList().remove(albumListAlbum);
                    oldEditoraIdOfAlbumListAlbum = em.merge(oldEditoraIdOfAlbumListAlbum);
                }
            }
            for (Album albumList1Album : utilizador.getAlbumList1()) {
                Utilizador oldArtistaIdOfAlbumList1Album = albumList1Album.getArtistaId();
                albumList1Album.setArtistaId(utilizador);
                albumList1Album = em.merge(albumList1Album);
                if (oldArtistaIdOfAlbumList1Album != null) {
                    oldArtistaIdOfAlbumList1Album.getAlbumList1().remove(albumList1Album);
                    oldArtistaIdOfAlbumList1Album = em.merge(oldArtistaIdOfAlbumList1Album);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUtilizador(utilizador.getUtilizadorId()) != null) {
                throw new PreexistingEntityException("Utilizador " + utilizador + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Utilizador utilizador) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Utilizador persistentUtilizador = em.find(Utilizador.class, utilizador.getUtilizadorId());
            Editora editoraOld = persistentUtilizador.getEditora();
            Editora editoraNew = utilizador.getEditora();
            DirEstudio dirEstudioOld = persistentUtilizador.getDirEstudio();
            DirEstudio dirEstudioNew = utilizador.getDirEstudio();
            Admin adminOld = persistentUtilizador.getAdmin();
            Admin adminNew = utilizador.getAdmin();
            Artista artistaOld = persistentUtilizador.getArtista();
            Artista artistaNew = utilizador.getArtista();
            List<Musica> musicaListOld = persistentUtilizador.getMusicaList();
            List<Musica> musicaListNew = utilizador.getMusicaList();
            List<Reserva> reservaListOld = persistentUtilizador.getReservaList();
            List<Reserva> reservaListNew = utilizador.getReservaList();
            List<Album> albumListOld = persistentUtilizador.getAlbumList();
            List<Album> albumListNew = utilizador.getAlbumList();
            List<Album> albumList1Old = persistentUtilizador.getAlbumList1();
            List<Album> albumList1New = utilizador.getAlbumList1();
            List<String> illegalOrphanMessages = null;
            if (editoraOld != null && !editoraOld.equals(editoraNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Editora " + editoraOld + " since its utilizador field is not nullable.");
            }
            if (dirEstudioOld != null && !dirEstudioOld.equals(dirEstudioNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain DirEstudio " + dirEstudioOld + " since its utilizador field is not nullable.");
            }
            if (adminOld != null && !adminOld.equals(adminNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Admin " + adminOld + " since its utilizador field is not nullable.");
            }
            if (artistaOld != null && !artistaOld.equals(artistaNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Artista " + artistaOld + " since its utilizador field is not nullable.");
            }
            for (Musica musicaListOldMusica : musicaListOld) {
                if (!musicaListNew.contains(musicaListOldMusica)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Musica " + musicaListOldMusica + " since its artistaId field is not nullable.");
                }
            }
            for (Reserva reservaListOldReserva : reservaListOld) {
                if (!reservaListNew.contains(reservaListOldReserva)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Reserva " + reservaListOldReserva + " since its requerenteId field is not nullable.");
                }
            }
            for (Album albumListOldAlbum : albumListOld) {
                if (!albumListNew.contains(albumListOldAlbum)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Album " + albumListOldAlbum + " since its editoraId field is not nullable.");
                }
            }
            for (Album albumList1OldAlbum : albumList1Old) {
                if (!albumList1New.contains(albumList1OldAlbum)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Album " + albumList1OldAlbum + " since its artistaId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (editoraNew != null) {
                editoraNew = em.getReference(editoraNew.getClass(), editoraNew.getEditoraId());
                utilizador.setEditora(editoraNew);
            }
            if (dirEstudioNew != null) {
                dirEstudioNew = em.getReference(dirEstudioNew.getClass(), dirEstudioNew.getDirEstudioId());
                utilizador.setDirEstudio(dirEstudioNew);
            }
            if (adminNew != null) {
                adminNew = em.getReference(adminNew.getClass(), adminNew.getAdminId());
                utilizador.setAdmin(adminNew);
            }
            if (artistaNew != null) {
                artistaNew = em.getReference(artistaNew.getClass(), artistaNew.getArtistaId());
                utilizador.setArtista(artistaNew);
            }
            List<Musica> attachedMusicaListNew = new ArrayList<Musica>();
            for (Musica musicaListNewMusicaToAttach : musicaListNew) {
                musicaListNewMusicaToAttach = em.getReference(musicaListNewMusicaToAttach.getClass(), musicaListNewMusicaToAttach.getMusicaId());
                attachedMusicaListNew.add(musicaListNewMusicaToAttach);
            }
            musicaListNew = attachedMusicaListNew;
            utilizador.setMusicaList(musicaListNew);
            List<Reserva> attachedReservaListNew = new ArrayList<Reserva>();
            for (Reserva reservaListNewReservaToAttach : reservaListNew) {
                reservaListNewReservaToAttach = em.getReference(reservaListNewReservaToAttach.getClass(), reservaListNewReservaToAttach.getReservaId());
                attachedReservaListNew.add(reservaListNewReservaToAttach);
            }
            reservaListNew = attachedReservaListNew;
            utilizador.setReservaList(reservaListNew);
            List<Album> attachedAlbumListNew = new ArrayList<Album>();
            for (Album albumListNewAlbumToAttach : albumListNew) {
                albumListNewAlbumToAttach = em.getReference(albumListNewAlbumToAttach.getClass(), albumListNewAlbumToAttach.getAlbumId());
                attachedAlbumListNew.add(albumListNewAlbumToAttach);
            }
            albumListNew = attachedAlbumListNew;
            utilizador.setAlbumList(albumListNew);
            List<Album> attachedAlbumList1New = new ArrayList<Album>();
            for (Album albumList1NewAlbumToAttach : albumList1New) {
                albumList1NewAlbumToAttach = em.getReference(albumList1NewAlbumToAttach.getClass(), albumList1NewAlbumToAttach.getAlbumId());
                attachedAlbumList1New.add(albumList1NewAlbumToAttach);
            }
            albumList1New = attachedAlbumList1New;
            utilizador.setAlbumList1(albumList1New);
            utilizador = em.merge(utilizador);
            if (editoraNew != null && !editoraNew.equals(editoraOld)) {
                Utilizador oldUtilizadorOfEditora = editoraNew.getUtilizador();
                if (oldUtilizadorOfEditora != null) {
                    oldUtilizadorOfEditora.setEditora(null);
                    oldUtilizadorOfEditora = em.merge(oldUtilizadorOfEditora);
                }
                editoraNew.setUtilizador(utilizador);
                editoraNew = em.merge(editoraNew);
            }
            if (dirEstudioNew != null && !dirEstudioNew.equals(dirEstudioOld)) {
                Utilizador oldUtilizadorOfDirEstudio = dirEstudioNew.getUtilizador();
                if (oldUtilizadorOfDirEstudio != null) {
                    oldUtilizadorOfDirEstudio.setDirEstudio(null);
                    oldUtilizadorOfDirEstudio = em.merge(oldUtilizadorOfDirEstudio);
                }
                dirEstudioNew.setUtilizador(utilizador);
                dirEstudioNew = em.merge(dirEstudioNew);
            }
            if (adminNew != null && !adminNew.equals(adminOld)) {
                Utilizador oldUtilizadorOfAdmin = adminNew.getUtilizador();
                if (oldUtilizadorOfAdmin != null) {
                    oldUtilizadorOfAdmin.setAdmin(null);
                    oldUtilizadorOfAdmin = em.merge(oldUtilizadorOfAdmin);
                }
                adminNew.setUtilizador(utilizador);
                adminNew = em.merge(adminNew);
            }
            if (artistaNew != null && !artistaNew.equals(artistaOld)) {
                Utilizador oldUtilizadorOfArtista = artistaNew.getUtilizador();
                if (oldUtilizadorOfArtista != null) {
                    oldUtilizadorOfArtista.setArtista(null);
                    oldUtilizadorOfArtista = em.merge(oldUtilizadorOfArtista);
                }
                artistaNew.setUtilizador(utilizador);
                artistaNew = em.merge(artistaNew);
            }
            for (Musica musicaListNewMusica : musicaListNew) {
                if (!musicaListOld.contains(musicaListNewMusica)) {
                    Utilizador oldArtistaIdOfMusicaListNewMusica = musicaListNewMusica.getArtistaId();
                    musicaListNewMusica.setArtistaId(utilizador);
                    musicaListNewMusica = em.merge(musicaListNewMusica);
                    if (oldArtistaIdOfMusicaListNewMusica != null && !oldArtistaIdOfMusicaListNewMusica.equals(utilizador)) {
                        oldArtistaIdOfMusicaListNewMusica.getMusicaList().remove(musicaListNewMusica);
                        oldArtistaIdOfMusicaListNewMusica = em.merge(oldArtistaIdOfMusicaListNewMusica);
                    }
                }
            }
            for (Reserva reservaListNewReserva : reservaListNew) {
                if (!reservaListOld.contains(reservaListNewReserva)) {
                    Utilizador oldRequerenteIdOfReservaListNewReserva = reservaListNewReserva.getRequerenteId();
                    reservaListNewReserva.setRequerenteId(utilizador);
                    reservaListNewReserva = em.merge(reservaListNewReserva);
                    if (oldRequerenteIdOfReservaListNewReserva != null && !oldRequerenteIdOfReservaListNewReserva.equals(utilizador)) {
                        oldRequerenteIdOfReservaListNewReserva.getReservaList().remove(reservaListNewReserva);
                        oldRequerenteIdOfReservaListNewReserva = em.merge(oldRequerenteIdOfReservaListNewReserva);
                    }
                }
            }
            for (Album albumListNewAlbum : albumListNew) {
                if (!albumListOld.contains(albumListNewAlbum)) {
                    Utilizador oldEditoraIdOfAlbumListNewAlbum = albumListNewAlbum.getEditoraId();
                    albumListNewAlbum.setEditoraId(utilizador);
                    albumListNewAlbum = em.merge(albumListNewAlbum);
                    if (oldEditoraIdOfAlbumListNewAlbum != null && !oldEditoraIdOfAlbumListNewAlbum.equals(utilizador)) {
                        oldEditoraIdOfAlbumListNewAlbum.getAlbumList().remove(albumListNewAlbum);
                        oldEditoraIdOfAlbumListNewAlbum = em.merge(oldEditoraIdOfAlbumListNewAlbum);
                    }
                }
            }
            for (Album albumList1NewAlbum : albumList1New) {
                if (!albumList1Old.contains(albumList1NewAlbum)) {
                    Utilizador oldArtistaIdOfAlbumList1NewAlbum = albumList1NewAlbum.getArtistaId();
                    albumList1NewAlbum.setArtistaId(utilizador);
                    albumList1NewAlbum = em.merge(albumList1NewAlbum);
                    if (oldArtistaIdOfAlbumList1NewAlbum != null && !oldArtistaIdOfAlbumList1NewAlbum.equals(utilizador)) {
                        oldArtistaIdOfAlbumList1NewAlbum.getAlbumList1().remove(albumList1NewAlbum);
                        oldArtistaIdOfAlbumList1NewAlbum = em.merge(oldArtistaIdOfAlbumList1NewAlbum);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = utilizador.getUtilizadorId();
                if (findUtilizador(id) == null) {
                    throw new NonexistentEntityException("The utilizador with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Utilizador utilizador;
            try {
                utilizador = em.getReference(Utilizador.class, id);
                utilizador.getUtilizadorId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The utilizador with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Editora editoraOrphanCheck = utilizador.getEditora();
            if (editoraOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Utilizador (" + utilizador + ") cannot be destroyed since the Editora " + editoraOrphanCheck + " in its editora field has a non-nullable utilizador field.");
            }
            DirEstudio dirEstudioOrphanCheck = utilizador.getDirEstudio();
            if (dirEstudioOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Utilizador (" + utilizador + ") cannot be destroyed since the DirEstudio " + dirEstudioOrphanCheck + " in its dirEstudio field has a non-nullable utilizador field.");
            }
            Admin adminOrphanCheck = utilizador.getAdmin();
            if (adminOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Utilizador (" + utilizador + ") cannot be destroyed since the Admin " + adminOrphanCheck + " in its admin field has a non-nullable utilizador field.");
            }
            Artista artistaOrphanCheck = utilizador.getArtista();
            if (artistaOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Utilizador (" + utilizador + ") cannot be destroyed since the Artista " + artistaOrphanCheck + " in its artista field has a non-nullable utilizador field.");
            }
            List<Musica> musicaListOrphanCheck = utilizador.getMusicaList();
            for (Musica musicaListOrphanCheckMusica : musicaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Utilizador (" + utilizador + ") cannot be destroyed since the Musica " + musicaListOrphanCheckMusica + " in its musicaList field has a non-nullable artistaId field.");
            }
            List<Reserva> reservaListOrphanCheck = utilizador.getReservaList();
            for (Reserva reservaListOrphanCheckReserva : reservaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Utilizador (" + utilizador + ") cannot be destroyed since the Reserva " + reservaListOrphanCheckReserva + " in its reservaList field has a non-nullable requerenteId field.");
            }
            List<Album> albumListOrphanCheck = utilizador.getAlbumList();
            for (Album albumListOrphanCheckAlbum : albumListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Utilizador (" + utilizador + ") cannot be destroyed since the Album " + albumListOrphanCheckAlbum + " in its albumList field has a non-nullable editoraId field.");
            }
            List<Album> albumList1OrphanCheck = utilizador.getAlbumList1();
            for (Album albumList1OrphanCheckAlbum : albumList1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Utilizador (" + utilizador + ") cannot be destroyed since the Album " + albumList1OrphanCheckAlbum + " in its albumList1 field has a non-nullable artistaId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(utilizador);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Utilizador> findUtilizadorEntities() {
        return findUtilizadorEntities(true, -1, -1);
    }

    public List<Utilizador> findUtilizadorEntities(int maxResults, int firstResult) {
        return findUtilizadorEntities(false, maxResults, firstResult);
    }

    private List<Utilizador> findUtilizadorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Utilizador.class));
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

    public Utilizador findUtilizador(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Utilizador.class, id);
        } finally {
            em.close();
        }
    }

    public int getUtilizadorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Utilizador> rt = cq.from(Utilizador.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
