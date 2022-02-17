package com.doneugene.microservices.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOutputDTO implements Serializable {

    private String name;

    private String fee;

    @JsonProperty("service_code")
    private String serviceCode;

    @JsonProperty("support_variable_amount")
    private String supportVariableAmount;

    @JsonProperty("min_variable_amount")
    private String minVariableAmount;

    @JsonProperty("max_variable_amount")
    private String maxVariableAmount;

    private String currency;

    @JsonProperty("supported_branches")
    private List<String> supportedBranches = new ArrayList<>();

}
