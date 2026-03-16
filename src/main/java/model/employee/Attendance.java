package model.employee;

import java.sql.Date;
import java.sql.Timestamp;

public class Attendance {
    private String employeeId;
    private String shiftId;
    private Date workDate;
    private Timestamp checkInTime;
    private Timestamp checkOutTime;
    private double attendanceCoefficient;
    private int isDeleted;

    public Attendance() {}

    public Attendance(String employeeId, String shiftId, Date workDate, Timestamp checkInTime, 
                      Timestamp checkOutTime, double attendanceCoefficient, int isDeleted) {
        this.employeeId = employeeId;
        this.shiftId = shiftId;
        this.workDate = workDate;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.attendanceCoefficient = attendanceCoefficient;
        this.isDeleted = isDeleted;
    }

    // Getters and Setters...
}