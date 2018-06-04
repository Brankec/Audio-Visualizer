import javafx.scene.chart.XYChart;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.scene.text.Text;
import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import javafx.scene.media.AudioSpectrumListener;

public class Controller {
    Globals global;
    int PlayedTime = 0;

    MediaPlayer player;
    Media audio;
    File file;

    public Text SelectedText, PlayingText, PlaytimeText;
    final FileChooser fileChooser = new FileChooser();

    public void OpenMediaAction() {
        configureFileChooser(fileChooser);
        file = fileChooser.showOpenDialog(global.primaryStage);

        audio = new Media(file.toURI().toASCIIString());
        player = new MediaPlayer(audio);
        SelectedText.setText("Selected: " + file.getName());

    }

    public void PlayMediaAction() {
        if(player != null && file != null) {
            player.play();
            PlayingText.setText("Playing: " + file.getName());

            player.setAudioSpectrumListener(new AudioSpectrumListener() {
                @Override
                public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
                    PlayedTime = 0;
                    PlayedTime += timestamp;
                    PlaytimeText.setText("Playtime: " + PlayedTime + "s");
                    float[] correctedMagnitude = new float[magnitudes.length];

                    for (int i = 0; i < magnitudes.length; i++) {
                        correctedMagnitude[i] = magnitudes[i] - player.getAudioSpectrumThreshold();
                    }
                }
            });
        }
    }

    public void PauseMediaAction() {
        if(player != null && file != null) {
            player.pause();
            PlayingText.setText("Paused: " + file.getName());
        }
    }

    public void StopMediaAction() {
        if(player != null && file != null) {
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

}
