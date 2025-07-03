package com.woromedia.api.task.repository;

import com.woromedia.api.task.entity.HospitalDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HospitalDepartmentRepository extends JpaRepository<HospitalDepartment, Long> {
    List<HospitalDepartment> findByHospital_Id(Long hospitalId);

    @Query("SELECT DISTINCT hd.hospital.id FROM HospitalDepartment hd WHERE hd.department.id IN :departmentIds")
    List<Long> findHospitalIdsByDepartmentIds(@Param("departmentIds") List<Long> departmentIds);
}
