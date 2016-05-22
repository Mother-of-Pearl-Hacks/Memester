package com.twok16.motheropearlhacks.memester;

/**
 * Created by appleowner on 5/22/16.
 * Class to make a meme by getting a random image and random phrase
 */
public class Meme {
    protected String file_name; // the meme's image filename
    protected String message; // the meme's message

    // constructor that sets the meme's image and message
    protected Meme(PhraseFinder phraseFinder, ImageFinder imageFinder) {
        file_name = imageFinder.getImage();
        message = phraseFinder.getPhrase();
    }
}
