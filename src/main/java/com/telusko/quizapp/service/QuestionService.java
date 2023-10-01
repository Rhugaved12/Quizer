package com.telusko.quizapp.service;

import java.util.List;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.telusko.quizapp.dao.QuestionDao;
import com.telusko.quizapp.model.Question;

@Service
public class QuestionService {
    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<List<Question>> getAllQuestions(){
        try{
        return new ResponseEntity<>(questionDao.findAll(), HttpStatus.OK);
    }
        catch (Exception e) {
            e.printStackTrace();
        }    
        return new ResponseEntity<>(Collections.emptyList(), HttpStatus.BAD_REQUEST);
}

    public ResponseEntity<List<Question>> getQuestionsByCategory(String category){
        try{
        return new ResponseEntity<>(questionDao.findByCategory(category), HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(Collections.emptyList(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> addQuestion(Question question){
        try{
        questionDao.save(question);
        return new ResponseEntity<String>("Success", HttpStatus.CREATED);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<String>("Failed to add question", HttpStatus.BAD_REQUEST);
    }
}
