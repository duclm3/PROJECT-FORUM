package com.casestudy.service.hastag;

import com.casestudy.model.Hastag;
import com.casestudy.service.IGeneralService;

import java.util.Set;

public interface IHastagService extends IGeneralService<Hastag> {
    Iterable<Hastag> getTheMostUsedHashtags();
    Iterable<Hastag> getHastagByTopicId(Long topicId);
    Hastag saveAndReturn(Hastag hastag);
}
