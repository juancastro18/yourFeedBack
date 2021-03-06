package com.backend.technicalchallenge.restControllers;



import com.backend.technicalchallenge.model.evaluation.EvaluatedUser;
import com.backend.technicalchallenge.model.evaluation.Evaluation;
import com.backend.technicalchallenge.model.evaluation.GroupComment;
import com.backend.technicalchallenge.model.questionnaire.Answer;
import com.backend.technicalchallenge.model.questionnaire.GroupApp;
import com.backend.technicalchallenge.model.questionnaire.Question;
import com.backend.technicalchallenge.services.interfaces.EvaluationService;
import com.backend.technicalchallenge.services.interfaces.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "*")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    @Autowired
    private QuestionnaireService questionnaireService;


    @GetMapping("/getAverageForQuestion")
    public ResponseEntity getAverage(@RequestParam("idEvent") Long idEvent,@RequestParam("idUser") Long idUser, @RequestParam("idGroup") Long idGroup){

        List<Object> result = evaluationService.getAverage(idEvent,idUser,idGroup);


        if(!result.isEmpty()){
            return new ResponseEntity<>(result, new HttpHeaders(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>("There's no evaluations on database", new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
        }
    }


    @GetMapping("/getEvaluations")
    public ResponseEntity<Object> getEvaluations(){
        List<Evaluation> evaluations = evaluationService.getEvaluations();
        if(!evaluations.isEmpty()){
            return new ResponseEntity<>(evaluations, new HttpHeaders(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>("There's no evaluations on database", new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
        }

    }

    @PostMapping("/persistEvaluation")
    public ResponseEntity<Object> persistEvaluation(@RequestParam(name = "idEvent") Long idEvent,
                                                    @RequestParam(name = "idEvaluator")Long idEvaluator,
                                                    @RequestParam(name = "idEvaluatedUser") Long idEvaluatedUser,
                                                    @RequestParam(name = "note") String note,
                                                    @RequestBody List<Answer> answers){


        Long evaluationId = evaluationService.persistEvaluation(idEvent, idEvaluator, idEvaluatedUser, note, answers);
        if(evaluationId != null){

            ResponseEntity<Object> result =  evaluationService.getEvaluation(evaluationId)!=null? new ResponseEntity<>(evaluationId, new HttpHeaders(), HttpStatus.OK): new ResponseEntity<>("couldn't saved it on database", new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);;
            return result;

        }else{
            return new ResponseEntity<>("Some of the id's isn't a valid one", new HttpHeaders(), HttpStatus.CONFLICT);
        }

    }

    @PostMapping("/persistEvaluationGroupComments")
    public ResponseEntity<Object> persistEvaluation(@RequestParam(name = "idEvaluation") Long idEvaluation,
                                                    @RequestBody List<GroupComment> groupComments){

            boolean isPersisted = evaluationService.persistEvaluationGroupComments(idEvaluation, groupComments);

            ResponseEntity<Object> result =  isPersisted ? new ResponseEntity<>("groupComments saved on database", new HttpHeaders(), HttpStatus.OK): new ResponseEntity<>("couldn't be saved on database", new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);;

        return result;


    }

    @GetMapping("/getScore")
    public ResponseEntity<Object> getScore(@RequestParam("idEvent") Long idEvent,@RequestParam("idUser") Long idUser){

        List<Object> answer = evaluationService.getScore(idEvent,idUser);
        ResponseEntity<Object> result =  answer !=null ? new ResponseEntity<>(answer, new HttpHeaders(), HttpStatus.OK): new ResponseEntity<>("couldn't be saved on database", new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);;

        return result;
    }

    @GetMapping("/getQuestionsGroups/{id}")
    public List<GroupApp> getGroups(@PathVariable("id") Long idEvent){
         return questionnaireService.getGroupAppOfEventQuestionnaire(idEvent);
    }
    @GetMapping("/getQuestions/{idEvent}/{idGroup}")
    public List<Question> getQuestionsByEvent(@PathVariable("idEvent") Long idEvent,@PathVariable("idGroup") Long idGroup){
        return questionnaireService.getQuestionsOfGroup(idEvent, idGroup);
    }

    @GetMapping("/getEvaluatedsUserById/{idUser}")
    public List<EvaluatedUser> getEvaluatedsUserById(@PathVariable("idUser") Long idUser){
        return evaluationService.getEvaluatedUserByUserApp(idUser);
    }

    @GetMapping("/getEvaluationsByEvaluatedUser")
    public List<Evaluation> getEvaluationsByEvaluatedUser(@RequestParam("idEvaluatedUser") Long idEvaluatedUser){
        List<Evaluation> answer = evaluationService.getEvaluationByEvaluatedUser(idEvaluatedUser);
        return answer;
    }

    @GetMapping("/getGroupCommentByGroupAppIdAndEvaluatedUser")
    public List<GroupComment> getGroupCommentByGroupAppIdAndEvaluatedUser(@RequestParam("idGroupApp") Long idGroupApp,@RequestParam("idEvaluatedUser") Long idEvaluatedUser){
        List<GroupComment> answer = evaluationService.getGroupCommentByGroupAppIdAndEvaluatedUser(idGroupApp,idEvaluatedUser);
        return answer;
    }



}
