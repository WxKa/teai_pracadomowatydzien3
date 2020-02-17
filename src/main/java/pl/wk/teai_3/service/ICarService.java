package pl.wk.teai_3.service;

import pl.wk.teai_3.model.Car;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ICarService {

    List<Car> getCars();
    int getCarIndex( long id );
    Car getCarById( long id );
    List<Car> getCarsByColor(String color);
    boolean addCar(Car car);
    boolean modifyCar( Car car, Map<String, String> requestParams );
    boolean setCar( int index, Car car );
    boolean removeCar( int index, long id );
}
