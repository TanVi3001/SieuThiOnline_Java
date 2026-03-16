package model.account.kpi;

public class KpiEvaluation {
    private String employeeId;
    private String kpiId;
    private String evaluationPeriod;
    private double actualValue;
    private double achievedScore;
    private String managerNote;
    private int isDeleted;

    public KpiEvaluation() {}

    public KpiEvaluation(String employeeId, String kpiId, String evaluationPeriod, 
                         double actualValue, double achievedScore, String managerNote, int isDeleted) {
        this.employeeId = employeeId;
        this.kpiId = kpiId;
        this.evaluationPeriod = evaluationPeriod;
        this.actualValue = actualValue;
        this.achievedScore = achievedScore;
        this.managerNote = managerNote;
        this.isDeleted = isDeleted;
    }

    // Getters and Setters...

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getKpiId() {
        return kpiId;
    }

    public void setKpiId(String kpiId) {
        this.kpiId = kpiId;
    }

    public String getEvaluationPeriod() {
        return evaluationPeriod;
    }

    public void setEvaluationPeriod(String evaluationPeriod) {
        this.evaluationPeriod = evaluationPeriod;
    }

    public double getActualValue() {
        return actualValue;
    }

    public void setActualValue(double actualValue) {
        this.actualValue = actualValue;
    }

    public double getAchievedScore() {
        return achievedScore;
    }

    public void setAchievedScore(double achievedScore) {
        this.achievedScore = achievedScore;
    }

    public String getManagerNote() {
        return managerNote;
    }

    public void setManagerNote(String managerNote) {
        this.managerNote = managerNote;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}