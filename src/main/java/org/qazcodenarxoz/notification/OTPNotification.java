package org.qazcodenarxoz.notification;

import java.util.UUID;

public class OTPNotification extends Notification {

    private final String code;

    public OTPNotification(Long id, String channel, String to, String text, String code) {
        super(id, channel, to, text);
        this.code = code;
    }

    public String getCode() { return code; }
}
