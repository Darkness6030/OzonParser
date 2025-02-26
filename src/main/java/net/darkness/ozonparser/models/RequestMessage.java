package net.darkness.ozonparser.models;

import lombok.*;

@Data
@Builder
public class RequestMessage {
    private Integer id;
    private byte[] image;
}
