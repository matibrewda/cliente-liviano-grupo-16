package ar.utn.frba.ddsi.cliente_liviano.services;

import ar.utn.frba.ddsi.cliente_liviano.models.repositories.ArchivosRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.http.HttpClient;

@Service
public class ArchivoService {
    @Autowired
    ArchivosRepository archivosRepository;

    public void almacenarArchivo(MultipartFile file) {
        archivosRepository.save(file);

    }
}
