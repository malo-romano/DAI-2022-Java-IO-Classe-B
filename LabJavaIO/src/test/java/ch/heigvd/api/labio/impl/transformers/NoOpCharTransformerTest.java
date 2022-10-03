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
public class NoOpCharTransformerTest {

    private String transform(String input) {
        NoOpCharTransformer transformer = new NoOpCharTransformer();
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            String s = Character.toString(input.charAt(i));
            output.append(transformer.transform(s));
        }
        return output.toString();
    }

    @Test
    public void itShouldNotTransformCharacters() {
        String source = "abcdefgABCDEFG 12345 !?'.\r\nAnother Line...";
        assertEquals(source, transform(source));
    }
}
