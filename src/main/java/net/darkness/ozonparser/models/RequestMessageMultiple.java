package net.darkness.ozonparser.models;

import lombok.*;

import java.util.List;

@Data
@Builder
public class RequestMessageMultiple {
    private Integer orderId;
    private List<byte[]> filesBytes;
}
