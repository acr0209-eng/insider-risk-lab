package com.acr0209.app.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AwarenessForm {

    @NotNull @Min(1) @Max(5)
    private Integer awarenessQ1;
    @NotNull @Min(1) @Max(5)
    private Integer awarenessQ2;
    @NotNull @Min(1) @Max(5)
    private Integer awarenessQ3;

    public Integer getAwarenessQ1() { return awarenessQ1; }
    public void setAwarenessQ1(Integer awarenessQ1) { this.awarenessQ1 = awarenessQ1; }
    public Integer getAwarenessQ2() { return awarenessQ2; }
    public void setAwarenessQ2(Integer awarenessQ2) { this.awarenessQ2 = awarenessQ2; }
    public Integer getAwarenessQ3() { return awarenessQ3; }
    public void setAwarenessQ3(Integer awarenessQ3) { this.awarenessQ3 = awarenessQ3; }
}
