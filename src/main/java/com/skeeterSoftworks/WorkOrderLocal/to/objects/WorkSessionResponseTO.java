package com.skeeterSoftworks.WorkOrderLocal.to.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
    private long controlProductCount;
    private long faultyProductCount;
    private long setupProductCount;
    private String productReferenceID;
    private String operatorQrCode;
    private String operatorName;
    private String operatorSurname;
    private String stationId;
    private boolean workOrderCompletedByTarget;

    private List<MeasuringFeaturePrototypeTO> measuringFeaturePrototypes;

    /** Raw Base64 of the product technical drawing (when present). */
    private String technicalDrawingBase64;
}
