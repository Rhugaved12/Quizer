package com.telusko.quizapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.telusko.quizapp.dao.QuestionDao;
import com.telusko.quizapp.dao.QuizDao;
import com.telusko.quizapp.model.Quiz;
import com.telusko.quizapp.model.Response;
import com.telusko.quizapp.model.Question;
import com.telusko.quizapp.model.QuestionWrapper;


@Service
public class QuizService {
    @Autowired
    QuizDao quizDao;
    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<String> createQuiz(String category, int numberOfQuestions, String title) {
        try{
        List<Question> questions = questionDao.findRandomQuestionsByCategory(category, numberOfQuestions);
        System.out.println("Quiz Quesionts: ");
        System.out.println(questions);
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);
        quizDao.save(quiz);

        return new ResponseEntity<String>("Success", HttpStatus.CREATED);
    }
    catch (Exception e){
        e.printStackTrace();
    }
    return new ResponseEntity<String>("Failed to retrieve quiz", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Optional<Quiz> quiz = quizDao.findById(id);
        List<Question> questionsFromDB = quiz.get().getQuestions();
        List<QuestionWrapper> questionsForUser = new ArrayList<>();
        for(Question q: questionsFromDB){
            QuestionWrapper qw = new QuestionWrapper(q.getId(), q.getQuestionTitle(), q.getOption1(), q.getOption2(), q.getOption3(), q.getOption4());
            questionsForUser.add(qw);
        }
        return new ResponseEntity<List<QuestionWrapper>>(questionsForUser, HttpStatus.OK);
    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
        try{
        Quiz quiz = quizDao.findById(id).get();
        List<Question> questions = quiz.getQuestions();
        int right = 0;
        int i = 0;
        System.out.println("Submission");
        for(Response response: responses){
            System.out.println(response);
            System.out.println(questions);
            if(response.getResponse().equals(questions.get(i).getRightAnswer())){
                right++;}
            i++;
        }
        return new ResponseEntity<Integer>(right, HttpStatus.OK);
    }
    catch(Exception e){
        e.printStackTrace();
    }
        return new ResponseEntity<Integer>(0, HttpStatus.BAD_REQUEST);

    }

}
