package information.portal.repository;

import information.portal.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e WHERE " +
            "(:filter IS NULL OR LOWER(e.fName) LIKE %:filter% OR LOWER(e.lName) LIKE %:filter% OR " +
            "LOWER(e.phone) LIKE %:filter% OR LOWER(e.email) LIKE %:filter% OR e.salary = :filter)")
    Page<Employee> searchEmployees(@Param("filter") String filter, Pageable pageable);

}
