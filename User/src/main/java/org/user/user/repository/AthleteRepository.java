package org.user.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.user.user.model.Athlete;

import java.util.List;
import java.util.UUID;

@Repository
public interface AthleteRepository extends JpaRepository<Athlete, UUID> {

}
