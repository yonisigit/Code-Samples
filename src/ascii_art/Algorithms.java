package ascii_art;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Algorithms {

    public static int findDuplicate(int[] numList) {
        Set<Integer> numSet = new HashSet<Integer>();
        for (int i = 0; i < numList.length; i++) {
            if (numSet.contains(numList[i])){
                return numList[i];
            }
            numSet.add(numList[i]);
        }
        return 0;
    }

    public static int uniqueMorseRepresentations(String[] words) {
        Map<Character, String> morseCode = new HashMap<Character, String>();
        morseCode.put('a', ".-");
        morseCode.put('b', "-...");
        morseCode.put('c',  "-.-");
        morseCode.put('d',  "-..");
        morseCode.put('e',    ".");
        morseCode.put('f', "..-.");
        morseCode.put('g',  "--.");
        morseCode.put('h', "....");
        morseCode.put('i',   "..");
        morseCode.put('j', ".---");
        morseCode.put('k',   "-.");
        morseCode.put('l', ".-..");
        morseCode.put('m',   "--");
        morseCode.put('n',   "-.");
        morseCode.put('o',  "---");
        morseCode.put('p', ".--.");
        morseCode.put('q', "--.-");
        morseCode.put('r', ".-.");
        morseCode.put('s',  "...");
        morseCode.put('t',   "-");
        morseCode.put('u',  "..-");
        morseCode.put('v', "...-");
        morseCode.put('w',  ".--");
        morseCode.put('x', "-..-");
        morseCode.put('y', "-.--");
        morseCode.put('z', "--..");
        morseCode.put('1', ".----");
        morseCode.put('2',"..---");
        morseCode.put('3', "...--");
        morseCode.put('4', "....-");
        morseCode.put('5', ".....");
        morseCode.put('6', "-....");
        morseCode.put('7', "--...");
        morseCode.put('8', "---..");
        morseCode.put('9', "----.");
        morseCode.put('0', "-----");
        Set<String> morseWordsSet = new HashSet<String>();
        for (String word: words) {
            morseWordsSet.add(translate(word, morseCode));
        }
        return morseWordsSet.size();
    }

    private static String translate(String word, Map<Character, String> morseCode){
        StringBuilder morseWord = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            morseWord.append(morseCode.get(word.charAt(i)));
        }
        return morseWord.toString();
    }
}
