package com.skeeterSoftworks.WorkOrderLocal.to.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Tool row nested under central {@code TechnologyTO} (catalogue tools for a product).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TechnologyToolTO {

    private Long id;
    private String toolName;
    private String toolDescription;
    private Integer orderNumber;
    private Integer workingTime;
    private Long technologyId;
}
