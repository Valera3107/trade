package ua.com.test.controller.dto;

import lombok.*;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {
    private String name;

    private String symbol;

    private String url;
}
