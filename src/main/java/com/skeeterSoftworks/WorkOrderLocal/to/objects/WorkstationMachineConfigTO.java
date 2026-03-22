package com.skeeterSoftworks.WorkOrderLocal.to.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Persisted in {@code workstation-machine.json}, e.g.
 * {@code {"machineName":"Line 1","machineId":3}}.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WorkstationMachineConfigTO {

    /** Selected machine name from central; null or blank means not configured. */
    private String machineName;

    /** Optional stable id from central; used for work-order lookup. */
    private Long machineId;
}
