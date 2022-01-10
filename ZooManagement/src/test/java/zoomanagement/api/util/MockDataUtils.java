package zoomanagement.api.util;

import zoomanagement.api.DTO.ActivityDTO;
import zoomanagement.api.DTO.BabyAnimal;
import zoomanagement.api.DTO.DietDTO;
import zoomanagement.api.DTO.GenealogicalTree;
import zoomanagement.api.domain.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MockDataUtils {
    public static Pen aPen(String name) {
        return Pen.builder()
                .name(name)
                .location("12:13")
                .surface(50.0)
                .status("active")
                .description("#forest#")
                .species(null)
                .animals(new ArrayList<>())
                .build();
    }

    public static Pen aPen(String name, String description) {
        return Pen.builder()
                .name(name)
                .location("12:13")
                .surface(50.0)
                .status("active")
                .description(description)
                .species(null)
                .animals(new ArrayList<>())
                .build();
    }

    public static Pen aPen(String name, String location, String status, Species species) {
        return Pen.builder()
                .name(name)
                .location(location)
                .surface(50.0)
                .status(status)
                .description("#forest#")
                .species(species)
                .animals(new ArrayList<>())
                .build();
    }

    public static Species aSpecies(String name) {
        return Species.builder()
                .name(name)
                .naturalHabitat("#savannah#")
                .generalDescription("Black with white lines, fast, herbivorous")
                .build();
    }

    public static Species aSpecies(String name, String naturalHabitat) {
        return Species.builder()
                .name(name)
                .naturalHabitat(naturalHabitat)
                .generalDescription("Black with white lines, fast, herbivorous")
                .build();
    }

    public static Diet aDiet(String preferences) {
        return Diet.builder()
                .recommendations("#meat#,#honey#,#insects#")
                .schedule("11:30-17:30")
                .preferences(preferences)
                .build();
    }

    public static Diet aDiet(String preferences, String recommendations) {
        return Diet.builder()
                .recommendations(recommendations)
                .schedule("11:30-17:30")
                .preferences(preferences)
                .build();
    }

    public static DietDTO aDietDTO(String preferences, String recommendations) {
        return DietDTO.builder()
                .recommendations(recommendations)
                .schedule("11:30-17:30")
                .preferences(preferences)
                .build();
    }

    public static Animal unknownAnimal() {
        return Animal.builder()
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
                .build();
    }

    public static Animal anAnimal(String name) {
        return Animal.builder()
                .name(name)
                .age("12:3")
                .sex("male")
                .status("active")
                .peculiarities("very_big")
                .species(null)
                .diet(null)
                .parents(new ArrayList<>())
                .children(new ArrayList<>())
                .pen(null)
                .build();
    }

    public static Animal anAnimal(String name, String sex) {
        return Animal.builder()
                .name(name)
                .age("12:3")
                .sex(sex)
                .status("active")
                .peculiarities("very_big")
                .species(null)
                .diet(null)
                .parents(new ArrayList<>())
                .children(new ArrayList<>())
                .pen(null)
                .build();
    }

    public static Animal anAnimal(String name, String peculiarities, Species species) {
        return Animal.builder()
                .name(name)
                .age("12:3")
                .sex("male")
                .status("active")
                .peculiarities(peculiarities)
                .species(species)
                .diet(null)
                .parents(new ArrayList<>())
                .children(new ArrayList<>())
                .pen(null)
                .build();
    }

    public static Animal anAnimal(String name, Diet diet, Species species, Pen pen) {
        return Animal.builder()
                .name(name)
                .age("12:3")
                .sex("male")
                .status("active")
                .peculiarities("very_big")
                .species(species)
                .diet(diet)
                .parents(new ArrayList<>())
                .children(new ArrayList<>())
                .pen(pen)
                .build();
    }

    public static Employee anEmployee(String name) {
        return Employee.builder()
                .name(name)
                .profession("Doctor")
                .schedule("schedule")
                .specializations(null)
                .activities(null)
                .build();
    }

    public static GenealogicalTree aGenealogicalTree(String name, ArrayList<String> parents) {
        return GenealogicalTree.builder()
                .name(name)
                .parents(parents)
                .build();
    }

    public static BabyAnimal aBabyAnimal(String name, String motherName, String fatherName) {
        return BabyAnimal.builder()
                .name(name)
                .diet(null)
                .peculiarities("very_big")
                .sex("male")
                .parents(new ArrayList<>(Arrays.asList(motherName, fatherName)))
                .build();
    }

    public static ActivityDTO anActivityDTO(String name, List<String> employeeNames, String action, LocalDateTime startTime,
                                            LocalDateTime endTime, String penName) {
        return ActivityDTO.builder()
                .name(name)
                .action(action)
                .startTime(startTime)
                .endTime(endTime)
                .penName(penName)
                .employeeNames(employeeNames)
                .build();
    }

    public static ActivityDTO anActivityDTO(List<String> employeeNames) {
        return ActivityDTO.builder()
                .name("Cleaning BearPublic")
                .action("cleaning")
                .startTime(LocalDateTime.of(2022, 01, 8, 13, 30))
                .endTime(LocalDateTime.of(2022, 01, 8, 15, 30))
                .penName("BearPublic")
                .employeeNames(employeeNames)
                .build();
    }

    public static ActivityDTO anActivityDTO(List<String> employeeNames, LocalDateTime startTime, LocalDateTime endTime) {
        return ActivityDTO.builder()
                .name("Cleaning BearPublic1")
                .action("cleaning")
                .startTime(startTime)
                .endTime(endTime)
                .penName("BearPublic1")
                .employeeNames(employeeNames)
                .build();
    }

    public static Activity anActivity(LocalDateTime startTime, LocalDateTime endTime) {
        return Activity.builder()
                .name("Cleaning BearPublic2")
                .action("cleaning")
                .startTime(startTime)
                .endTime(endTime)
                .pen(null)
                .employees(null)
                .status("not started")
                .build();
    }

}
