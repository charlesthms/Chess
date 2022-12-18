package utils;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Loader {

    public static final String B_KING = "pieces/black/king.png";
    public static final String B_TOWER = "pieces/black/rook.png";
    public static final String B_BISHOP = "pieces/black/bishop.png";
    public static final String B_QUEEN = "pieces/black/queen.png";
    public static final String B_KNIGHT = "pieces/black/knight.png";
    public static final String B_PAWN = "pieces/black/pawn.png";

    public static final String W_KING = "pieces/white/king.png";
    public static final String W_TOWER = "pieces/white/rook.png";
    public static final String W_BISHOP = "pieces/white/bishop.png";
    public static final String W_QUEEN = "pieces/white/queen.png";
    public static final String W_KNIGHT = "pieces/white/knight.png";
    public static final String W_PAWN = "pieces/white/pawn.png";

    public static final String TAKE = "sounds/take.wav";
    public static final String MOVE = "sounds/move.wav";
    public static final String CASTLE = "sounds/castle.wav";

    public static BufferedImage getImage(String path) {
        BufferedImage img = null;

        try (InputStream is = Loader.class.getResourceAsStream("/" + path)){
            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return img;
    }

    public static Clip getSound(String path) {
        Clip clip = null;

        System.out.println(path);

        try (AudioInputStream ais = AudioSystem.getAudioInputStream(Loader.class.getResourceAsStream("/" + path))) {
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e ) {
            e.printStackTrace();
        }


        return clip;
    }
}
