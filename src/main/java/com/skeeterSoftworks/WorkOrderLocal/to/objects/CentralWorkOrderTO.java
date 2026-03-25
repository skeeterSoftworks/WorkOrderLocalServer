package com.skeeterSoftworks.WorkOrderLocal.to.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

/**
 * Subset of central work order JSON for the local production UI.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class CentralWorkOrderTO {

    private Long id;
    private Long productOrderId;
    private Long purchaseOrderId;
    private String productName;
    private String productReference;
    private Integer requiredQuantity;
    private Long producedGoodQuantity;
    private LocalDate dueDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private String comment;
}
