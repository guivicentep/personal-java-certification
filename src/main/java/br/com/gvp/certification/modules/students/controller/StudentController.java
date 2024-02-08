package br.com.gvp.certification.modules.students.controller;

import br.com.gvp.certification.modules.students.dto.StudentCertificationAnswerDTO;
import br.com.gvp.certification.modules.students.dto.VerifyHasCertificationDTO;
import br.com.gvp.certification.modules.students.useCases.StudentCertificationAnswersUseCase;
import br.com.gvp.certification.modules.students.useCases.VerifyIfHasCertificationUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private VerifyIfHasCertificationUseCase verifyIfHasCertificationUseCase;

    @Autowired
    private StudentCertificationAnswersUseCase studentCertificationAnswersUseCase;

    @PostMapping("/verify")
    public String verifyIfHasCertification(@RequestBody VerifyHasCertificationDTO verifyHasCertificationDTO) {

        var result = this.verifyIfHasCertificationUseCase.execute(verifyHasCertificationDTO);

        if (result) {
            return "Usuário já fez a prova";
        }
        return "Usuário pode fazer a prova";
    }

    @PostMapping("/certification/answer")
    public StudentCertificationAnswerDTO certificationAnswerDTO(@RequestBody StudentCertificationAnswerDTO studentCertificationAnswerDTO)  {
       return this.studentCertificationAnswersUseCase.execute(studentCertificationAnswerDTO);
    }
}
