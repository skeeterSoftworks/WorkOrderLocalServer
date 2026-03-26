package com.skeeterSoftworks.WorkOrderLocal.to.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeasuringFeatureInputTO {

    /** Links the submitted assessment to a specific measuring feature prototype (by catalogue id). */
    private String catalogueId;

    /** Digits-only for MEASURED features; persisted into {@code MeasuringFeature.assessedValue}. */
    private String assessedValue;

    /** Used for ATTRIBUTIVE features; persisted into {@code MeasuringFeature.assessedValueGood}. */
    private boolean assessedValueGood;
}
