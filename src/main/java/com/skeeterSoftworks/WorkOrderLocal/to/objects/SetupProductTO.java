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
public class SetupProductTO {

    private Long id;
    private LocalDateTime recordedAt;
    private SetupDataPrototypeTO prototypeSnapshot;
    private String measuredHeight;
    private Boolean measuredHeightOk;
    private String measuredDiameter;
    private Boolean measuredDiameterOk;
}
