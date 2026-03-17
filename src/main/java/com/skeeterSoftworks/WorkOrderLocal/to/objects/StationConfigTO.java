package com.skeeterSoftworks.WorkOrderLocal.to.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class StationConfigTO {


    private String machineName;
    private String woPreconditionsJSON;
}
