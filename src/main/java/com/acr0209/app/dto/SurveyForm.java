package com.acr0209.app.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class SurveyForm {

    @NotNull @Min(1) @Max(5)
    private Integer intentionQ1;
    @NotNull @Min(1) @Max(5)
    private Integer intentionQ2;
    @NotNull @Min(1) @Max(5)
    private Integer intentionQ3;

    @NotNull @Min(1) @Max(5)
    private Integer justificationQ1;
    @NotNull @Min(1) @Max(5)
    private Integer justificationQ2;
    @NotNull @Min(1) @Max(5)
    private Integer justificationQ3;

    @NotNull @Min(1) @Max(5)
    private Integer awarenessQ1;
    @NotNull @Min(1) @Max(5)
    private Integer awarenessQ2;
    @NotNull @Min(1) @Max(5)
    private Integer awarenessQ3;

    public Integer getIntentionQ1() { return intentionQ1; }
    public void setIntentionQ1(Integer intentionQ1) { this.intentionQ1 = intentionQ1; }
    public Integer getIntentionQ2() { return intentionQ2; }
    public void setIntentionQ2(Integer intentionQ2) { this.intentionQ2 = intentionQ2; }
    public Integer getIntentionQ3() { return intentionQ3; }
    public void setIntentionQ3(Integer intentionQ3) { this.intentionQ3 = intentionQ3; }
    public Integer getJustificationQ1() { return justificationQ1; }
    public void setJustificationQ1(Integer justificationQ1) { this.justificationQ1 = justificationQ1; }
    public Integer getJustificationQ2() { return justificationQ2; }
    public void setJustificationQ2(Integer justificationQ2) { this.justificationQ2 = justificationQ2; }
    public Integer getJustificationQ3() { return justificationQ3; }
    public void setJustificationQ3(Integer justificationQ3) { this.justificationQ3 = justificationQ3; }
    public Integer getAwarenessQ1() { return awarenessQ1; }
    public void setAwarenessQ1(Integer awarenessQ1) { this.awarenessQ1 = awarenessQ1; }
    public Integer getAwarenessQ2() { return awarenessQ2; }
    public void setAwarenessQ2(Integer awarenessQ2) { this.awarenessQ2 = awarenessQ2; }
    public Integer getAwarenessQ3() { return awarenessQ3; }
    public void setAwarenessQ3(Integer awarenessQ3) { this.awarenessQ3 = awarenessQ3; }
}
