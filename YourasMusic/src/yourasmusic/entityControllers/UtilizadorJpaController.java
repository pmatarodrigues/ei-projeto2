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
import yourasmusic.entities.Editora;
import yourasmusic.entities.DirEstudio;
import yourasmusic.entities.Artista;
import yourasmusic.entities.Musica;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import yourasmusic.entities.Reserva;
import yourasmusic.entities.Utilizador;
import yourasmusic.entityControllers.exceptions.IllegalOrphanException;
import yourasmusic.entityControllers.exceptions.NonexistentEntityException;
import yourasmusic.entityControllers.exceptions.PreexistingEntityException;

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
            Artista artistaOld = persistentUtilizador.getArtista();
            Artista artistaNew = utilizador.getArtista();
            List<Musica> musicaListOld = persistentUtilizador.getMusicaList();
            List<Musica> musicaListNew = utilizador.getMusicaList();
            List<Reserva> reservaListOld = persistentUtilizador.getReservaList();
            List<Reserva> reservaListNew = utilizador.getReservaList();
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
            for (Reserva reservaListOldReserva : reservaListOld) {
                if (!reservaListNew.contains(reservaListOldReserva)) {
                    reservaListOldReserva.setRequerenteId(null);
                    reservaListOldReserva = em.merge(reservaListOldReserva);
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
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Reserva> reservaList = utilizador.getReservaList();
            for (Reserva reservaListReserva : reservaList) {
                reservaListReserva.setRequerenteId(null);
                reservaListReserva = em.merge(reservaListReserva);
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
