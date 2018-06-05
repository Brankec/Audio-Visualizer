import javafx.scene.canvas.Canvas;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.scene.text.Text;
import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import javafx.scene.media.AudioSpectrumListener;

public class Controller {
    private int PlayedTime = 0;

    private MediaPlayer player;
    private Media audio;
    private File file;

    //private Rectangle[] rect;
    private Rectangle[] rect;
    private Rectangle irectangle;
    private int band;

    public Text SelectedText, PlayingText, PlaytimeText;
    public Canvas AudioVisualizerCanvas;
    private final FileChooser fileChooser = new FileChooser();

    private int countRect = 0;

    public Controller() {
    }

    public void OpenMediaAction() {
        configureFileChooser(fileChooser);
        file = fileChooser.showOpenDialog(Globals.primaryStage);

        if (file.toURI() != null) {
            audio = new Media(file.toURI().toASCIIString());
        }
        player = new MediaPlayer(audio);
        band = 128;
        rect = new Rectangle[band];
        irectangle = new Rectangle();

        SelectedText.setText("Selected: " + file.getName());
    }

    public void PlayMediaAction() {
        if (player != null && file != null) {
            player.play();
            PlayingText.setText("Playing: " + file.getName());

            player.setAudioSpectrumListener(new AudioSpectrumListener() {
                @Override
                public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
                    PlayedTime = 0;
                    PlayedTime += timestamp;
                    float[] correctedMagnitude = new float[band];

                    for (int i = 0; i < band; i++) {
                        correctedMagnitude[i] = magnitudes[i] - player.getAudioSpectrumThreshold();

                        initRect();
                        drawRect(i,(i * 8), (int) (1080 / 2.5), 7, (int) (correctedMagnitude[i] * 7));
                    }

                    PlaytimeText.setText("Playtime: " + PlayedTime + "s");
                }
            });
        }
    }

    public void PauseMediaAction() {
        if (player != null && file != null) {
            player.pause();
            PlayingText.setText("Paused: " + file.getName());
        }
    }

    public void StopMediaAction() {
        if (player != null && file != null) {
            player.stop();
            PlayingText.setText("Playing: nothing");

            //doesnt work for now
            PlayedTime = 0;
            PlaytimeText.setText("Playtime: " + PlayedTime + "s");
        }
    }

    private static void configureFileChooser(
            final FileChooser fileChooser) {
        fileChooser.setTitle("View songs");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP3", "*.mp3"),
                new FileChooser.ExtensionFilter("WAV", "*.wav")
        );
    }

    private void drawRect(int i, int x, int y, int w, int h) {
        float amplitude = h;

        DecimalFormat df = new DecimalFormat("#.00");

        double bandColor = Double.parseDouble(df.format(i * 0.00782));

        rect[i].setFill(Color.color(1, bandColor, 0));
        rect[i].setX(x);
        rect[i].setWidth(w);
        rect[i].setHeight(amplitude);
        rect[i].setY(y - rect[i].getHeight());
    }

    private void initRect() {
        if(countRect < band) {
            rect[countRect] = new Rectangle();
            Globals.root.getChildren().add(rect[countRect]);
            countRect++;
        }
    }
}
