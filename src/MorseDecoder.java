import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SoullessStone on 25.03.2016.
 */
public class MorseDecoder {
    final Map<String, Character> charMap = new HashMap();
    String curChar = "";
    String curWord = "";


    public static final int ALLOWED_OFFSET = 100;
    final String PREFIX = "--------------------------------";
    final int DELAY;
    final int DIT_LENGHT = 1200; // SYMBOL_PAUSE
    final int DAH_LENGHT = 3 * DIT_LENGHT; // CHARACTER_PAUSE
    final int WORD_PAUSE = 7 * DIT_LENGHT;
    boolean highPeriod = false;
    int highcount = 0;
    int lowcount = 0;


    public MorseDecoder(int delay) {
        this.DELAY = delay;
        charMap.put("21", 'A');
        charMap.put("1222", 'B');
        charMap.put("1212", 'C');
        charMap.put("122", 'D');
        charMap.put("2", 'E');
        charMap.put("2212", 'F');
        charMap.put("112", 'G');
        charMap.put("2222", 'H');
        charMap.put("22", 'I');
        charMap.put("2111", 'J');
        charMap.put("121", 'K');
        charMap.put("2122", 'L');
        charMap.put("11", 'M');
        charMap.put("12", 'N');
        charMap.put("111", 'O');
        charMap.put("2112", 'P');
        charMap.put("1121", 'Q');
        charMap.put("212", 'R');
        charMap.put("222", 'S');
        charMap.put("1", 'T');
        charMap.put("221", 'U');
        charMap.put("2221", 'V');
        charMap.put("211", 'W');
        charMap.put("1221", 'X');
        charMap.put("1211", 'Y');
        charMap.put("1122", 'Z');
    }

    public void postSignal(int curTime, int sample) {
        // System.out.println(curTime + " -> " + sample);

        if (sample >= 600) {
            if (!highPeriod) {
                // Was low, now high
                resolveState(analyseHistory());
                lowcount = 0;
            }
            highcount++;
            highPeriod = true;
        } else {
            if (highPeriod) {
                // Was high, now low
                resolveState(analyseHistory());
                highcount = 0;
            }
            lowcount++;
            highPeriod = false;
        }
    }

    private void resolveState(int i) {
        switch (i){
            case 0:
                curWord += charMap.get(curChar);
                System.out.println(PREFIX + "            WORD_PAUSE: " + curWord);
                curWord = "";
                curChar = "";
                break;
            case 1:
                System.out.println(PREFIX + "DAH");
                curChar+=i;
                break;
            case 2:
                System.out.println(PREFIX + "DIT");
                curChar+=i;
                break;
            case 3:
                System.out.println(PREFIX + "      CHARACTER_PAUSE: " + charMap.get(curChar));
                curWord += charMap.get(curChar);
                curChar = "";
                break;
            case 4:
                System.out.println(PREFIX + "SYMBOL_PAUSE");
                break;
            case 9:
                System.out.println(PREFIX + "OTHER");
                break;
            default:
                System.out.println(PREFIX + i);
        }
    }
    private int analyseHistory() {
        int result = 9;
        int lenght = DELAY * lowcount;
        if (highPeriod){
            lenght = DELAY * highcount;
        }

        // System.out.println(PREFIX + lenght);
        if (!highPeriod && isInRange(lenght, WORD_PAUSE)) {
            return 0;
        }else if (isInRange(lenght, DAH_LENGHT)){
            result = 1;
        }else if (isInRange(lenght, DIT_LENGHT)) {
            result = 2;
        }else{
            return result;
        }
        if (!highPeriod)
            result+=2;
        return result;
    }

    private boolean isInRange(int real, int target) {
        if (real > 0 && real > target - ALLOWED_OFFSET && real < target + ALLOWED_OFFSET)
            return true;
        return false;
    }
}
