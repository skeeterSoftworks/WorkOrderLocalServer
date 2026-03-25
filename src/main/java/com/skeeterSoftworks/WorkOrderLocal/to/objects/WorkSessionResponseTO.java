package com.skeeterSoftworks.WorkOrderLocal.to.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkSessionResponseTO {

    private Long id;
    private Long workOrderId;
    private LocalDateTime sessionStart;
    private LocalDateTime sessionEnd;
    private long productCount;
    private String productReferenceID;
    private String operatorQrCode;
    private String operatorName;
    private String operatorSurname;
    private String stationId;
    private boolean workOrderCompletedByTarget;
}
