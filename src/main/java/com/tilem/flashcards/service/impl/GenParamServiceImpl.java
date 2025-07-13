package com.tilem.flashcards.service.impl;

import com.tilem.flashcards.data.dto.GenParamDTO;
import com.tilem.flashcards.data.entity.GenParam;
import com.tilem.flashcards.mapper.GenParamMapper;
import com.tilem.flashcards.repository.GenParamRepository;
import com.tilem.flashcards.service.GenParamService;
import org.springframework.stereotype.Service;

@Service
public class GenParamServiceImpl extends GenericServiceImpl<GenParam, GenParamDTO, GenParamRepository> implements GenParamService {

    public GenParamServiceImpl(GenParamRepository repository, GenParamMapper genParamMapper) {
        super(repository, genParamMapper, GenParam.class);
    }
}