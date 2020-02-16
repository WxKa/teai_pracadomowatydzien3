package pl.wk.teai_3.service;

import pl.wk.teai_3.model.Car;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ICarService {

    List<Car> getCars();
    Car getCarById(long id);
    List<Car> getCarsByColor(String color);
    boolean addCar(Car car);
    boolean modifyCar( Car car, Map<String, String> requestParams );
    boolean removeCar(Car car);
}
