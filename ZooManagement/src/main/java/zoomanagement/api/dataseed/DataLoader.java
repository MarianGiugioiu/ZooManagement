package zoomanagement.api.dataseed;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import zoomanagement.api.domain.*;
import zoomanagement.api.repository.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

//Seed the database before usage
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
        newData();
    }

    private void newData() {
        //animalRepository.deleteById(UUID.fromString("1a30fa1d-7e49-1097-817e-4970ad460001"));
    }

    private void loadData() {
        if (animalRepository.findAll().isEmpty()) {
            //Species
            Species species1 = speciesRepository.save(Species.builder()
                    .name("Black_Bear")
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
                    .species(species1)
                    .animals(new ArrayList<>())
                    .build());

            Pen pen2 = penRepository.save(Pen.builder()
                    .name("LionPublic")
                    .location("11:5")
                    .surface(45.0)
                    .status(PenStatusType.maintenance.name())
                    .description("#savannah#")
                    .species(species2)
                    .animals(new ArrayList<>())
                    .build());

            Pen pen3 = penRepository.save(Pen.builder()
                    .name("BearPublicReserve")
                    .location("16:12")
                    .surface(50.0)
                    .status(PenStatusType.inactive.name())
                    .description("#forest#")
                    .species(species1)
                    .animals(new ArrayList<>())
                    .build());

            //Diets
            Diet diet1 = dietRepository.save(Diet.builder()
                    .recommendations("#meat#,#honey#,#insects#,#fruits#")
                    .schedule("11:30-17:30")
                    .preferences("#meat#,#honey#,#fruits#")
                    .build());

            Diet diet2 = dietRepository.save(Diet.builder()
                    .recommendations("#meat#,#honey#,#insects#")
                    .schedule("11:30-17:30")
                    .preferences("#meat#,#honey#")
                    .build());

            Diet diet3 = dietRepository.save(Diet.builder()
                    .recommendations("#meat#")
                    .schedule("11:30-17:30")
                    .preferences("#meat#")
                    .build());


            //Animals
            Animal animal0 = animalRepository.save(Animal.builder()
                    .name("Unknown")
                    .age("")
                    .sex("")
                    .status("")
                    .peculiarities("")
                    .species(null)
                    .diet(null)
                    .parents(new ArrayList<>())
                    .children(new ArrayList<>())
                    .pen(null)
                    .build());

            Animal animal1 = animalRepository.save(Animal.builder()
                    .name("Bruno")
                    .age("12:3")
                    .sex("male")
                    .status("active")
                    .peculiarities("very_big")
                    .species(species1)
                    .diet(diet1)
                    .parents(new ArrayList<>((Arrays.asList(animal0, animal0))))
                    .children(new ArrayList<>())
                    .pen(pen1)
                    .build());

            diet1.setAnimal(animal1);
            dietRepository.save(diet1);

            Animal animal2 = animalRepository.save(Animal.builder()
                    .name("Lisa")
                    .age("5:2")
                    .sex("female")
                    .status("active")
                    .peculiarities("very_big, aggressive")
                    .species(species1)
                    .diet(diet2)
                    .parents(new ArrayList<>((Arrays.asList(animal0, animal1))))
                    .children(new ArrayList<>())
                    .pen(pen1)
                    .build());

            diet2.setAnimal(animal2);
            dietRepository.save(diet2);

            Animal animal3 = animalRepository.save(Animal.builder()
                    .name("Leonard")
                    .age("9:3")
                    .sex("male")
                    .status("active")
                    .peculiarities("aggressive")
                    .species(species2)
                    .diet(diet3)
                    .parents(new ArrayList<>((Arrays.asList(animal0, animal0))))
                    .children(new ArrayList<>())
                    .pen(pen2)
                    .build());

            diet3.setAnimal(animal3);
            dietRepository.save(diet3);

            //Employees
            Employee employee1 = employeeRepository.save(Employee.builder()
                    .name("Andrei")
                    .profession("Doctor")
                    .schedule("monday:saturday-13:30-16:30")
                    .specializations(new ArrayList<>(Arrays.asList(species1)))
                    .activities(new ArrayList<>())
                    .build());

            Employee employee2 = employeeRepository.save(Employee.builder()
                    .name("Alin")
                    .profession("CareTaker")
                    .schedule("monday:saturday-13:30-16:30")
                    .specializations(new ArrayList<>(Arrays.asList(species1)))
                    .activities(new ArrayList<>())
                    .build());

            //Activities
        }
    }


}
