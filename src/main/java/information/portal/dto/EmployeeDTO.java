package information.portal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDTO {

    private Long id;
    private String fName;
    private String lName;
    private String phone;
    private String email;
    private double salary;

}
