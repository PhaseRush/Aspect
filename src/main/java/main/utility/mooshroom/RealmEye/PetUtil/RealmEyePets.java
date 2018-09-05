package main.utility.mooshroom.RealmEye.PetUtil;

import java.util.ArrayList;
import java.util.List;

public class RealmEyePets {
    List<Pet> pets = new ArrayList<>();

    public List<Pet> getPets() {
        return pets;
    }

    public int getNumber_of_pets() {
        return pets.size();
    }

    public Pet findHighestLevelPet() {
        if (pets.size() == 1) {
            return pets.get(0);
        }

        Pet highestLevelPetSoFar = null;
        int highestLevelSoFar = 0;

        for (Pet p : pets) {
            int tempLevel = p.getAbility1().getLevel();
            if (tempLevel > highestLevelSoFar) {
                highestLevelPetSoFar = p;
                highestLevelSoFar = tempLevel;
            }
        }
        return highestLevelPetSoFar;
    }
}
