package com.woromedia.api.task.service;

import com.woromedia.api.task.entity.Department;
import com.woromedia.api.task.entity.HospitalDepartment;
import com.woromedia.api.task.repository.HospitalDepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HospitalDepartmentService {

    @Autowired
    private HospitalDepartmentRepository hospitalDepartmentRepository;

    public List<Department> getDepartmentsByHospitalId(Long hospitalId) {
        List<HospitalDepartment> departments = hospitalDepartmentRepository.findByHospital_Id(hospitalId);
        return departments.stream().map(HospitalDepartment::getDepartment).collect(Collectors.toList());
    }


    public Set<Long> getUniqueHospitalIdsByDepartmentIds(List<Long> departmentIds) {
        List<Long> hospitalIds = hospitalDepartmentRepository.findHospitalIdsByDepartmentIds(departmentIds);
        return new HashSet<>(hospitalIds);
    }
}
