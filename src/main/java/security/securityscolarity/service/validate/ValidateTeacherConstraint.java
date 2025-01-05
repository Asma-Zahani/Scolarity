package security.securityscolarity.service.validate;

import security.securityscolarity.entity.*;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ValidateTeacherConstraint {

    public static boolean isTeacherAvailable(Teacher teacher, List<Schedule> schedules, ChronoDay chronoDay) {
        TeacherConstraint constraint = teacher.getConstraint();

        if (constraint != null) {
            boolean validateConstraints = validateConstraints(constraint, schedules, chronoDay);
            if (!validateConstraints) {
                return false;
            }
        }

        return schedules.stream().noneMatch(schedule ->
                schedule.getTeacher().equals(teacher) &&
                        schedule.getId().getDay().equals(chronoDay.getId().getDay()) &&
                        schedule.getId().getChrono().equals(chronoDay.getId().getChrono())
        );
    }

    public static boolean validateConstraints(TeacherConstraint constraint, List<Schedule> schedules, ChronoDay chronoDay) {
        if (constraint.getUnavailableDays().contains(chronoDay)) {
            return false;
        }

        long totalDaysScheduled = schedules.stream()
                .map(schedule -> schedule.getId().getDay())
                .distinct()
                .count();
        if (totalDaysScheduled > constraint.getMaxDaysPerWeek()) {
            return false;
        }

        if (totalDaysScheduled < constraint.getMinDaysPerWeek()) {
            return false;
        }

        if (calculateConsecutiveDays(schedules) > constraint.getMaxConsecutiveDays()) {
            return false;
        }

        if (calculateDailyGaps(schedules, chronoDay) > constraint.getMaxDailyGaps()) {
            return false;
        }

        if (calculateWeeklyGaps(schedules) > constraint.getMaxWeeklyGaps()) {
            return false;
        }

        if (calculateDailyHours(schedules, chronoDay) > constraint.getMaxDailyHours()) {
            return false;
        }

        if (calculateDailyHours(schedules, chronoDay) < constraint.getMinDailyHours()) {
            return false;
        }

        if (calculateRestHours(schedules) < constraint.getMinRestHours()) {
            return false;
        }

        if (calculateDailyAmplitude(schedules, chronoDay) > constraint.getMaxDailyAmplitude()) {
            return false;
        }


        return calculateContinuousHours(schedules) <= constraint.getMaxContinuousHours();
    }

    private static long calculateConsecutiveDays(List<Schedule> schedules) {
        return schedules.stream()
                .map(schedule -> schedule.getId().getDay())
                .distinct()
                .sorted()
                .toList()
                .stream()
                .mapToInt(Day::getDayNumber)
                .reduce(0, (count, currentDay) -> {
                    if (currentDay == count + 1) {
                        return count + 1;
                    }
                    return count;
                });
    }

    private static long calculateDailyGaps(List<Schedule> schedules, ChronoDay chronoDay) {
        List<Integer> hours = schedules.stream()
                .filter(schedule -> schedule.getId().getDay().equals(chronoDay.getId().getDay()))
                .map(schedule -> schedule.getId().getChrono().getStartTime().getHour())
                .distinct()
                .sorted()
                .toList();

        long gaps = 0;
        for (int i = 1; i < hours.size(); i++) {
            gaps += hours.get(i) - hours.get(i - 1) - 1;
        }
        System.out.println(gaps);
        return gaps;
    }

    private static long calculateWeeklyGaps(List<Schedule> schedules) {
        List<Integer> weekDays = schedules.stream()
                .map(schedule -> schedule.getId().getDay().getDayNumber()) // Supposons que tu as dayNumber pour identifier les jours de la semaine
                .distinct()
                .sorted()
                .toList();

        long gaps = 0;
        for (int i = 1; i < weekDays.size(); i++) {
            gaps += weekDays.get(i) - weekDays.get(i - 1) - 1; // Gap entre les jours distincts
        }

        return gaps;
    }

    private static long calculateContinuousHours(List<Schedule> schedules) {
        List<Integer> hours = schedules.stream()
                .map(schedule -> schedule.getId().getChrono().getStartTime().getHour())
                .sorted()
                .toList();

        long continuousHours = 0;
        for (int i = 1; i < hours.size(); i++) {
            continuousHours = Math.max(continuousHours, hours.get(i) - hours.get(i - 1));
        }

        return continuousHours;
    }

    private static long calculateDailyHours(List<Schedule> schedules, ChronoDay chronoDay) {
        List<Integer> hours = schedules.stream()
                .filter(schedule -> schedule.getId().getDay().equals(chronoDay.getId().getDay()))
                .map(schedule -> schedule.getId().getChrono().getStartTime().getHour()) // On suppose que Chrono contient l'heure de début
                .distinct()
                .sorted()
                .toList();

        // Calculer les heures quotidiennes
        long dailyHours = 0;
        for (int i = 1; i < hours.size(); i++) {
            dailyHours += hours.get(i) - hours.get(i - 1);
        }

        return dailyHours;
    }


    private static long calculateRestHours(List<Schedule> schedules) {
        // Extraire les horaires des Chrono associés aux jours travaillés
        List<LocalTime> workTimes = schedules.stream()
                .map(schedule -> schedule.getId().getChrono().getEndTime()) // Utilisation de l'heure de fin de Chrono
                .distinct()
                .sorted()
                .toList();

        long restHours = 0;
        for (int i = 1; i < workTimes.size(); i++) {
            // Calculer la différence en heures entre les horaires de fin des jours consécutifs
            long diff = ChronoUnit.HOURS.between(workTimes.get(i - 1), workTimes.get(i));
            restHours += diff;
        }

        return restHours;
    }

    private static long calculateDailyAmplitude(List<Schedule> schedules, ChronoDay chronoDay) {
        // Filter schedules that match the given ChronoDay
        List<LocalTime> startTimes = schedules.stream()
                .filter(schedule -> schedule.getId().getDay().equals(chronoDay.getId().getDay()))  // Filter by day
                .map(schedule -> schedule.getId().getChrono().getStartTime())  // Extract start times
                .distinct()
                .sorted()
                .toList();

        List<LocalTime> endTimes = schedules.stream()
                .filter(schedule -> schedule.getId().getDay().equals(chronoDay.getId().getDay()))  // Filter by day
                .map(schedule -> schedule.getId().getChrono().getEndTime())  // Extract end times
                .distinct()
                .sorted()
                .toList();

        if (startTimes.isEmpty() || endTimes.isEmpty()) {
            return 0; // No scheduled classes for that day, so no amplitude
        }

        // Calculate the amplitude: the difference between the earliest start time and the latest end time
        long startOfDay = startTimes.getFirst().toSecondOfDay(); // Convert to seconds
        long endOfDay = endTimes.getLast().toSecondOfDay(); // Convert to seconds

        // Calculate the difference in seconds, and convert it to hours
        return ChronoUnit.HOURS.between(LocalTime.ofSecondOfDay(startOfDay), LocalTime.ofSecondOfDay(endOfDay));
    }

}
