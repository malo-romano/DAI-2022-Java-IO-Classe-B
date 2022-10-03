package ch.heigvd.api.labio.impl.transformers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * *** IMPORTANT WARNING : DO NOT EDIT THIS FILE ***
 * <p>
 * This file is used to specify what you have to implement. To check your work,
 * we will run our own copy of the automated tests. If you change this file,
 * then you will introduce a change of specification!!!
 *
 * @author Juergen Ehrensberger
 */
public class CombinedCharTransformerTest {

    private String transform(String input) {
        UpperCaseCharTransformer transformer1 = new UpperCaseCharTransformer();
        LineNumberingCharTransformer transformer2 = new LineNumberingCharTransformer();
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            String s = Character.toString(input.charAt(i));
            output.append(transformer2.transform(transformer1.transform(s)));
        }
        return output.toString();
    }

    @Test
    public void itShouldTransformCharacters() {
        String source = "abcdefgABCDEFG\t12345 !?'.\r\nAnother Line...\nThird Line.\r\n";
        String target = "1. ABCDEFGABCDEFG\t12345 !?'.\n2. ANOTHER LINE...\n3. THIRD LINE.\n4. ";
        assertEquals(target, transform(source));
    }
}
