package com.skeeterSoftworks.WorkOrderLocal.to.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkSessionSetupProductCreateTO {
    private String measuredHeight;
    private Boolean measuredHeightOk;
    private String measuredDiameter;
    private Boolean measuredDiameterOk;
}
