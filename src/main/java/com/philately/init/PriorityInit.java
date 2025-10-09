package com.philately.init;

import com.philately.model.entity.Paper;
import com.philately.model.entity.enums.PaperName;
import com.philately.repository.PaperRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class PriorityInit implements CommandLineRunner {

    private final Map<PaperName, String> careerInformation = Map.of(
            PaperName.WOVE_PAPER, "Has an even texture without any particular distinguishing features.",
            PaperName.LAID_PAPER, "When held up to the light, shows parallel lines of greater or less width running across the stamp.",
            PaperName.GRANITE_PAPER, "Has tiny specks of coloured fibre in it, which can usually be seen with the naked eye."
    );

    private final PaperRepository paperRepository;

    public PriorityInit(PaperRepository paperRepository) {
        this.paperRepository = paperRepository;
    }


    @Override
    public void run(String... args) throws Exception {

        long count = this.paperRepository.count();

        if (count > 0) {
            return;
        }

        List<Paper> toInsert = Arrays.stream(PaperName.values())
                .map(name -> new Paper(name, careerInformation.get(name))).toList();

        this.paperRepository.saveAll(toInsert);
        }
    }

