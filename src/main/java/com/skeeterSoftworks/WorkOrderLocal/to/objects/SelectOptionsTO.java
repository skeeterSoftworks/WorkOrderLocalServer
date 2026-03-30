package com.skeeterSoftworks.WorkOrderLocal.to.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/** Mirrors central {@code SelectOptionsTO} for JSON from {@code /config/select-options}. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SelectOptionsTO {

    private List<String> measuringTools = new ArrayList<>();
    private List<String> deliveryTerms = new ArrayList<>();
    private List<String> rejectCauses = new ArrayList<>();
}
