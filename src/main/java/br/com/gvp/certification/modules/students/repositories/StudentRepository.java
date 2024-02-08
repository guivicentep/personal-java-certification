package br.com.gvp.certification.modules.students.repositories;

import br.com.gvp.certification.modules.students.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {

    public Optional<Student> findByEmail(String email);
}
