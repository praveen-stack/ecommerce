package com.ecommerce.usermanagementservice.enums;

import lombok.Getter;

@Getter
public enum MessageText {
    INCORRECT_EMAIL_PASSWORD("Incorrect email or password.");

    private final String value;

    MessageText(String value) {
        this.value = value;
    }

    // Optional: a method to get the enum from the string value
    public static MessageText fromString(String text) {
        for (MessageText textEnum : MessageText.values()) {
            if (textEnum.value.equalsIgnoreCase(text)) {
                return textEnum;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}