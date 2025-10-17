package fr.campus.dungeoncrawlerapi.repository;

import fr.campus.dungeoncrawlerapi.domain.equipments.offensive.OffensiveEquipment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OffensiveEquipmentRepository extends CrudRepository<OffensiveEquipment, Integer> {
    Optional<OffensiveEquipment> findByName(String name);
}
