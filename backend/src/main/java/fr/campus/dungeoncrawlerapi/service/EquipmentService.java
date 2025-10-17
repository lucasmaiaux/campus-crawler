package fr.campus.dungeoncrawlerapi.service;

import fr.campus.dungeoncrawlerapi.domain.equipments.defensive.Armor;
import fr.campus.dungeoncrawlerapi.domain.equipments.defensive.DefensiveEquipment;
import fr.campus.dungeoncrawlerapi.domain.equipments.defensive.Helmet;
import fr.campus.dungeoncrawlerapi.domain.equipments.defensive.Shield;
import fr.campus.dungeoncrawlerapi.domain.equipments.offensive.OffensiveEquipment;
import fr.campus.dungeoncrawlerapi.domain.equipments.offensive.Spell;
import fr.campus.dungeoncrawlerapi.domain.equipments.offensive.Weapon;
import fr.campus.dungeoncrawlerapi.dto.OffensiveEquipmentDTO;
import fr.campus.dungeoncrawlerapi.dto.DefensiveEquipmentDTO;
import fr.campus.dungeoncrawlerapi.repository.DefensiveEquipmentRepository;
import fr.campus.dungeoncrawlerapi.repository.OffensiveEquipmentRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Service
public class EquipmentService {

    @Autowired
    private OffensiveEquipmentRepository offensiveEquipmentRepository;
    @Autowired
    private DefensiveEquipmentRepository defensiveEquipmentRepository;

    public Iterable<OffensiveEquipment> getOffensiveEquipments() {
        return offensiveEquipmentRepository.findAll();
    }

    public Iterable<DefensiveEquipment> getDefensiveEquipments() {
        return defensiveEquipmentRepository.findAll();
    }

    public OffensiveEquipment createOffensiveEquipmentFromDTO(OffensiveEquipmentDTO offensiveEquipmentDTO) {
        OffensiveEquipment offensiveEquipment;

        switch (offensiveEquipmentDTO.type) {
            case "Spell":
                offensiveEquipment = new Spell();
                break;
            case "Weapon":
                offensiveEquipment = new Weapon();
                break;
            default:
                throw new IllegalArgumentException("Type d'équipement inconnu : " + offensiveEquipmentDTO.type);
        }

        offensiveEquipment.setName(offensiveEquipmentDTO.name);
        offensiveEquipment.setAttack(offensiveEquipmentDTO.attack);

        return offensiveEquipmentRepository.save(offensiveEquipment);
    }

    public DefensiveEquipment createDefensiveEquipmentFromDTO(DefensiveEquipmentDTO defensiveEquipmentDTO) {
        DefensiveEquipment defensiveEquipment;

        switch (defensiveEquipmentDTO.type) {
            case "Armor":
                defensiveEquipment = new Armor();
                break;
            case "Helmet":
                defensiveEquipment = new Helmet();
                break;
            case "Shield":
                defensiveEquipment = new Shield();
                break;
            default:
                throw new IllegalArgumentException("Type d'équipement inconnu : " + defensiveEquipmentDTO.type);
        }

        defensiveEquipment.setName(defensiveEquipmentDTO.name);
        defensiveEquipment.setDefense(defensiveEquipmentDTO.defense);

        return defensiveEquipmentRepository.save(defensiveEquipment);
    }

    public void deleteOffensiveEquipment(Integer equipmentId) {
        offensiveEquipmentRepository.deleteById(equipmentId);
    }

    public void deleteDefensiveEquipment(Integer equipmentId) {
        defensiveEquipmentRepository.deleteById(equipmentId);
    }

}

