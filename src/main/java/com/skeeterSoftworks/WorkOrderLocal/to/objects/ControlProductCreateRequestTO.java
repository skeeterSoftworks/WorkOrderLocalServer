package com.skeeterSoftworks.WorkOrderLocal.to.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ControlProductCreateRequestTO {

    private List<MeasuringFeatureInputTO> measuringFeatures = new ArrayList<>();
}
