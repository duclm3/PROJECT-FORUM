package com.casestudy.service.hastag;

import com.casestudy.model.Hastag;
import com.casestudy.repository.IHastagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class HastagService implements IHastagService{
    @Autowired
    private IHastagRepository hastagRepository;

    @Override
    public Iterable<Hastag> findAll() {
        return hastagRepository.findAll();
    }

    @Override
    public Optional<Hastag> findById(Long id) {
        return hastagRepository.findById(id);
    }

    @Override
    public void save(Hastag hastag) {
        hastagRepository.save(hastag);
    }

    @Override
    public void remove(Long id) {
        hastagRepository.deleteById(id);
    }

    @Override
    public Iterable<Hastag> getTheMostUsedHashtags() {
       return hastagRepository.getTheMostUsedHashtags();
    }

    @Override
    public Hastag saveAndReturn(Hastag hastag){
        return hastagRepository.save(hastag);
    }

    @Override
    public Iterable<Hastag> getHastagByTopicId(Long topicId) {
        return hastagRepository.getHastagByTopicId(topicId);
    }

    public Iterable<Hastag> getHastagByTopicId1(Long topicId) {
        return (Set<Hastag>) hastagRepository.getHastagByTopicId(topicId);
    }

    public static void main(String[] args) {
        System.out.println("a");
        HastagService hastagService = new HastagService();
        hastagService.getHastagByTopicId(1l);
        System.out.println("Test");
    }
}
