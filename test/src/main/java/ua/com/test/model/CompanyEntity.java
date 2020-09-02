package ua.com.test.model;

import lombok.*;


@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(of = {"companyName", "companySymbol", "logoUrl"})
public class CompanyEntity {

    private String companyName;
    private String companySymbol;
    private String logoUrl;
}
