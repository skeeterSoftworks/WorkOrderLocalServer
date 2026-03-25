package com.skeeterSoftworks.WorkOrderLocal.to.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkSessionOpenRequestTO {

    private Long workOrderId;
    private String operatorQrCode;
    private String operatorName;
    private String operatorSurname;
    private String stationId;
}
