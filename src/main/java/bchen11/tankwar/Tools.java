package bchen11.tankwar;

import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.io.File;

class Tools {


    //Tool methods to get image and play audio
    static Image getImage(String imageName) {
        return new ImageIcon("assets/images/" + imageName).getImage();
    }

    private static boolean STOPPED = false;

    static synchronized void playAudio(final String fileName) {
        if (STOPPED || GameClient.getInstance().isSilent()) return;
        try {
            Media sound = new Media(new File("assets/audios/" + fileName).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        } catch (MediaException e) {
            e.printStackTrace();
            STOPPED = true;
        }
    }

}
