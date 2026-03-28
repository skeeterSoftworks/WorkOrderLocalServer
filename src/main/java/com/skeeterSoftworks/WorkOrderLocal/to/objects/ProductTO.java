package com.skeeterSoftworks.WorkOrderLocal.to.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/** Subset of central {@code ProductTO}; extra fields from central JSON are ignored. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductTO {

    private Long id;
    private String name;
    private String reference;
    private String description;
    private List<QualityInfoStepTO> qualityInfoSteps;
}
