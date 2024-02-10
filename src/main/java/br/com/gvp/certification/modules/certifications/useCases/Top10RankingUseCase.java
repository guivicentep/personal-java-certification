package br.com.gvp.certification.modules.certifications.useCases;

import br.com.gvp.certification.modules.students.entities.CertificationStudent;
import br.com.gvp.certification.modules.students.repositories.CertificationsStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Top10RankingUseCase {

    @Autowired
    private CertificationsStudentRepository certificationsStudentRepository;
    public List<CertificationStudent> execute() {
        var result = this.certificationsStudentRepository.findTop10ByOrderByGrateDesc();
        return result;
    }
}
