package org.ifsp.scholardesktop.service;

import org.ifsp.scholardesktop.model.Activity;

import java.util.List;

public class GradeService {

    public double calculateGrade(List<Activity> activities) {
        return activities.stream()
                .mapToDouble(a -> (a.getGrade() / 10.0) * a.getActivityType().getWeight() * 100)
                .sum();
    }

    public double calculateMaxPossible(List<Activity> activities) {
        return activities.stream()
                .mapToDouble(a -> a.getActivityType().getWeight() * 100)
                .sum();
    }

}
