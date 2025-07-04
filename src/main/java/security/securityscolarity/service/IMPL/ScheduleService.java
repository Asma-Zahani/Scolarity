package security.securityscolarity.service.IMPL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import security.securityscolarity.entity.*;
import security.securityscolarity.repository.*;
import security.securityscolarity.service.validate.ValidateRoomConstraint;
import security.securityscolarity.service.validate.ValidateTeacherConstraint;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    private ChronoDayService chronoDayService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private TeacherService teacherService;

    public List<Schedule> testGenerateSchedule(University university) {
        List<Schedule> schedules = new ArrayList<>();

        List<Teacher> teachers = teacherService.findTeacherByUniversity(university);
        List<Room> rooms = roomService.findByBuildingUniversity(university);
        List<Group> groups = groupService.findByUniversity(university);
        List<ChronoDay> chronoDays = chronoDayService.findByUniversity(university);

        if (teachers.isEmpty() || rooms.isEmpty() || groups.isEmpty() || chronoDays.isEmpty()) {
            System.out.println("Une des listes nécessaires pour générer les horaires est vide.");
            return null;
        }

        List<Schedule> existingSchedules = scheduleRepository.findScheduleByTeacher(teachers.getFirst());
        if (!existingSchedules.isEmpty()) {
            System.out.println("La base de données contient déjà des horaires pour cette université.");
            return schedules;
        }
        schedules.add(new Schedule());
        return schedules;
    }


    public void generateSchedule(University university) {
        List<Schedule> schedules = new ArrayList<>();
        List<Teacher> teachers = teacherService.findTeacherByUniversity(university);
        List<Room> rooms = roomService.findByBuildingUniversity(university);
        List<Group> groups = groupService.findByUniversity(university);
        List<ChronoDay> chronoDays = chronoDayService.findByUniversity(university);

        for (Group group : groups) {
            List<Subject> tpsFraction2 = new ArrayList<>();
            List<Subject> tpsAutre = new ArrayList<>();
            List<Subject> cours = new ArrayList<>();

            for (Subject subject : group.getSubjects()) {
                if (subject.getSession().equals("TP")) {
                    if (subject.getFraction() == 2) {
                        tpsFraction2.add(subject);
                    } else {
                        tpsAutre.add(subject);
                    }
                } else if (subject.getSession().equals("CI")) {
                    cours.add(subject);
                }
            }

            for (int index = 0; index < tpsFraction2.size(); index += 2) {
                Subject subject1 = tpsFraction2.get(index);
                Subject subject2 = (index + 1 < tpsFraction2.size()) ? tpsFraction2.get(index + 1) : null;

                Optional<Room> availableRoomForSubject1 = rooms.stream()
                        .filter(room -> room.getSessionType().equals(subject1.getSession()))
                        .findFirst();

                // Vérification que subject2 n'est pas null avant de traiter
                Optional<Room> availableRoomForSubject2 = Optional.empty();
                if (subject2 != null) {
                    availableRoomForSubject2 = rooms.stream()
                            .filter(room -> room.getSessionType().equals(subject2.getSession()))
                            .filter(room -> availableRoomForSubject1.map(r -> !r.equals(room)).orElse(true))
                            .findFirst();
                }

                if (availableRoomForSubject1.isPresent() && (subject2 == null || availableRoomForSubject2.isPresent())) {
                    Room room1 = availableRoomForSubject1.get();
                    Room room2 = subject2 != null ? availableRoomForSubject2.get() : null;

                    Optional<Teacher> availableTeacherForSubject1 = teachers.stream()
                            .filter(teacher -> teacher.getSubjects().contains(subject1))
                            .findFirst();

                    // Vérification que subject2 n'est pas null avant de traiter
                    Optional<Teacher> availableTeacherForSubject2 = Optional.empty();
                    if (subject2 != null) {
                        availableTeacherForSubject2 = teachers.stream()
                                .filter(teacher -> teacher.getSubjects().contains(subject2))
                                .filter(teacher -> availableTeacherForSubject1.map(t -> !t.equals(teacher)).orElse(true))
                                .findFirst();
                    }

                    if (availableTeacherForSubject1.isPresent() && (subject2 == null || availableTeacherForSubject2.isPresent())) {
                        Teacher teacher1 = availableTeacherForSubject1.get();
                        Teacher teacher2 = subject2 != null ? availableTeacherForSubject2.get() : null;

                        for (ChronoDay chronoDay : chronoDays) {
                            Optional<ChronoDay> nextChronoForSubject1 = findNextChrono(chronoDay, chronoDays, schedules, teacher1, room1, group, subject1);
                            Optional<ChronoDay> nextChronoForSubject2 = subject2 != null ? findNextChrono(chronoDay, chronoDays, schedules, teacher2, room2, group, subject2) : Optional.empty();

                            if (isChronoDayAvailable(chronoDay, schedules, teacher1, room1, group, subject1)
                                    && (subject2 == null || isChronoDayAvailable(chronoDay, schedules, teacher2, room2, group, subject2))
                                    && nextChronoForSubject1.isPresent()
                                    && (subject2 == null || nextChronoForSubject2.isPresent())) {

                                // Création de l'emploi du temps pour subject1
                                Schedule schedule1 = new Schedule();
                                schedule1.setId(new ScheduleId(chronoDay.getId().getChrono(), chronoDay.getId().getDay(), group, subject1));
                                schedule1.setRoom(room1);
                                schedule1.setTeacher(teacher1);
                                schedules.add(schedule1);
                                scheduleRepository.save(schedule1);

                                // Création de l'emploi du temps pour subject2 si non null
                                if (subject2 != null) {
                                    Schedule schedule2 = new Schedule();
                                    schedule2.setId(new ScheduleId(chronoDay.getId().getChrono(), chronoDay.getId().getDay(), group, subject2));
                                    schedule2.setRoom(room2);
                                    schedule2.setTeacher(teacher2);
                                    schedules.add(schedule2);
                                    scheduleRepository.save(schedule2);
                                }

                                // Création du second créneau pour subject1
                                ChronoDay secondChronoForSubject1 = nextChronoForSubject1.get();
                                Schedule secondSchedule1 = new Schedule();
                                secondSchedule1.setId(new ScheduleId(secondChronoForSubject1.getId().getChrono(), secondChronoForSubject1.getId().getDay(), group , subject1));
                                secondSchedule1.setRoom(room1);
                                secondSchedule1.setTeacher(teacher1);
                                schedules.add(secondSchedule1);
                                scheduleRepository.save(secondSchedule1);

                                // Création du second créneau pour subject2 si non null
                                if (subject2 != null) {
                                    ChronoDay secondChronoForSubject2 = nextChronoForSubject2.get();
                                    Schedule secondSchedule2 = new Schedule();
                                    secondSchedule2.setId(new ScheduleId(secondChronoForSubject2.getId().getChrono(), secondChronoForSubject2.getId().getDay(), group , subject2));
                                    secondSchedule2.setRoom(room2);
                                    secondSchedule2.setTeacher(teacher2);
                                    schedules.add(secondSchedule2);
                                    scheduleRepository.save(secondSchedule2);
                                }

                                break;  // Une fois l'emploi du temps créé pour les deux matières, on passe à la matière suivante
                            }
                        }
                    }
                }
            }

            for (int index = 0; index < tpsAutre.size(); index += 2) {
                Subject subject1 = tpsAutre.get(index);
                Subject subject2 = (index + 1 < tpsAutre.size()) ? tpsAutre.get(index + 1) : null;

                Optional<Room> availableRoomForSubject1 = rooms.stream()
                        .filter(room -> room.getSessionType().equals(subject1.getSession()))
                        .findFirst();

                // Vérification que subject2 n'est pas null avant de traiter
                Optional<Room> availableRoomForSubject2 = Optional.empty();
                if (subject2 != null) {
                    availableRoomForSubject2 = rooms.stream()
                            .filter(room -> room.getSessionType().equals(subject2.getSession()))
                            .filter(room -> availableRoomForSubject1.map(r -> !r.equals(room)).orElse(true))
                            .findFirst();
                }

                if (availableRoomForSubject1.isPresent() && (subject2 == null || availableRoomForSubject2.isPresent())) {
                    Room room1 = availableRoomForSubject1.get();
                    Room room2 = subject2 != null ? availableRoomForSubject2.get() : null;

                    Optional<Teacher> availableTeacherForSubject1 = teachers.stream()
                            .filter(teacher -> teacher.getSubjects().contains(subject1))
                            .findFirst();

                    // Vérification que subject2 n'est pas null avant de traiter
                    Optional<Teacher> availableTeacherForSubject2 = Optional.empty();
                    if (subject2 != null) {
                        availableTeacherForSubject2 = teachers.stream()
                                .filter(teacher -> teacher.getSubjects().contains(subject2))
                                .filter(teacher -> availableTeacherForSubject1.map(t -> !t.equals(teacher)).orElse(true))
                                .findFirst();
                    }

                    if (availableTeacherForSubject1.isPresent() && (subject2 == null || availableTeacherForSubject2.isPresent())) {
                        Teacher teacher1 = availableTeacherForSubject1.get();
                        Teacher teacher2 = subject2 != null ? availableTeacherForSubject2.get() : null;

                        for (ChronoDay chronoDay : chronoDays) {
                            Optional<ChronoDay> nextChronoForSubject1 = findNextChrono(chronoDay, chronoDays, schedules, teacher1, room1, group, subject1);
                            Optional<ChronoDay> nextChronoForSubject2 = subject2 != null ? findNextChrono(chronoDay, chronoDays, schedules, teacher2, room2, group, subject2) : Optional.empty();

                            if (isChronoDayAvailable(chronoDay, schedules, teacher1, room1, group, subject1)
                                    && (subject2 == null || isChronoDayAvailable(chronoDay, schedules, teacher2, room2, group, subject2))
                                    && nextChronoForSubject1.isPresent()
                                    && (subject2 == null || nextChronoForSubject2.isPresent())) {

                                // Création de l'emploi du temps pour subject1
                                Schedule schedule1 = new Schedule();
                                schedule1.setId(new ScheduleId(chronoDay.getId().getChrono(), chronoDay.getId().getDay(), group, subject1));
                                schedule1.setRoom(room1);
                                schedule1.setTeacher(teacher1);
                                schedules.add(schedule1);
                                scheduleRepository.save(schedule1);

                                // Création de l'emploi du temps pour subject2 si non null
                                if (subject2 != null) {
                                    Schedule schedule2 = new Schedule();
                                    schedule2.setId(new ScheduleId(chronoDay.getId().getChrono(), chronoDay.getId().getDay(), group, subject2));
                                    schedule2.setRoom(room2);
                                    schedule2.setTeacher(teacher2);
                                    schedules.add(schedule2);
                                    scheduleRepository.save(schedule2);
                                }

                                // Création du second créneau pour subject1
                                ChronoDay secondChronoForSubject1 = nextChronoForSubject1.get();
                                Schedule secondSchedule1 = new Schedule();
                                secondSchedule1.setId(new ScheduleId(secondChronoForSubject1.getId().getChrono(), secondChronoForSubject1.getId().getDay(), group , subject1));
                                secondSchedule1.setRoom(room1);
                                secondSchedule1.setTeacher(teacher1);
                                schedules.add(secondSchedule1);
                                scheduleRepository.save(secondSchedule1);

                                // Création du second créneau pour subject2 si non null
                                if (subject2 != null) {
                                    ChronoDay secondChronoForSubject2 = nextChronoForSubject2.get();
                                    Schedule secondSchedule2 = new Schedule();
                                    secondSchedule2.setId(new ScheduleId(secondChronoForSubject2.getId().getChrono(), secondChronoForSubject2.getId().getDay(), group , subject2));
                                    secondSchedule2.setRoom(room2);
                                    secondSchedule2.setTeacher(teacher2);
                                    schedules.add(secondSchedule2);
                                    scheduleRepository.save(secondSchedule2);
                                }

                                int maxFractions = Math.max(subject1.getFraction(), subject2 != null ? subject2.getFraction() : 0);

                                for (int i = 0; i < maxFractions / 2 ; i= i+2) {
                                    ChronoDay nextAvailableChrono1 = findNextAvailableChronoOnOtherDay(chronoDay, teacher1, room1, group, subject1, chronoDays , schedules);
                                    ChronoDay nextAvailableChrono2 = subject2 != null ? findNextAvailableChronoOnOtherDay(chronoDay, teacher2, room2, group, subject2, chronoDays, schedules) : null;

                                    if (nextAvailableChrono1 != null && (subject2 == null || nextAvailableChrono2 != null)) {
                                        Schedule additionalSchedule1 = new Schedule();
                                        additionalSchedule1.setId(new ScheduleId(nextAvailableChrono1.getId().getChrono(), nextAvailableChrono1.getId().getDay(), group, subject1));
                                        additionalSchedule1.setRoom(room1);
                                        additionalSchedule1.setTeacher(teacher1);
                                        schedules.add(additionalSchedule1);
                                        scheduleRepository.save(additionalSchedule1);

                                        if (subject2 != null) {
                                            Schedule additionalSchedule2 = new Schedule();
                                            additionalSchedule2.setId(new ScheduleId(nextAvailableChrono2.getId().getChrono(), nextAvailableChrono2.getId().getDay(), group, subject2));
                                            additionalSchedule2.setRoom(room2);
                                            additionalSchedule2.setTeacher(teacher2);
                                            schedules.add(additionalSchedule2);
                                            scheduleRepository.save(additionalSchedule2);
                                        }
                                    }
                                }

                                break;  // Une fois l'emploi du temps créé pour les deux matières, on passe à la matière suivante
                            }
                        }
                    }
                }
            }
            for (Subject subject: cours) {
                Optional<Room> availableRoom = rooms.stream()
                        .filter(room -> room.getSessionType().equals(subject.getSession()))
                        .findFirst();

                if (availableRoom.isPresent()) {
                    Room room = availableRoom.get();

                    Optional<Teacher> availableTeacher = teachers.stream()
                            .filter(teacher -> teacher.getSubjects().contains(subject))
                            .findFirst();

                    if (availableTeacher.isPresent()) {
                        Teacher teacher = availableTeacher.get();

                        for (ChronoDay chronoDay : chronoDays) {
                            if (isChronoDayAvailable(chronoDay, schedules, teacher, room, group, subject)) {

                                Schedule schedule = new Schedule();
                                schedule.setId(new ScheduleId(chronoDay.getId().getChrono(), chronoDay.getId().getDay(), group, subject));
                                schedule.setRoom(room);
                                schedule.setTeacher(teacher);
                                schedules.add(schedule);
                                scheduleRepository.save(schedule);

                                if (subject.getFraction() == 2) {
                                    Optional<ChronoDay> nextChrono = findNextChrono(chronoDay, chronoDays, schedules, teacher, room, group, subject);

                                    if (nextChrono.isPresent()) {
                                        ChronoDay secondChrono = nextChrono.get();

                                        Schedule secondSchedule = new Schedule();
                                        secondSchedule.setId(new ScheduleId(secondChrono.getId().getChrono(), secondChrono.getId().getDay(), group , subject));
                                        secondSchedule.setRoom(room);
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
            }
        }
    }

    private boolean isChronoDayAvailable(ChronoDay chronoDay, List<Schedule> schedules, Teacher teacher, Room room , Group group, Subject subject) {
        if (!ValidateRoomConstraint.isRoomAvailable(room, chronoDay, schedules)) {
            return false;
        }

        if (!ValidateTeacherConstraint.isTeacherAvailable(teacher, schedules, chronoDay)) {
            return false;
        }

        return schedules.stream()
                .noneMatch(schedule ->
                        schedule.getId().getDay().equals(chronoDay.getId().getDay()) &&
                                schedule.getId().getChrono().equals(chronoDay.getId().getChrono()) &&
                                (schedule.getId().getGroup().equals(group) || schedule.getId().getSubject().equals(subject))
                );
    }


    private Optional<ChronoDay> findNextChrono(ChronoDay currentChrono, List<ChronoDay> chronoDays, List<Schedule> schedules, Teacher teacher, Room room , Group group, Subject subject) {
        return chronoDays.stream()
                .filter(chrono -> chrono.getId().getDay().equals(currentChrono.getId().getDay()))
                .filter(chrono -> chrono.getId().getChrono().getStartTime().isAfter(currentChrono.getId().getChrono().getEndTime()))
                .filter(chrono -> isChronoDayAvailable(chrono, schedules, teacher, room, group, subject))
                .findFirst();
    }

    private ChronoDay findNextAvailableChronoOnOtherDay(ChronoDay currentChrono, Teacher teacher, Room room, Group group, Subject subject, List<ChronoDay> chronoDays, List<Schedule> schedules) {
        for (ChronoDay day : chronoDays) {
            if (!day.getId().getDay().equals(currentChrono.getId().getDay())) {  // On cherche un créneau sur un autre jour
                if (isChronoDayAvailable(day, schedules, teacher, room, group, subject)) {
                    return day;  // Retourne le premier créneau valide trouvé
                }
            }
        }
        return null;  // Aucun créneau disponible sur un autre jour
    }
}