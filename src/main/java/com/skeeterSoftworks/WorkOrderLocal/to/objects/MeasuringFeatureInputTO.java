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

    private String featureName;
    private Long width;
    private Long height;
    private Long depth;
    private Long diameter;
}
