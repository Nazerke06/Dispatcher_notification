package org.qazcodenarxoz.web;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FlashMessage {
    private final String type;   // "success" или "error"
    private final String text;
}