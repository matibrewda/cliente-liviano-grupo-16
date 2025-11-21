package ar.utn.frba.ddsi.cliente_liviano.models.repositories;
import ar.utn.frba.ddsi.cliente_liviano.models.Hecho;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class HechosRepository {
    private final List<Hecho> hechos = new ArrayList<Hecho>();
    private Long proxId = 1L;

    public void HechosRepository(){}

    public List<Hecho> findAll(){return this.hechos;}

    /*public Optional<Hecho> findById(Long id){
        //return this.hechos.stream().filter(hecho -> hecho.getId().equals(id)).findFirst();
    }*/

    public void save(Hecho hechoNuevo){
    }
}
