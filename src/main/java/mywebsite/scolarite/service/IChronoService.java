package mywebsite.scolarite.service;

import mywebsite.scolarite.entity.Chrono;
import mywebsite.scolarite.entity.Room;

import java.util.List;

public interface IChronoService {

    List<Chrono> findAll();
    Chrono findByChronoID(Long id);
    Chrono addChrono(Chrono chrono);
    void deleteChrono(Long id);
    Chrono updateChrono(Long id , Chrono chrono);
}
