package com.skeeterSoftworks.WorkOrderLocal.to.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/** Subset of central product technology (cycle/norm/tools). */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TechnologyTO {

    private Long id;
    private String cycleTime;
    private Integer norm100;
    private Integer piecesPerMaterial;
    private List<TechnologyToolTO> tools;
}
