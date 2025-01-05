package security.securityscolarity.service.validate;

import security.securityscolarity.entity.*;

import java.util.List;

public class ValidateRoomConstraint {
    public static boolean isRoomAvailable(Room room, ChronoDay chronoDay, List<Schedule> schedules) {
        RoomConstraint roomConstraint = room.getConstraint();

        if (roomConstraint != null) {
            boolean isRoomUnavailable = roomConstraint.getUnavailableDays().stream()
                    .anyMatch(chrono -> chrono.equals(chronoDay));
            if (isRoomUnavailable) {
                return false;
            }
        }

        return schedules.stream().noneMatch(schedule ->
                schedule.getRoom().equals(room) &&
                        schedule.getId().getDay().equals(chronoDay.getId().getDay()) &&
                        schedule.getId().getChrono().equals(chronoDay.getId().getChrono())
        );
    }
}
