package com.phi.battleshipapp.persistence.repo;

import com.phi.battleshipapp.persistence.model.Salvo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalvoRepository extends JpaRepository<Salvo, Long> {

}
