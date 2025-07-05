package com.hrelix.app.leave;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, UUID> {

    List<LeaveRequest> findByEmployeeId(UUID employeeId);

    List<LeaveRequest> findByStatus(LeaveStatus status);

    List<LeaveRequest> findByLeaveType(LeaveType leaveType);

    List<LeaveRequest> findTop10ByOrderByCreatedAtDesc();
}
