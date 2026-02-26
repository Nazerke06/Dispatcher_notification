package org.qazcodenarxoz.web;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FlashMessage {
    private final String type;
    private final String text;
}