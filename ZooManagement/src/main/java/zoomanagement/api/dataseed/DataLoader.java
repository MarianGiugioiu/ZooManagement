package zoomanagement.api.dataseed;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import zoomanagement.api.domain.*;
import zoomanagement.api.repository.*;

import java.util.ArrayList;

@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final AnimalRepository animalRepository;
    private final SpeciesRepository speciesRepository;
    private final PenRepository penRepository;
    private final DietRepository dietRepository;
    private final ActivityRepository activityRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public void run(String... args) throws Exception {
        loadData();
    }

    private void loadData() {
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
    }
}
