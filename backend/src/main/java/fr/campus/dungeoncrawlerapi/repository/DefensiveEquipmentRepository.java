package fr.campus.dungeoncrawlerapi.repository;

import fr.campus.dungeoncrawlerapi.domain.equipments.defensive.DefensiveEquipment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DefensiveEquipmentRepository extends CrudRepository<DefensiveEquipment, Integer> {
    Optional<DefensiveEquipment> findByName(String name);
}

