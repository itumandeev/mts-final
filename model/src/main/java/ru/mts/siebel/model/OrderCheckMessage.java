package ru.mts.siebel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCheckMessage {

    private String id;
    private String productCode;
    private Integer quantity;
    private String status;

}
