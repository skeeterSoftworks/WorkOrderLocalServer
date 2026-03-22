package com.skeeterSoftworks.WorkOrderLocal.to.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Persisted as {@code {"machineName": "..."}} in the workstation JSON config file.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WorkstationMachineConfigTO {

    /** Selected machine name from central; null or blank means not configured. */
    private String machineName;
}
