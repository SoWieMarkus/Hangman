package markus.wieland.hangman;

import android.content.Context;

import androidx.core.os.ConfigurationCompat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import markus.wieland.games.persistence.GameGenerator;
import markus.wieland.hangman.models.HangmanWord;

public class HangmanGenerator extends GameGenerator<HangmanGameState> {

    private static final List<String> WORDS = new ArrayList<>();

    private final Context context;
    private final Random random;

    public HangmanGenerator(HangmanConfiguration hangmanConfiguration, Context context) {
        super(hangmanConfiguration);
        this.context = context;
        this.random = new Random();
    }

    @Override
    public HangmanGameState generate() {
        if (WORDS.isEmpty()) {
            loadWords();
        }
        String word;
        do {
            word = WORDS.get(random.nextInt(WORDS.size()));
        } while (doesNotMatchPattern(word));

        return new HangmanGameState(new HangmanWord(word));
    }

    public static boolean doesNotMatchPattern(String word) {
        return !word.matches("[a-zA-Z]+");
    }

    private void loadWords() {
        WORDS.clear();
        Locale current = ConfigurationCompat.getLocales(context.getResources().getConfiguration()).get(0);
        String fileName = current.toString().equalsIgnoreCase("de_DE")
                ? "words_de.txt"
                : "words.txt";

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(fileName)))) {
            String word;
            while ((word = reader.readLine()) != null) {
                WORDS.add(word);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
