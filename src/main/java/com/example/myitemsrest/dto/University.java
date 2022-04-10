package com.example.myitemsrest.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class University {
    @JsonProperty("web_pages")
    private List<String> webPages;
    @JsonProperty("alpha_two_code")
    private String alphaTwoCode;
    private String country;
    private String name;
}
