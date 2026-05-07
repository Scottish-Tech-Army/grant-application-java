package org.tfg.grant_java.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.tfg.grant_java.repository.QuestionRepository;
import org.tfg.grant_java.service.QuestionRecommender;

import java.io.IOException;

@Component
@Profile("dev")   // only runs when spring.profiles.active=dev
@Order(1)
public class DataSeeder implements ApplicationRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSeeder.class);
    private final QuestionRepository questionRepository;
    private final QuestionRecommender questionRecommender;

    public DataSeeder(
            QuestionRepository questionRepository, QuestionRecommender questionRecommender
    ) {
        LOGGER.trace("Initializing Constructor");
        this.questionRepository = questionRepository;
        this.questionRecommender = questionRecommender;
        LOGGER.trace("Terminating Constructor");
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws IOException {
        // Idempotent guard: don’t re-seed if data exists
        LOGGER.trace("Initializing Seed");
        if (questionRepository.count() > 0) return;
        questionRecommender.rebuild();
        LOGGER.trace("Terminating Seed");
    }
}