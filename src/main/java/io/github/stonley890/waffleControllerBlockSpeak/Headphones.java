package io.github.stonley890.waffleControllerBlockSpeak;

import io.github.givimad.whisperjni.WhisperFullParams;
import io.github.givimad.whisperjni.WhisperJNI;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.UserAudio;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Headphones implements AudioReceiveHandler {

    private final Map<User, ByteArrayOutputStream> audioBuffer = new HashMap<>();
    private final Map<User, LocalTime> lastVoiceUpdate = new HashMap<>();

    private final Object whisperLock = new Object();

    @Override
    public boolean canReceiveUser() {
        return true;
    }

    @Override
    public void handleUserAudio(@NotNull UserAudio userAudio) {
        byte[] audioData = userAudio.getAudioData(1.0f); // Get raw PCM data
        User user = userAudio.getUser();
        try {
            lastVoiceUpdate.put(user, LocalTime.now());
            ByteArrayOutputStream byteArrayOutputStream = audioBuffer.get(user);
            if (byteArrayOutputStream == null) byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(audioData);
            audioBuffer.put(user, byteArrayOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getBufferedAudio(User user) {
        synchronized (audioBuffer) {
            return audioBuffer.get(user).toByteArray();
        }
    }

    public void clearBuffer() {
        synchronized (audioBuffer) {
            for (User user : audioBuffer.keySet()) {
                ByteArrayOutputStream userBuffer = audioBuffer.get(user);
                byte[] currentData = userBuffer.toByteArray();

                // Calculate bytes to retain
                int bytesToRetain = (int) (48000 * 2 * 2 * 1.5); // 48kHz, stereo, 16-bit, 0.5 seconds
                int startIndex = Math.max(0, currentData.length - bytesToRetain);

                // Copy last portion to a new buffer
                byte[] retainedData = Arrays.copyOfRange(currentData, startIndex, currentData.length);

                // Replace the buffer
                userBuffer.reset();
                try {
                    userBuffer.write(retainedData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void clearTotalBuffer(User user) {
        synchronized (audioBuffer) {
            audioBuffer.get(user).reset();
        }
    }

    public void processBufferedAudio() {
        for (User user : audioBuffer.keySet()) {
            byte[] bufferedAudio = getBufferedAudio(user);

            boolean clearTotal = false;

            if (lastVoiceUpdate.get(user).isBefore(LocalTime.now().minusSeconds(2))) clearTotal = true;

            try {
                float[] samples = resampleTo16kHz(bufferedAudio, OUTPUT_FORMAT);
                String transcription = transcribe(samples);

                WaffleControllerBlockSpeak.getInstance().getLogger().info(user.getName() + ": " + transcription);

                BlockRemover.removeMentionedBlocks(user, transcription);
                KeywordActions.processKeywords(user, transcription);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (clearTotal) clearTotalBuffer(user);
                else clearBuffer();
            }

        }
    }

    private void saveAsWav(byte[] pcmData, String filename) throws IOException {
        AudioFormat format = new AudioFormat(48000, 16, 2, true, true);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pcmData);
        AudioInputStream audioInputStream = new AudioInputStream(byteArrayInputStream, format, pcmData.length);
        String pathname = WaffleControllerBlockSpeak.getInstance().getDataFolder() + "/" + filename + ".wav";
        File wavFile = new File(pathname);
        AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, wavFile);
    }

    private float[] resampleTo16kHz(byte[] pcmData, AudioFormat originalFormat) throws IOException {
        // Decode PCM to TarsosDSP format
        AudioFormat targetFormat = new AudioFormat(16000, 16, 1, true, false); // 16kHz, 16-bit, mono
        AudioInputStream inputStream = new AudioInputStream(
                new ByteArrayInputStream(pcmData),
                originalFormat,
                pcmData.length / originalFormat.getFrameSize()
        );
        AudioInputStream resampledStream = AudioSystem.getAudioInputStream(targetFormat, inputStream);

        // Convert to float array
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = resampledStream.read(buffer)) != -1) {
            byteOutput.write(buffer, 0, bytesRead);
        }
        byte[] resampledBytes = byteOutput.toByteArray();

        // Convert to float[]
        return convertBytesToFloatArray(resampledBytes);
    }

    public float[] convertBytesToFloatArray(byte[] pcmData) {
        int length = pcmData.length / 2; // 16-bit samples
        float[] floatArray = new float[length];

        for (int i = 0; i < length; i++) {
            int sample = ((pcmData[i * 2 + 1] & 0xFF) << 8) | (pcmData[i * 2] & 0xFF); // Combine bytes
            if (sample > 32767) sample -= 65536; // Handle signed 16-bit
            floatArray[i] = sample / 32768.0f; // Normalize to -1.0 to 1.0
        }

        return floatArray;
    }

    private String transcribe(float[] samples) throws IOException {
        synchronized (whisperLock) {
            WhisperJNI.loadLibrary(); // load platform binaries
            WhisperJNI.setLibraryLogger(null); // capture/disable whisper.cpp log
            var whisper = new WhisperJNI();
//        float[] samples = readJFKFileSamples();
            var ctx = whisper.init(Path.of(WaffleControllerBlockSpeak.getInstance().getDataFolder().toString(), "ggml-tiny.bin"));
            var params = new WhisperFullParams();
            int result = whisper.full(ctx, params, samples, samples.length);
            if(result != 0) {
                throw new RuntimeException("Transcription failed with code " + result);
            }
            int numSegments = whisper.fullNSegments(ctx);
            String text = null;
            try {
                text = whisper.fullGetSegmentText(ctx,0);
            } catch (IndexOutOfBoundsException e) {
                text = "[BLANK_AUDIO]";
            }
            ctx.close(); // free native memory, should be called when we don't need the context anymore.
            return text;
        }

    }

}
