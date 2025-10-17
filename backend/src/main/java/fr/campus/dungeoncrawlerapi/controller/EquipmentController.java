package fr.campus.dungeoncrawlerapi.controller;

import fr.campus.dungeoncrawlerapi.domain.equipments.defensive.DefensiveEquipment;
import fr.campus.dungeoncrawlerapi.domain.equipments.offensive.OffensiveEquipment;
import fr.campus.dungeoncrawlerapi.dto.ApiResponse;
import fr.campus.dungeoncrawlerapi.dto.DefensiveEquipmentDTO;
import fr.campus.dungeoncrawlerapi.dto.OffensiveEquipmentDTO;
import fr.campus.dungeoncrawlerapi.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * GET    /equipments/offensive     - Liste des equipements offensifs
 * POST   /equipments/offensive     - Créer un équipement offensif
 * DELETE /equipments/offensive/{id}- Suprimer un équipement offensif
 * GET    /equipments/defensive     - Liste des equipements défensifs
 * POST   /equipments/defensive     - Créer un équipement défensif
 * DELETE /equipments/defensive/{id}- Supprimer un équipement défensif
 */
@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class EquipmentController {

    private final EquipmentService equipmentService;

    @Autowired
    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    // [GET] /equipments/offensive : Liste des equipements offensifs
    @GetMapping("/equipments/offensive")
    public Iterable<OffensiveEquipment> getOffensiveEquipments() {
        return equipmentService.getOffensiveEquipments();
    }

    // [POST] /equipments/offensive : Créer un équipement offensif
    @PostMapping("/equipments/offensive")
    public OffensiveEquipment addOffensiveEquipment(@RequestBody OffensiveEquipmentDTO offensiveEquipmentDTO) {
        return equipmentService.createOffensiveEquipmentFromDTO(offensiveEquipmentDTO);
    }

    // [DELETE] /equipments/offensive/{id} : Suprimer un équipement offensif
    @DeleteMapping("/equipments/offensive/{id}")
    public void deleteOffensiveEquipment(@PathVariable Integer id) {
        equipmentService.deleteOffensiveEquipment(id);
    }

    // [GET] /equipments/defensive : Liste des equipements défensifs
    @GetMapping("/equipments/defensive")
    public Iterable<DefensiveEquipment> getDefensiveEquipments() {
        return equipmentService.getDefensiveEquipments();
    }

    // [POST] /equipments/defensive : Créer un équipement défensif
    @PostMapping("/equipments/defensive")
    public DefensiveEquipment addDefensiveEquipment(@RequestBody DefensiveEquipmentDTO defensiveEquipmentDTO) {
        return equipmentService.createDefensiveEquipmentFromDTO(defensiveEquipmentDTO);
    }

    // [DELETE] /equipments/defensive/{id} : Supprimer un équipement défensif
    @DeleteMapping("/equipments/defensive/{id}")
    public void deleteDefensiveEquipment(@PathVariable Integer id) {
        equipmentService.deleteDefensiveEquipment(id);
    }
}
