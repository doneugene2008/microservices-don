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
public class ServiceAvailableDTO implements Serializable {

    private Integer status;

    private String message;

    @JsonProperty("current_page")
    private String currentPage;

    @JsonProperty("results_per_page")
    private  String resultsPerPage;

    @JsonProperty("total_results_count")
    private String totalResultsCount;

    @JsonProperty("max_page")
    private Integer maxPage;

    @JsonProperty("output")
    private List<ServiceOutputDTO> serviceOutput = new ArrayList<>();

}
