package com.example.segulaproject.Controllers;

import com.example.segulaproject.Entities.Specialty; 
import com.example.segulaproject.Repositories.SpecialtyRepository; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController 
@RequestMapping("/api/specialties") 
@CrossOrigin(origins = "http://localhost:3000") // autorise React à accéder 
public class SpecialtyController {

@Autowired
private SpecialtyRepository specialtyRepository;

// GET: Liste de toutes les spécialités
@GetMapping
public List<Specialty> getAllSpecialties() {
    return specialtyRepository.findAll();
}

// POST: Ajouter une spécialité (facultatif, pour test ou interface admin future)
@PostMapping
public Specialty addSpecialty(@RequestBody Specialty specialty) {
    return specialtyRepository.save(specialty);
}

// GET: Récupérer une spécialité par ID (optionnel)
@GetMapping("/{id}")
public Specialty getSpecialtyById(@PathVariable Long id) {
    return specialtyRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Spécialité non trouvée avec ID : " + id));
}
}

