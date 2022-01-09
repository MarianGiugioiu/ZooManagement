package zoomanagement.api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import zoomanagement.api.domain.*;
import zoomanagement.api.repository.*;

import java.util.ArrayList;

@SpringBootApplication
public class ZooManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZooManagementApplication.class, args);
    }

    /*@Bean
    public CommandLineRunner loadData(AnimalRepository animalRepository, SpeciesRepository speciesRepository, DietRepository dietRepository, PenRepository penRepository) {
        return (args) -> {
            if (animalRepository.findAll().isEmpty()) {
                //Species
                Species species1 = speciesRepository.save(Species.builder()
                        .name("Black Bear")
                        .naturalHabitat("#forest#")
                        .generalDescription("Black fur, medium size, omnivores")
                        .build());

                Species species2 = speciesRepository.save(Species.builder()
                        .name("Lion")
                        .naturalHabitat("#savannah#")
                        .generalDescription("Yellowish red fur, muscular, carnivorous")
                        .build());

                Species species3 = speciesRepository.save(Species.builder()
                        .name("Zebra")
                        .naturalHabitat("#savannah#")
                        .generalDescription("Black with white lines, fast, herbivorous")
                        .build());

                //Pens
                Pen pen1 = penRepository.save(Pen.builder()
                        .name("BearPublic")
                        .location("12:13")
                        .surface(50.0)
                        .status(PenStatusType.active.name())
                        .description("#forest#")
                        .species(speciesRepository.findByName("Black Bear").orElse(null))
                        .animals(new ArrayList<>())
                        .build());

                //Diets
                Diet diet1 = dietRepository.save(Diet.builder()
                        .recommendations("#meat#,#honey#,#insects#")
                        .schedule("11:30-17:30")
                        .preferences("#meat#,#honey#")
                        .build());

                //Animals
                Animal animal1 = animalRepository.save(Animal.builder()
                        .name("Bruno")
                        .age("12:3")
                        .sex("male")
                        .status("active")
                        .peculiarities("very_big")
                        .species(speciesRepository.findByName("BlackBear").orElse(null))
                        .diet(diet1)
                        .parents(new ArrayList<>())
                        .children(new ArrayList<>())
                        .pen(penRepository.findByName("BearPublic").orElse(null))
                        .build());

                diet1.setAnimal(animal1);
                dietRepository.save(diet1);

                //Employees

                //Activities
            }
        };
    }*/

}
