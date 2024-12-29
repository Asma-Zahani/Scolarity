package mywebsite.scolarite.service.IMPL;

import mywebsite.scolarite.entity.Building;
import mywebsite.scolarite.entity.Chrono;
import mywebsite.scolarite.entity.Room;
import mywebsite.scolarite.repository.ChronoRepository;
import mywebsite.scolarite.repository.RoomRepository;
import mywebsite.scolarite.service.IChronoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChronoService implements IChronoService{
    @Autowired
    ChronoRepository chronoRepository;
    @Autowired
    private RoomRepository roomRepository;

    public List<Chrono> findAll() {
        return chronoRepository.findAll();
    }

    public Chrono findByChronoID(Long id) {
        return chronoRepository.findByChronoId(id);
    }

    public Chrono addChrono(Chrono Chrono) {
        return chronoRepository.save(Chrono);
    }
    public void deleteChrono(Long id) {
        chronoRepository.deleteById(id);
    }

    public Chrono updateChrono(Long id , Chrono Chrono) {
        Chrono chronoToUpdate = chronoRepository.findByChronoId(id);
        chronoToUpdate.setChronoId(id);
        chronoToUpdate.setChronoName(Chrono.getChronoName());
        chronoToUpdate.setChronoDescription(Chrono.getChronoDescription());
        chronoToUpdate.setStartTime(Chrono.getStartTime());
        chronoToUpdate.setEndTime(Chrono.getEndTime());
        return chronoRepository.save(chronoToUpdate);
    }
}
