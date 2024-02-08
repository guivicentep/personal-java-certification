package br.com.gvp.certification.modules.students.useCases;

import br.com.gvp.certification.modules.questions.entities.Question;
import br.com.gvp.certification.modules.questions.repositories.QuestionRepository;
import br.com.gvp.certification.modules.students.dto.StudentCertificationAnswerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentCertificationAnswersUseCase {

    @Autowired
    private QuestionRepository questionRepository;

    public StudentCertificationAnswerDTO execute(StudentCertificationAnswerDTO dto)  {

        // Buscar as alternativas da perguntas
        List<Question> questions = questionRepository.findByTechnology(dto.getTechnology());

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
                    } else {
                        questionAnswer.setCorrect(false);
                    }
                });

                return dto;
        // Salvar as informações da certificação

    }
}
