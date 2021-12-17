package zoomanagement.api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import zoomanagement.api.domain.Animal;
import zoomanagement.api.domain.Diet;
import zoomanagement.api.domain.Pen;
import zoomanagement.api.domain.Species;
import zoomanagement.api.repository.*;

import java.util.ArrayList;

@SpringBootApplication
public class ZooManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZooManagementApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(AnimalRepository animalRepository, SpeciesRepository speciesRepository, DietRepository dietRepository, PenRepository penRepository) {
        return (args) -> {
            if (penRepository.findAll().isEmpty()) {
                Pen pen1 = penRepository.save(Pen.builder()
                        .name("BearPublic")
                        .location("12:13")
                        .surface("50")
                        .status("open")
                        .description("forest")
                        .species(speciesRepository.findByName("Bear").orElse(null))
                        .animals(new ArrayList<>())
                        .build());
            }

            if (animalRepository.findAll().isEmpty()) {
                Diet diet1 = dietRepository.save(Diet.builder()
                        .recommendations("meat, honey, insects")
                        .schedule("monday:sunday-11:30-17:30")
                        .preferences(" meat , honey ")
                        .build());

                Animal animal1 = animalRepository.save(Animal.builder()
                        .name("Bruno")
                        .age("12:3")
                        .sex("male")
                        .status("active")
                        .peculiarities("very_big")
                        .species(speciesRepository.findByName("Bear").orElse(null))
                        .diet(diet1)
                        .parents(new ArrayList<>())
                        .children(new ArrayList<>())
                        .pen(penRepository.findByName("BearPublic").orElse(null))
                        .build());

                diet1.setAnimal(animal1);
                dietRepository.save(diet1);
            }
        };
    }

}
