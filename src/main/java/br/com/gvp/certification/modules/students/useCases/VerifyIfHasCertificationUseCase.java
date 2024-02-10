package br.com.gvp.certification.modules.students.useCases;

import br.com.gvp.certification.modules.students.dto.VerifyHasCertificationDTO;
import br.com.gvp.certification.modules.students.repositories.CertificationsStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerifyIfHasCertificationUseCase {

    @Autowired
    private CertificationsStudentRepository certificationsStudentRepository;

    public boolean execute(VerifyHasCertificationDTO dto) {
        var result = this.certificationsStudentRepository.findByStudentEmailAndTechnology(dto.getEmail(), dto.getTechnology());
        return !result.isEmpty();
    }
}
