package com.skeeterSoftworks.WorkOrderLocal.to.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SetupDataPrototypeTO {

    private String operationID;
    private String toolID;
    private BigDecimal diameterRefValue;
    private BigDecimal diameterMaxPosTolerance;
    private BigDecimal diameterMaxNegTolerance;
    private BigDecimal heightRefValue;
    private BigDecimal heightMaxPosTolerance;
    private BigDecimal heightMaxNegTolerance;
    private Boolean attributiveHeightMeasurement;
    private Boolean attributiveDiameterMeasurement;
}
