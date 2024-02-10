package br.com.gvp.certification.modules.students.repositories;

import br.com.gvp.certification.modules.students.entities.CertificationStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CertificationsStudentRepository extends JpaRepository<CertificationStudent, UUID> {

    @Query("SELECT c FROM certifications c INNER JOIN c.student std WHERE std.email = :email AND c.technology = :technology")
    List<CertificationStudent> findByStudentEmailAndTechnology(String email, String technology);

    @Query("SELECT c from certifications c ORDER BY c.grate DESC LIMIT 10")
    List<CertificationStudent> findTop10ByOrderByGrateDesc();
}
