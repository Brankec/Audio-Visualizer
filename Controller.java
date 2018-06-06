import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
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

    private int band;

    public Text SelectedText, PlayingText, PlaytimeText;
    public Canvas canvas;
    public Slider sliderAmplitude, sliderSize;
    private GraphicsContext gc;
    private final FileChooser fileChooser = new FileChooser();

    private int list = 0;

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
        initRect();

        SelectedText.setText("Selected: " + file.getName());
    }

    public void initialize() {

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

                    gc.clearRect(0,0,1920/2.5, 1080/2.5);

                    for (int i = 0; i < band; i++) {
                        correctedMagnitude[i] = magnitudes[i] - player.getAudioSpectrumThreshold();

                        //drawInHeight(i,(i * 8), (int) (1080 / 2.5), 7, (int) (correctedMagnitude[i] * 7));
                        //drawInPosition(i,(i * 8), (int) (1080 / 2.5), 7, (int) (correctedMagnitude[i] * 7));
                        list(i,(i * 8), (int) (1080 / 2.5), 7, (int) (correctedMagnitude[i] * 7));
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
                new FileChooser.ExtensionFilter("WAV", "*.wav") //add formats you'd like to play
        );
    }

    private void initRect() {
        gc = canvas.getGraphicsContext2D();
        Globals.root.getChildren().add(canvas);
    }

    public void NextButtonAction() {
        if(list < 2) { //the number 2 is the number of different functions for displaying
            list++;
        }
    }
    public void PreviousButtonAction() {
        if(list > 0) {
            list--;
        }
    }

    private void list(int i, int x, int y, int w, int h) {
        switch(list) {
            case 0: drawInHeight(i, x, y, w, h);
            break;
            case 1: drawInPosition(i, x, y, w, h);
            break;
        }
    }






























    private void drawInHeight(int i, int x, int y, int w, double h) {
        if(sliderSize.getValue() > 0)
            h /= sliderSize.getValue()/10;

        double amplitude;
        if(sliderAmplitude.getValue() > 0) {
            amplitude = ((y - 145) - h * 0.1 * sliderAmplitude.getValue()/10);
        } else {
            amplitude = ((y - 145) - h * 0.1);
        }

        DecimalFormat df = new DecimalFormat("#.00");

        double bandColor = Double.parseDouble(df.format(i * 0.00782)); //This is a decimal I used so that when I multiply it with 128 max value is 1.00(I cut the rest)

        gc.setFill(Color.color(1, bandColor, 0));
        gc.fillRect(x, amplitude, w, h);
    }


    private void drawInPosition(int i, int x, int y, int w, int h) {
        double amplitude;
        if(sliderAmplitude.getValue() > 1) {
            amplitude = (200) - h * 0.1 * sliderAmplitude.getValue()/10;
        } else {
            amplitude = 200 - h * 0.1;
        }
        DecimalFormat df = new DecimalFormat("#.00");

        double bandColor = Double.parseDouble(df.format(i * 0.00782)); //This is a decimal I used so that when I multiply it with 128 max value is 1.00(I cut the rest)

        gc.setFill(Color.color(1, bandColor, 0));

        if(sliderSize.getValue() > 0) {
            gc.fillRect(x, amplitude + i*0.1, w, 80 / sliderSize.getValue()*10);
        } else {
            gc.fillRect(x, amplitude + i*0.1, w, 80);
        }
    }

}
