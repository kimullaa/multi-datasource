package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SampleServiceImpl implements SampleService {
    @Qualifier("oldSampleRepository")
    private final SampleRepository oldSampleRepository;

    private final SampleRepository sampleRepository;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<String> findAll() {
        sampleRepository.lock();
        oldSampleRepository.lock();

        sampleRepository.findAll()
                .stream()
                .forEach(System.out::println);

        System.out.println("-------------------");

        oldSampleRepository.findAll()
                .stream()
                .forEach(System.out::println);

        return Collections.EMPTY_LIST;
    }
}
