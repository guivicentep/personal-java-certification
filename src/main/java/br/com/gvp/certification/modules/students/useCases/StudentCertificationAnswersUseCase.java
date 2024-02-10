package br.com.gvp.certification.modules.students.useCases;

import br.com.gvp.certification.modules.questions.entities.Question;
import br.com.gvp.certification.modules.questions.repositories.QuestionRepository;
import br.com.gvp.certification.modules.students.dto.StudentCertificationAnswerDTO;
import br.com.gvp.certification.modules.students.dto.VerifyHasCertificationDTO;
import br.com.gvp.certification.modules.students.entities.AnswersCertification;
import br.com.gvp.certification.modules.students.entities.CertificationStudent;
import br.com.gvp.certification.modules.students.entities.Student;
import br.com.gvp.certification.modules.students.repositories.CertificationsStudentRepository;
import br.com.gvp.certification.modules.students.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StudentCertificationAnswersUseCase {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private CertificationsStudentRepository certificationsStudentRepository;

    @Autowired
    private VerifyIfHasCertificationUseCase verifyIfHasCertificationUseCase;


    public CertificationStudent execute(StudentCertificationAnswerDTO dto) throws Exception {

        var hasCertification = this.verifyIfHasCertificationUseCase.execute(new VerifyHasCertificationDTO(dto.getEmail(), dto.getTechnology()));

        if (hasCertification) {
            throw new Exception("Você já tirou sua certificação!");
        }

        // Buscar as alternativas da perguntas
        List<Question> questions = questionRepository.findByTechnology(dto.getTechnology());
        List<AnswersCertification> answersCertifications = new ArrayList<>();

        AtomicInteger correctAnswers = new AtomicInteger();

        // Validar se estão corretas ou incorretas
        dto.getQuestionsAnswers()
                .stream()
                .forEach(questionAnswer -> {
                    var question = questions.stream()
                            .filter(q -> q.getId().equals(questionAnswer.getQuestionId()))
                            .findFirst().get();

                    var findCorrectAlternative = question.getAlternatives()
                            .stream()
                            .filter(alternative -> alternative.isCorrect())
                            .findFirst().get();

                    if (findCorrectAlternative.getId().equals(questionAnswer.getAlternativeId())) {
                        questionAnswer.setCorrect(true);
                        correctAnswers.incrementAndGet();
                    } else {
                        questionAnswer.setCorrect(false);
                    }

                    var answersCertificationsEntity = AnswersCertification.builder()
                                    .answerId(questionAnswer.getAlternativeId())
                                    .questionId(questionAnswer.getQuestionId())
                                    .isCorrect(questionAnswer.isCorrect()).build();

                    answersCertifications.add(answersCertificationsEntity);
                });

        // Verificar se o estudante existe pelo email
        var student = studentRepository.findByEmail(dto.getEmail());
        UUID studentId;
        if (student.isEmpty()) {
            var studentCreated = Student.builder().email(dto.getEmail()).build();
            studentCreated = studentRepository.save(studentCreated);
            studentId = studentCreated.getId();
        } else {
            studentId = student.get().getId();
        }

        CertificationStudent certificationStudent = CertificationStudent.builder()
                .technology(dto.getTechnology())
                .studentId(studentId)
                .grate(correctAnswers.get())
                .build();

        var certificationStudentCreated = certificationsStudentRepository.save(certificationStudent);

        answersCertifications.stream().forEach(answersCertification -> {
            answersCertification.setCertificationId(certificationStudent.getId());
            answersCertification.setCertificationStudent(certificationStudent);
        });

        certificationStudent.setAnswersCertificationEntities(answersCertifications);

        certificationsStudentRepository.save(certificationStudent);

        return certificationStudentCreated;

    }
}
