package com.skeeterSoftworks.WorkOrderLocal.to.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeasuringFeaturePrototypeTO {

    private Long id;
    private String catalogueId;
    private String description;
    private BigDecimal refValue;
    private BigDecimal minTolerance;
    private BigDecimal maxTolerance;
    private String classType;
    private String frequency;
    private String checkType;
    private String toolType;
    private String measuringTool;
}

