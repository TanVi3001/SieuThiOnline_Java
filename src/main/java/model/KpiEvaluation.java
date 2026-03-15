package model;

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
}