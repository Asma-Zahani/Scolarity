package mywebsite.scolarite.service.IMPL;

import mywebsite.scolarite.entity.*;
import mywebsite.scolarite.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {

    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    ChronoDayRepository chronoDayRepository;
    @Autowired
    ScheduleRepository scheduleRepository;

    public List<Schedule> findSchedulesForChronoAndDay(Chrono chrono, Day day) {
        return scheduleRepository.findScheduleByIdChronoAndIdDay(chrono, day);
    }

    public List<Schedule> getScheduleForGroup(Long groupId) {
        Group group = groupRepository.findById(groupId).orElse(null);
        List<Schedule> existingSchedules = scheduleRepository.findScheduleByIdGroup(group);
        if (!existingSchedules.isEmpty()) {
            return existingSchedules;
        } else {
            assert group != null;
            return generateScheduleForGroup(group);
        }
    }

    public List<Schedule> getScheduleForGroup(Group group) {
        List<Schedule> existingSchedules = scheduleRepository.findScheduleByIdGroup(group);
        if (!existingSchedules.isEmpty()) {
            return existingSchedules;
        } else {
            return generateScheduleForGroup(group);
        }
    }

    public List<Schedule> getScheduleForAllGroups() {
        List<Group> groups = groupRepository.findAll();
        List<Schedule> allSchedules = new ArrayList<>();

        for (Group group : groups) {
            List<Schedule> existingSchedules = scheduleRepository.findScheduleByIdGroup(group);
            if (!existingSchedules.isEmpty()) {
                allSchedules.addAll(existingSchedules);
            } else {
                List<Schedule> newSchedules = generateScheduleForGroup(group);
                allSchedules.addAll(newSchedules);
            }
        }

        return allSchedules;
    }

    public List<Schedule> generateScheduleForGroup(Group group) {
        List<Schedule> schedules = new ArrayList<>();
        List<Teacher> teachers = teacherRepository.findAll();
        List<Room> rooms = roomRepository.findAll();
        List<ChronoDay> chronoDays = chronoDayRepository.findAll();

        for (Subject subject : group.getSubjects()) {
            String requiredBuilding = subject.getSession().equals("CI") ? "C" : "LI"; // a modifier

            Optional<Teacher> availableTeacher = teachers.stream()
                    .filter(teacher -> teacher.getSubjects().contains(subject))
                    .filter(teacher -> isTeacherAvailable(teacher, schedules))
                    .findFirst();

            Optional<Room> availableRoom = rooms.stream()
                    .filter(room -> room.getBuilding().getBuildingName().equals(requiredBuilding))
                    .filter(room -> isRoomAvailable(room, schedules))
                    .findFirst();

            if (availableTeacher.isPresent() && availableRoom.isPresent()) {
                Teacher teacher = availableTeacher.get();
                Room room = availableRoom.get();

                for (ChronoDay chronoDay : chronoDays) {
                    if (isChronoDayAvailable(chronoDay, schedules, teacher, room)) {
                        Schedule schedule = new Schedule();
                        schedule.setId(new ScheduleId(chronoDay.getId().getChrono(), chronoDay.getId().getDay(), group));
                        schedule.setRoom(room);
                        schedule.setSubject(subject);
                        schedule.setTeacher(teacher);

                        //schedules.add(schedule);
                        scheduleRepository.save(schedule);

                        if (subject.getFraction() == 2) {
                            Optional<ChronoDay> nextChrono = findNextChrono(chronoDay, chronoDays, schedules, teacher);
                            if (nextChrono.isPresent()) {
                                ChronoDay secondChrono = nextChrono.get();
                                Schedule secondSchedule = new Schedule();
                                secondSchedule.setId(new ScheduleId(secondChrono.getId().getChrono(), secondChrono.getId().getDay(), group));
                                secondSchedule.setRoom(room);
                                secondSchedule.setSubject(subject);
                                secondSchedule.setTeacher(teacher);

                                schedules.add(secondSchedule);
                                scheduleRepository.save(secondSchedule);
                            }
                        }
                        break;
                    }
                }
            }
        }
        return schedules;
    }

    public List<Schedule> generateSchedule() {
        List<Schedule> schedules = new ArrayList<>();
        List<Teacher> teachers = teacherRepository.findAll();
        List<Room> rooms = roomRepository.findAll();
        List<Group> groups = groupRepository.findAll();
        List<ChronoDay> chronoDays = chronoDayRepository.findAll();

        for (Group group : groups) {
            for (Subject subject : group.getSubjects()) {
                String requiredBuilding = subject.getSession().equals("CI") ? "C" : "LI";

                Optional<Teacher> availableTeacher = teachers.stream()
                        .filter(teacher -> teacher.getSubjects().contains(subject))
                        .filter(teacher -> isTeacherAvailable(teacher, schedules))
                        .findFirst();

                Optional<Room> availableRoom = rooms.stream()
                        .filter(room -> room.getBuilding().getBuildingName().equals(requiredBuilding))
                        .filter(room -> isRoomAvailable(room, schedules))
                        .findFirst();

                if (availableTeacher.isPresent() && availableRoom.isPresent()) {
                    Teacher teacher = availableTeacher.get();
                    Room room = availableRoom.get();

                    // Vérification des créneaux disponibles
                    for (ChronoDay chronoDay : chronoDays) {
                        if (isChronoDayAvailable(chronoDay, schedules, teacher, room)) {
                            Schedule schedule = new Schedule();
                            schedule.setId(new ScheduleId(chronoDay.getId().getChrono(), chronoDay.getId().getDay(), group));
                            schedule.setRoom(room);
                            schedule.setSubject(subject);
                            schedule.setTeacher(teacher);

                            schedules.add(schedule);
                            scheduleRepository.save(schedule);

                            // Gérer les matières fractionnées
                            if (subject.getFraction() == 2) {
                                Optional<ChronoDay> nextChrono = findNextChrono(chronoDay, chronoDays, schedules, teacher);
                                if (nextChrono.isPresent()) {
                                    ChronoDay secondChrono = nextChrono.get();
                                    Schedule secondSchedule = new Schedule();
                                    secondSchedule.setId(new ScheduleId(secondChrono.getId().getChrono(), secondChrono.getId().getDay(), group));
                                    secondSchedule.setRoom(room);
                                    secondSchedule.setSubject(subject);
                                    secondSchedule.setTeacher(teacher);

                                    schedules.add(secondSchedule);
                                    scheduleRepository.save(secondSchedule);
                                }
                            }
                            break; // Passe au prochain sujet après avoir trouvé un créneau
                        }
                    }
                }
            }
        }
        return schedules;
    }

    private Optional<ChronoDay> findAvailableChronoDayForConsecutive(List<Schedule> schedules, Teacher teacher, ChronoDay firstChronoDay) {
        return chronoDayRepository.findAll().stream()
                .filter(chrono -> chrono.getId().getDay().equals(firstChronoDay.getId().getDay())) // Même jour
                .filter(chrono -> chrono.getId().getChrono().getStartTime().isAfter(firstChronoDay.getId().getChrono().getEndTime())) // Début après la fin du premier créneau
                .filter(chrono -> !isRoomOccupied(schedules, chrono)) // Vérifier si la salle est libre
                .filter(chrono -> !isTeacherUnavailable(teacher, schedules, chrono)) // Vérifier si l'enseignant est disponible
                .min(Comparator.comparing(chrono -> chrono.getId().getChrono().getStartTime()));
    }

    private Optional<ChronoDay> findAvailableChronoDay(List<Schedule> schedules, Teacher teacher) {
        List<ChronoDay> chronoDays = chronoDayRepository.findAll();
        TeacherConstraint constraint = teacher.getConstraint();

        return chronoDays.stream()
                .filter(chronoDay -> {
                    // Vérification si le jour est indisponible pour l'enseignant
                    if (constraint != null && constraint.getUnavailableDays().contains(chronoDay.getId().getDay())) {
                        return false;
                    }

                    // Vérification si le créneau est déjà occupé
                    return schedules.stream()
                            .noneMatch(schedule ->
                                    schedule.getId().getDay().equals(chronoDay.getId().getDay()) &&
                                            schedule.getId().getChrono().equals(chronoDay.getId().getChrono()));
                })
                .findFirst();
    }

    private boolean isTeacherUnavailable(Teacher teacher, List<Schedule> schedules, ChronoDay chronoDay) {
        TeacherConstraint constraint = teacher.getConstraint();
        if (constraint == null) {
            return false;
        }

        Day currentDay = chronoDay.getId().getDay();
        return constraint.getUnavailableDays().contains(currentDay)
                || schedules.stream().anyMatch(schedule -> schedule.getTeacher().equals(teacher)
                && schedule.getId().getDay().equals(currentDay)
                && schedule.getId().getChrono().equals(chronoDay.getId().getChrono()));
    }

    private boolean isRoomOccupied(List<Schedule> schedules, ChronoDay chronoDay) {
        return schedules.stream().anyMatch(schedule -> schedule.getId().getDay().equals(chronoDay.getId().getDay())
                && schedule.getId().getChrono().equals(chronoDay.getId().getChrono()));
    }

    private boolean isRoomAvailable(Room room, List<Schedule> schedules) {
        return schedules.stream().noneMatch(schedule -> schedule.getRoom().equals(room)); // a modifier
    }

    private boolean isTeacherAvailable(Teacher teacher, List<Schedule> schedules) {
        TeacherConstraint constraint = teacher.getConstraint();
        if (constraint == null) {
            return true;
        }

        return schedules.stream().noneMatch(schedule -> schedule.getTeacher().equals(teacher)
                && constraint.getUnavailableDays().contains(schedule.getId().getDay()));
    }

    private boolean isChronoDayAvailable(ChronoDay chronoDay, List<Schedule> schedules, Teacher teacher, Room room) {
        if (isRoomOccupied(schedules, chronoDay, room)) {
            return false;
        }

        if (isTeacherUnavailable(teacher, schedules, chronoDay)) {
            return false;
        }

        return schedules.stream().noneMatch(schedule ->
                schedule.getId().getDay().equals(chronoDay.getId().getDay()) &&
                        schedule.getId().getChrono().equals(chronoDay.getId().getChrono())
        );
    }

    private Optional<ChronoDay> findNextChrono(ChronoDay currentChrono, List<ChronoDay> chronoDays, List<Schedule> schedules, Teacher teacher) {
        return chronoDays.stream()
                .filter(chrono -> chrono.getId().getDay().equals(currentChrono.getId().getDay()))
                .filter(chrono -> chrono.getId().getChrono().getStartTime().isAfter(currentChrono.getId().getChrono().getEndTime()))
                .filter(chrono -> isChronoDayAvailable(chrono, schedules, teacher, null))
                .findFirst();
    }

    private boolean isRoomOccupied(List<Schedule> schedules, ChronoDay chronoDay, Room room) {
        return schedules.stream().anyMatch(schedule ->
                schedule.getRoom().equals(room) &&
                        schedule.getId().getDay().equals(chronoDay.getId().getDay()) &&
                        schedule.getId().getChrono().equals(chronoDay.getId().getChrono())
        );
    }


}