package com.example.madcompetition.backend.utils;

public class MessageUtils {

    private static final int NUM_OF_PREVIEW_CHARACTERS = 15;
    public static String  formatMessageToPreviewStyle(String message)
    {
       return formatMessageToPreviewStyle(message,NUM_OF_PREVIEW_CHARACTERS); // default number chaining methods for effeciency

    }

    public static String  formatMessageToPreviewStyle(String message, int num)
    {
        if (message.length() <= num)
        {
            return message;

        }
        else
        {
            String formatedMessage = "";
            formatedMessage = message.substring(0, num);
            formatedMessage.concat("...");
            return formatedMessage;
        }

    }

}
