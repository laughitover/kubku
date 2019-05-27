package com.laughitover.kubku.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"months", "days"})
public class Duration {
    private static final long serialVersionUID = -3497505925836062890L;

    static public Duration dayOf(final Integer days) {
        Duration duration = new Duration();
        duration.days = days;
        return duration;
    }

    static public Duration monthOf(final Integer months) {
        Duration duration = new Duration();
        duration.months = months;
        return duration;
    }

    public Duration validate() {
        if (months != null && days != null) {
            if (months == 0)
                months = null;
            else if (days == 0)
                days = null;
            else
                throw new RuntimeException();
        }
        return this;
    }

    private Integer months;
    private Integer days;
}
