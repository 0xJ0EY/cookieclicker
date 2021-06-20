package com.joeyderuiter.cookieclicker.services;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joeyderuiter.cookieclicker.models.NetworkMessageFactory;

import java.util.Base64;

public class NetworkMessageService {
    private static final String TAG = "NetworkMessageService";

    public static String encodeMessage(Object message) {
        String formattedMessage = formatMessage(message);
        return new String(encode(formattedMessage));
    }

    public static String decodeMessage(String message) {
        byte[] decoded = decode(message);
        return new String(decoded);
    }

    public static String formatMessage(Object message) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String objectType = message.getClass().getSimpleName();
            String payload = objectMapper.writeValueAsString(message);

            return String.format("%s:%s", objectType, payload);

        } catch (JsonProcessingException ex) {
            Log.e(TAG, "Invalid JSON");
            return "";
        }
    }

    private static byte[] encode(String encodedMessage) {
        return Base64.getEncoder()
                .encode(encodedMessage.getBytes());
    }

    public static byte[] decode(String encodedMessage) {
        return Base64.getDecoder()
                .decode(encodedMessage);
    }

    public static Class<?> getMessageType(String message) {
        int index = message.indexOf(':');
        String key = message.substring(0, index);

        return NetworkMessageFactory.fromString(key);
    }

    public static Object getMessageData(String message, Class<?> type) {
        int index = message.indexOf(':');
        String payload = message.substring(index + 1);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(payload, type);

        } catch (JsonProcessingException ex) {
            Log.e(TAG, "Invalid JSON");
            Log.e(TAG, ex.getMessage());

            throw new RuntimeException("Invalid JSON");
        }
    }

}
