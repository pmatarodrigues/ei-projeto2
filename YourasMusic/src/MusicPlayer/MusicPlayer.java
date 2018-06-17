/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package MusicPlayer;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;

/**
 *
 * @author Benjamim Oliveira
 * IPVC-ESTG 2017/2018
 */
public class MusicPlayer {
    
    public Blob blb;
    private InputStream IS;
    private BufferedInputStream BIS;
    
    public Player player;
    
    public boolean playing;
    
    public long pauseLocation;    
    public long songTotalLength;
    public InputStream fileDB;
    
    
    public void Play() throws JavaLayerException, IOException, SQLException
    {
        
        IS = blb.getBinaryStream();
        playing = true;
        fileDB = IS;
        BIS = new BufferedInputStream(IS);
        player = new Player(BIS);
        songTotalLength = IS.available();
        
        new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    player.play();
                }
                catch(JavaLayerException ex)
                {
                    System.out.println("Erro na reproduçao, thread: " +ex.getMessage());
                }
            }
        }.start();
    }
    
    public void Pause()
    {
        if(player != null)                                                      //Se o player tiver realmente a tocar para a musica
        {  
           try
           {
               playing = false;
               pauseLocation = IS.available();
               player.close();
               player = null;
           }
           catch(IOException ex)
           {
               System.out.println("Erro no pause: " + ex.getMessage());
           }
        }
    }
    
     public void Resume() throws JavaLayerException{
       try 
       {
            
            
           try {
               IS = blb.getBinaryStream();
           } catch (SQLException ex) {
               Logger.getLogger(MusicPlayer.class.getName()).log(Level.SEVERE, null, ex);
           }
            
            BIS = new BufferedInputStream(IS);
            
            player = new Player(BIS);
            long a = IS.skip(pauseLocation - songTotalLength);
            System.out.println(songTotalLength);
            System.out.println(pauseLocation);
            System.out.println(a);
            System.out.println("---------");
          
        } 
        catch (FileNotFoundException | JavaLayerException ex) 
        {
            System.out.println("Erro na re-reprodução da musica: " + ex.getMessage());            
        } 
        catch (IOException ex) 
        {
            System.out.println("Erro na re-reprodução da musica (IO): " + ex.getMessage());   
        }
        playing = true;
        new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    player.play();
                    System.out.println("passou");
                }
                catch(JavaLayerException ex)
                {
                    System.out.println("Erro na reproduçao, thread: " +ex.getMessage());
                }
            }
        }.start();
     }
}
