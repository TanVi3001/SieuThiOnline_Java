/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.employee;

/**
 *
 * @author nguye
 */
import java.sql.Date;
import java.math.BigDecimal;

public class Employee {

    private String employeeId;
    private String employeeName;
    private Date hireDate;
    private BigDecimal salaryCoefficient;
    private int totalCompletedOrders;
    private String roleId;
    private String shiftId;
    private int isDeleted;

    public Employee() {
    }

    public Employee(String employeeId, String employeeName, Date hireDate,
                    BigDecimal salaryCoefficient, int totalCompletedOrders,
                    String roleId, String shiftId, int isDeleted) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.hireDate = hireDate;
        this.salaryCoefficient = salaryCoefficient;
        this.totalCompletedOrders = totalCompletedOrders;
        this.roleId = roleId;
        this.shiftId = shiftId;
        this.isDeleted = isDeleted;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public BigDecimal getSalaryCoefficient() {
        return salaryCoefficient;
    }

    public void setSalaryCoefficient(BigDecimal salaryCoefficient) {
        this.salaryCoefficient = salaryCoefficient;
    }

    public int getTotalCompletedOrders() {
        return totalCompletedOrders;
    }

    public void setTotalCompletedOrders(int totalCompletedOrders) {
        this.totalCompletedOrders = totalCompletedOrders;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}