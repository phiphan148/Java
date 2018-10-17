package com.phi.battleshipapp.persistence.repo;

import com.phi.battleshipapp.persistence.model.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface ShipRepository extends JpaRepository<Ship, Long> {

}
