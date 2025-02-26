package net.darkness.ozonparser.models;

import lombok.*;

@Data
@Builder
public class ResponseMessage {
    private Integer id;
    private String parsedImage;
}
