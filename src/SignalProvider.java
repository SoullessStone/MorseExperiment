import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * Created by SoullessStone on 25.03.2016.
 */
public class SignalProvider {
    public static final int DELAY = 20;
    static final int SCHWELLE_ZERO = 0;
    static final int SCHWELLE_MIN = 600;
    static final int SCHWELLE_MAX = 1000;
    static MorseDecoder morseDecoder;
    static int curTime = 0;

    public static void main(String args[]) {
        morseDecoder = new MorseDecoder(DELAY);
        initFrame();
        int[] signal = concatAll(getSymbolPause(), getCharacterA(), getCharacterPause(), getCharacterB(), getCharacterPause(), getCharacterC(), getWordPause(),
                getCharacterC(), getCharacterPause(), getCharacterA(), getCharacterPause(), getCharacterB(), getWordPause(),
                getDit());
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (curTime >= signal.length)
                    System.exit(0);
                morseDecoder.postSignal(curTime, signal[curTime]);
                curTime += DELAY;
            }
        };
        Timer readSignalTimer = new Timer(3, actionListener);
        readSignalTimer.start();
    }

    private static int[] getCharacterA() {
        return concatAll(getDit(), getSymbolPause(), getDah());
    }

    private static int[] getCharacterB() {
        return concatAll(getDah(), getSymbolPause(), getDit(), getSymbolPause(), getDit(), getSymbolPause(), getDit());
    }

    private static int[] getCharacterC() {
        return concatAll(getDah(), getSymbolPause(), getDit(), getSymbolPause(), getDah(), getSymbolPause(), getDit());
    }

    private static int[] getSymbolPause() {
        return new int[1200];
    }

    private static int[] getCharacterPause() {
        return new int[3600];
    }

    private static int[] getWordPause() {
        return new int[7 * 1200];
    }

    private static int[] getDah() {
        int[] signal = new int[3600];
        for (int i = 0; i < signal.length; i++) {
            signal[i] = SCHWELLE_MIN + (int) (Math.random() * ((SCHWELLE_MAX - SCHWELLE_MIN) + 1));
        }
        return signal;
    }

    private static int[] getDit() {
        int[] signal = new int[1200];
        for (int i = 0; i < signal.length; i++) {
            signal[i] = SCHWELLE_MIN + (int) (Math.random() * ((SCHWELLE_MAX - SCHWELLE_MIN) + 1));
        }
        return signal;
    }

    private static void initFrame() {
        JFrame jFrame = new JFrame();
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setLocation(new Point(300, 300));
    }

    public static int[] concatAll(int[] first, int[]... rest) {
        int totalLength = first.length;
        for (int[] array : rest) {
            totalLength += array.length;
        }
        int[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (int[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }
}
