package com.example.quidditchsim.House;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseService {

    public HouseService(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    @Autowired
    private final HouseRepository houseRepository;

    public List<House> getHouses() {
        return houseRepository.findAll();
    }

    public House getOneHouse(Long houseId) {
        return houseRepository.findById(houseId).get();
    }


}
