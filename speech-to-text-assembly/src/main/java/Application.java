import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.*;

public class Application {

    public static void main(String[] args) throws Exception {

        String modelPath = "C:\\Users\\Patel-Ji\\vosk-model-small-en-us-0.15"; // Replace with your model path
        Model model = new Model(modelPath); // Load the model
        Recognizer recognizer = new Recognizer(model, 16000); // 16kHz sample rate

        // Set up the microphone for real-time audio capture
        AudioFormat format = new AudioFormat(16000, 16, 1, true, false); // 16-bit, mono, 16kHz sample rate
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)) {
            throw new LineUnavailableException("Audio line not supported");
        }

        TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
        line.open(format);
        line.start();

        byte[] buffer = new byte[4096]; // Buffer to hold audio data

        System.out.println("Start speaking...");

        // Capture and transcribe audio in real-time
        while (true) {
            int bytesRead = line.read(buffer, 0, buffer.length); // Read audio data

            if (bytesRead > 0) {
                // Send audio data to the recognizer
                if (recognizer.acceptWaveForm(buffer, bytesRead)) {
                    // If the recognizer has processed the complete waveform
                    System.out.println("Final Result: " + recognizer.getResult());
                } else {
                    // If partial results are available (incomplete transcription)
                    System.out.println("Partial Result: " + recognizer.getPartialResult());
                }
            }
        }
    }
}
