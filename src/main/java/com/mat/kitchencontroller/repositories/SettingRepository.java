package com.mat.kitchencontroller.repositories;

import com.mat.kitchencontroller.domain.Setting;
import org.springframework.data.repository.CrudRepository;

public interface SettingRepository extends CrudRepository<Setting, Integer> {
    String findValueForName(String name);
}
