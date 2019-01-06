package me.ssn.wsamq.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServerMessageDto {
    private MessageOrigin origin;
    private long timestamp;
    private String text;
}
