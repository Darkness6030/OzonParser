package net.darkness.ozonparser.models;

import lombok.*;

import java.util.List;

@Data
@Builder
public class ResponseMessageMultiple {
    private Integer id;
    private Float finalPrice;
    private List<Float> prices;
}
